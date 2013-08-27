package net.nationalfibre.filter;

import com.google.common.hash.HashFunction;
import java.util.HashSet;
import java.util.Set;
import net.nationalfibre.filter.provider.FilterProvider;

/**
 * Bloom filter based on {@link java.util.HashSet}
 *
 * @author Fabio B. Silva <fabios@nationalfibre.net>
 */
public class MapDataFilter extends BaseDataFilter
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
    protected FilterAdapter createFilter()
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

class HashSetAdapter implements FilterAdapter
{
    private Set<String> filter = new HashSet<String>();

    public void add(String hash)
    {
        filter.add(hash);
    }

    public boolean contains(String hash)
    {
        return filter.contains(hash);
    }
}