package net.nationalfibre.filter.provider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.skjegstad.utils.BloomFilter;

public class InMemoryFilterProvider implements FilterProvider {

    private Map<String, BloomFilter<String>> map = new HashMap<String, BloomFilter<String>>();

    @Override
    public boolean hasFilter(String name) throws IOException {
        return map.containsKey(name);
    }

    @Override
    public BloomFilter<String> loadFilter(String name) throws IOException {
        return map.get(name);
    }

    @Override
    public void saveFilter(String name, BloomFilter<String> filter) throws IOException {
        map.put(name, filter);
    }
}
