package net.nationalfibre.filter.provider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.skjegstad.utils.BloomFilter;

/**
 * In memory provider, valid only for test proposes
 *
 * @author Fabio B. Silva <fabios@nationalfibre.net>
 */
public class InMemoryFilterProvider implements FilterProvider
{
    /**
     * Map of {@link BloomFilter}
     */
    private Map<String, BloomFilter<String>> map = new HashMap<String, BloomFilter<String>>();

    /**
     * {@inheritDoc}
     */
    public boolean hasFilter(String name) throws IOException
    {
        return map.containsKey(name);
    }

    /**
     * {@inheritDoc}
     */
    public BloomFilter<String> loadFilter(String name) throws IOException
    {
        return map.get(name);
    }

    /**
     * {@inheritDoc}
     */
    public void saveFilter(String name, BloomFilter<String> filter) throws IOException
    {
        map.put(name, filter);
    }
}
