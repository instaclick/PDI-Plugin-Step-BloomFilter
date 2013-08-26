package net.nationalfibre.filter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;
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
        BloomFilter<String> filter      = BloomFilter.create(StringHashFunnel.INSTANCE, expectedNumberOfElements, falsePositiveProbability);

        return filter;
    }

    /**
     * {@inheritDoc}
     */
    protected boolean filterContains(BloomFilter filter, String hash)
    {
        return filter.mightContain(hash);
    }

    /**
     * {@inheritDoc}
     */
    protected void filterAdd(BloomFilter filter, String hash)
    {
        filter.put(hash);
    }
}

enum StringHashFunnel implements Funnel<String>
{
    INSTANCE;
    public void funnel(String hash, PrimitiveSink into)
    {
        into.putString(hash);
    }
}