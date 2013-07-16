package net.nationalfibre.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.nationalfibre.filter.provider.FilterProvider;

import com.skjegstad.utils.BloomFilter;

public class BloomDataFilter implements DataFilter {

    private FilterConfig config;
    private Set<String> dirtyFilters;
    private FilterProvider filterProvider;
    private Map<String, BloomFilter<String>> filters;

    public BloomDataFilter(FilterConfig config, FilterProvider filterProvider) {
        this.config         = config;
        this.filterProvider = filterProvider;
        this.dirtyFilters   = new HashSet<String>();
        this.filters        = new HashMap<String, BloomFilter<String>>();
    }

    private int getDataHashCode(Data data) {
        double division = config.getTimeDivision();
        Long timestamp  = data.getTimestamp();

        return (int) Math.round((timestamp / division));
    }

    private String getFilterName(Data data) {
        return getFilterName(getDataHashCode(data));
    }

    private String getFilterName(int dataHash) {
        return dataHash + ".bloom";
    }

    private void markAsDirty(String name) {
        dirtyFilters.add(name);
    }

    private boolean hasProviderFilter(int dataHash) {
        String name = getFilterName(dataHash);

        try {
            return filterProvider.hasFilter(name);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private boolean hasProviderFilter(Data data) {
        return hasProviderFilter(getDataHashCode(data));
    }

    private BloomFilter<String> getProviderFilter(int dataHash) {

        String name = getFilterName(dataHash);

        try {
            BloomFilter<String> filter = filterProvider.loadFilter(name);

            filters.put(name, filter);

            return filter;

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private BloomFilter<String> getProviderFilter(Data data) {
        return getProviderFilter(getDataHashCode(data));
    }

    private BloomFilter<String> getFilter(Data data) {
        String name = getFilterName(data);

        if (filters.containsKey(name)) {
            return filters.get(name);
        }

        if (hasProviderFilter(data)) {
            return getProviderFilter(data);
        }

        filters.put(name, new BloomFilter<String>(config.getFalsePositiveProbability(), config.getExpectedNumberOfElements()));
        markAsDirty(name);

        return filters.get(name);
    }

    public boolean add(Data data) {

        if (contains(data)) {
            return false;
        }

        BloomFilter<String> filter = getFilter(data);
        String hash = data.getHash();

        filter.add(hash);
        markAsDirty(getFilterName(data));

        return true;
    }

    public void flush() {
        try {
            for (String name : dirtyFilters) {
                filterProvider.saveFilter(name, filters.get(name));
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        filters.clear();
        dirtyFilters.clear();
    }

    private boolean memoryContains(Data data) {

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

    private boolean providerContains(Data data) {

        int lookups = config.getNumberOfLookups();
        int code    = getDataHashCode(data);
        String hash = data.getHash();

        for (int i = 0; i <= lookups; i++) {
            int current = code - i;

            if ( ! hasProviderFilter(current)) {
                continue;
            }

            if (getProviderFilter(current).contains(hash)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean contains(Data data) {

        if (memoryContains(data)) {
            return true;
        }

        return providerContains(data);
    }
}