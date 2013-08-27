package net.nationalfibre.filter;

import com.google.common.hash.HashFunction;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.nationalfibre.filter.provider.FilterProvider;

/**
 * Base filter implementation
 *
 * @author Fabio B. Silva <fabios@nationalfibre.net>
 */
abstract class BaseDataFilter implements DataFilter
{
    /**
     * Filter configuration
     */
    protected FilterConfig config;

    /**
     * Set of filters that should be flushed
     */
    protected Set<String> dirtyFilters;

    /**
     * Filter provider responsible for save and retrieve filter
     */
    protected FilterProvider filterProvider;

    /**
     * Map that caches providers lookups
     */
    protected Map<Integer, Boolean> containsFilters;

    /**
     * Map of {@link FilterAdapter}
     */
    protected Map<String, FilterAdapter> filters;

    /**
     * HashFunction
     */
    protected HashFunction hashFunction;

    /**
     * @param config            Filter configuration
     * @param filterProvider    Filter provider
     * @param hashFunction      Hash function
     */
    public BaseDataFilter(FilterConfig config, FilterProvider filterProvider, HashFunction hashFunction)
    {
        this.config          = config;
        this.hashFunction    = hashFunction;
        this.filterProvider  = filterProvider;
        this.dirtyFilters    = new HashSet<String>();
        this.filters         = new HashMap<String, FilterAdapter>();
        this.containsFilters = new HashMap<Integer, Boolean>();
    }

    /**
     * Creates a new filter
     *
     * @return
     */
    protected abstract FilterAdapter createFilter();

    /**
     * Creates a filter name base on a integer hash code
     *
     * @param data
     * @return
     */
    protected abstract String createFilterName(int dataHash);

    /**
     * Hashes the string if the HashFunction is available
     *
     * @param data
     *
     * @return
     */
    private String hashString(String hash)
    {
        if (hashFunction != null) {
            return hashFunction.hashString(hash).toString();
        }

        return hash;
    }

    /**
     * Creates an integer hash for the given {@link Data}
     *
     * @param data
     * 
     * @return
     */
    private int hashCodeFilterName(Data data)
    {
        double division = config.getTimeDivision();
        Long timestamp  = data.getTimestamp();

        return (int) Math.round((timestamp / division));
    }

    /**
     * Creates a filter name base on the given {@link Data}
     *
     * @param data
     * @return
     */
    private String getFilterName(Data data)
    {
        return createFilterName(hashCodeFilterName(data));
    }

    /**
     * Mark an filter as dirty, it will be flushed !
     *
     * @param data
     * @return
     */
    private void markAsDirty(String name)
    {
        dirtyFilters.add(name);
    }

    /**
     * Check if the provider contains the given data hash obtained from {@link getDataHashCode}
     *
     * @param dataHash
     * @return
     */
    private boolean hasProviderFilter(int dataHash)
    {
        if (containsFilters.containsKey(dataHash)) {
            return containsFilters.get(dataHash);
        }

        String name = createFilterName(dataHash);

        try {
            Boolean contains = filterProvider.hasFilter(name);

            containsFilters.put(dataHash, contains);

            return contains;

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Check if the provider contains the given {@link Data}
     *
     * @param dataHash
     * @return
     */
    private boolean hasProviderFilter(Data data)
    {
        return hasProviderFilter(hashCodeFilterName(data));
    }

    /**
     * Retrieve an filter base on a data hash obtained from {@link getDataHashCode}
     *
     * @param dataHash
     * @return
     */
    protected FilterAdapter loadFilter(int dataHash)
    {
        String name = createFilterName(dataHash);

        if (filters.containsKey(name)) {
            return filters.get(name);
        }

        try {
            FilterAdapter filter = (FilterAdapter) filterProvider.loadFilter(name);

            filters.put(name, filter);

            return filter;

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Retrieve an filter for the given {@link Data}
     *
     * @param data
     * @return
     */
    private FilterAdapter loadFilter(Data data)
    {
        return loadFilter(hashCodeFilterName(data));
    }

    /**
     * Retrieve or create an {@link BloomFilter} for the given {@link Data}
     *
     * @param data
     * @return
     */
    private FilterAdapter loadOrCreateFilter(Data data)
    {
        String name = getFilterName(data);

        if (filters.containsKey(name)) {
            return filters.get(name);
        }

        if (hasProviderFilter(data)) {
            return loadFilter(data);
        }

        filters.put(name, createFilter());
        markAsDirty(name);

        return filters.get(name);
    }

    /**
     * Lookup into the loaded filter for the given {@link Data}
     *
     * @param data
     * @return
     */
    private boolean memoryContains(Data data)
    {
        int lookups = config.getNumberOfLookups();
        int code    = hashCodeFilterName(data);
        String hash = hashString(data.getHash());

        for (int i = 0; i <= lookups; i++) {
            int current = code - i;
            String name = createFilterName(current);

            if ( ! filters.containsKey(name)) {
                continue;
            }

            if (filters.get(name).contains(hash)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Lookup into the {@link FilterProvider} for the given {@link Data}
     * 
     * @param data
     * @return
     */
    private boolean providerContains(Data data)
    {
        int lookups = config.getNumberOfLookups();
        int code    = hashCodeFilterName(data);
        String hash = hashString(data.getHash());

        for (int i = 0; i <= lookups; i++) {
            int current = code - i;

            if ( ! hasProviderFilter(current)) {
                continue;
            }

            if (loadFilter(current).contains(hash)) {
                return true;
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean add(Data data)
    {
        if (contains(data)) {
            return false;
        }

        FilterAdapter filter    = loadOrCreateFilter(data);
        String hash             = hashString(data.getHash());

        filter.add(hash);
        markAsDirty(getFilterName(data));

        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void flush()
    {
        try {
            for (String name : dirtyFilters) {
                filterProvider.saveFilter(name, filters.get(name));
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        filters.clear();
        dirtyFilters.clear();
        containsFilters.clear();
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(Data data)
    {
        if (memoryContains(data)) {
            return true;
        }

        return providerContains(data);
    }
}