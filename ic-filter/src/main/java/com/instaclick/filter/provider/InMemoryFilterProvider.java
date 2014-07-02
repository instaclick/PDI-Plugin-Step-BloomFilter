package com.instaclick.filter.provider;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * In memory provider, valid only for test proposes
 *
 * @author Fabio B. Silva <fabio.bat.silva@gmail.com>
 */
public class InMemoryFilterProvider implements FilterProvider
{
    /**
     * Map of {@link BloomFilter}
     */
    private final Map<String, Serializable> map = new HashMap<String, Serializable>();
    
    /**
     * {@inheritDoc}
     */
    public boolean hasFilter(final String name) throws IOException
    {
        return map.containsKey(name);
    }

    /**
     * {@inheritDoc}
     */
    public Serializable loadFilter(final String name) throws IOException
    {
        return map.get(name);
    }

    /**
     * {@inheritDoc}
     */
    public void saveFilter(final String name, Serializable filter) throws IOException
    {
        map.put(name, filter);
    }

    public void moveFilter(final String source, final String target) throws IOException
    {
        map.put(target, map.get(source));
        map.remove(source);
    }
}
