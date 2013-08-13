package net.nationalfibre.filter;

import java.util.HashSet;
import net.nationalfibre.filter.provider.FilterProvider;

/**
 * Bloom filter based on {@link java.util.HashSet}
 *
 * @author Fabio B. Silva <fabios@nationalfibre.net>
 */
public class MapDataFilter extends BaseDataFilter<HashSet>
{
     /**
     * @param config            Filter configuration
     * @param filterProvider    Filter provider
     */
    public MapDataFilter(FilterConfig config, FilterProvider filterProvider)
    {
        super(config, filterProvider);
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
    protected HashSet createFilter()
    {
        return new HashSet();
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