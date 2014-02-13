package com.instaclick.filter;

import java.util.HashSet;
import com.google.common.hash.HashFunction;
import com.instaclick.filter.provider.FilterProvider;

/**
 * Bloom filter based on {@link java.util.HashSet}
 *
 * @author Fabio B. Silva <fabio.bat.silva@gmail.com>
 */
public class MapDataFilter extends BaseDataFilter<HashSet>
{
    /**
     * @param config            Filter configuration
     * @param filterProvider    Filter provider
     */
    public MapDataFilter(FilterConfig config, FilterProvider filterProvider)
    {
        super(config, filterProvider, null);
    }

    /**
     * @param config            Filter configuration
     * @param filterProvider    Filter provider
     * @param hashFunction      Hash function
     */
    public MapDataFilter(FilterConfig config, FilterProvider filterProvider, HashFunction hashFunction)
    {
        super(config, filterProvider, hashFunction);
    }

    /**
     * {@inheritDoc}
     */
    protected String createFilterName(int dataHash)
    {
        return dataHash + ".map";
    }

    /**
     * {@inheritDoc}
     */
    protected FilterAdapter createFilterAdapter()
    {
        return new HashSetAdapter();
    }

    /**
     * {@inheritDoc}
     */
    protected boolean filterContains(HashSet filter, String hash)
    {
        return filter.contains(hash);
    }

    /**
     * {@inheritDoc}
     */
    protected void filterAdd(HashSet filter, String hash)
    {
        filter.add(hash);
    }
}

class HashSetAdapter implements FilterAdapter<HashSet>
{
    private HashSet<String> filter = new HashSet<String>();

    /**
     * {@inheritDoc}
     */
    public void add(String hash)
    {
        filter.add(hash);
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(String hash)
    {
        return filter.contains(hash);
    }

    /**
     * {@inheritDoc}
     */
    public HashSet getFilter()
    {
        return this.filter;
    }

    /**
     * {@inheritDoc}
     */
    public void setFilter(HashSet filter)
    {
        this.filter = filter;
    }
}