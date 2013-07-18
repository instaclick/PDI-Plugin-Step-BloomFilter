package net.nationalfibre.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.nationalfibre.filter.provider.FilterProvider;

import com.skjegstad.utils.BloomFilter;

/**
 * Bloom filter based on {@link com.skjegstad.utils.BloomFilter}
 *
 * @author Fabio B. Silva <fabios@nationalfibre.net>
 */
public class BloomDataFilter implements DataFilter
{
    /**
     * Filter configuration
     */
    private FilterConfig config;

    /**
     * Set of filters that should be flushed
     */
    private Set<String> dirtyFilters;

    /**
     * Filter provider responsible for save and retrieve filter
     */
    private FilterProvider filterProvider;

    /**
     * Map that caches providers lookups
     */
    private Map<Integer, Boolean> containsFilters;

    /**
     * Map of {@link com.skjegstad.utils.BloomFilter}
     */
    private Map<String, BloomFilter<String>> filters;

    /**
     * @param config            Filter configuration
     * @param filterProvider    Filter provider
     */
    public BloomDataFilter(FilterConfig config, FilterProvider filterProvider)
    {
        this.config          = config;
        this.filterProvider  = filterProvider;
        this.dirtyFilters    = new HashSet<String>();
        this.containsFilters = new HashMap<Integer, Boolean>();
        this.filters         = new HashMap<String, BloomFilter<String>>();
    }

    /**
     * Creates an integer hash for the given {@link Data}
     *
     * @param data
     * 
     * @return
     */
    private int getDataHashCode(Data data)
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
        return getFilterName(getDataHashCode(data));
    }

    /**
     * Creates a filter name base on a integer hash obtained from {@link getDataHashCode}
     *
     * @param data
     * @return
     */
    private String getFilterName(int dataHash)
    {
        return dataHash + ".bloom";
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

        String name = getFilterName(dataHash);

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
        return hasProviderFilter(getDataHashCode(data));
    }

    /**
     * Retrieve an {@link BloomFilter} base on a data hash obtained from {@link getDataHashCode}
     *
     * @param dataHash
     * @return
     */
    private BloomFilter<String> loadFilter(int dataHash)
    {
        String name = getFilterName(dataHash);

        try {
            BloomFilter<String> filter = filterProvider.loadFilter(name);

            filters.put(name, filter);

            return filter;

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Retrieve an {@link BloomFilter} for the given {@link Data}
     *
     * @param data
     * @return
     */
    private BloomFilter<String> loadFilter(Data data)
    {
        return loadFilter(getDataHashCode(data));
    }

    /**
     * Retrieve or create an {@link BloomFilter} for the given {@link Data}
     *
     * @param data
     * @return
     */
    private BloomFilter<String> loadOrCreateFilter(Data data)
    {
        String name = getFilterName(data);

        if (filters.containsKey(name)) {
            return filters.get(name);
        }

        if (hasProviderFilter(data)) {
            return loadFilter(data);
        }

        filters.put(name, new BloomFilter<String>(config.getFalsePositiveProbability(), config.getExpectedNumberOfElements()));
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
        int code    = getDataHashCode(data);
        String hash = data.getHash();

        for (int i = 0; i <= lookups; i++) {
            int current = code - i;
            String name = getFilterName(current);

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
        int code    = getDataHashCode(data);
        String hash = data.getHash();

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

        BloomFilter<String> filter = loadOrCreateFilter(data);
        String hash = data.getHash();

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