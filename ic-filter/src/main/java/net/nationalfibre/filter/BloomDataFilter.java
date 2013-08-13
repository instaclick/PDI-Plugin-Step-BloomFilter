package net.nationalfibre.filter;

import com.skjegstad.utils.BloomFilter;
import net.nationalfibre.filter.provider.FilterProvider;

/**
 * Bloom filter based on {@link com.skjegstad.utils.BloomFilter}
 *
 * @author Fabio B. Silva <fabios@nationalfibre.net>
 */
public class BloomDataFilter extends BaseDataFilter<BloomFilter>
{
    /**
     * @param config            Filter configuration
     * @param filterProvider    Filter provider
     */
    public BloomDataFilter(FilterConfig config, FilterProvider filterProvider)
    {
        super(config, filterProvider);
    }

    /**
     * {@inheritDoc}
     */
    protected String createFilterName(int dataHash)
    {
        return dataHash + ".bloom";
    }

    /**
     * {@inheritDoc}
     */
    protected BloomFilter createFilter()
    {
        double falsePositiveProbability = config.getFalsePositiveProbability();
        int expectedNumberOfElements    = config.getExpectedNumberOfElements();

        return new BloomFilter<String>(falsePositiveProbability, expectedNumberOfElements);
    }

    /**
     * {@inheritDoc}
     */
    protected boolean filterContains(BloomFilter filter, String hash)
    {
        return filter.contains(hash);
    }

    /**
     * {@inheritDoc}
     */
    protected void filterAdd(BloomFilter filter, String hash)
    {
        filter.add(hash);
    }
}