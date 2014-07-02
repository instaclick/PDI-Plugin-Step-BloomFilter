package com.instaclick.filter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.HashFunction;
import com.google.common.hash.PrimitiveSink;
import com.instaclick.filter.provider.FilterProvider;

/**
 * Bloom filter based on {@link com.skjegstad.utils.BloomFilter}
 *
 * @author Fabio B. Silva <fabio.bat.silva@gmail.com>
 */
public class BloomDataFilter extends BaseDataFilter<BloomFilter>
{
    /**
     * @param config            Filter configuration
     * @param filterProvider    Filter provider
     */
    public BloomDataFilter(final FilterConfig config, final FilterProvider filterProvider)
    {
        super(config, filterProvider, null);
    }

    /**
     * @param config            Filter configuration
     * @param filterProvider    Filter provider
     * @param hashFunction      Hash function
     */
    public BloomDataFilter(final FilterConfig config, final FilterProvider filterProvider, final HashFunction hashFunction)
    {
        super(config, filterProvider, hashFunction);
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
    protected FilterAdapter createFilterAdapter()
    {
        double falsePositiveProbability = config.getFalsePositiveProbability();
        int expectedNumberOfElements    = config.getExpectedNumberOfElements();
        BloomFilter<String> filter      = BloomFilter.create(StringHashFunnel.INSTANCE, expectedNumberOfElements, falsePositiveProbability);

        return new BloomFilterAdapter(filter);
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

class BloomFilterAdapter implements FilterAdapter<BloomFilter>
{
    private BloomFilter<String> filter;

    public BloomFilterAdapter(BloomFilter<String> filter)
    {
        this.filter = filter;
    }

    /**
     * {@inheritDoc}
     */
    public void add(String hash)
    {
        filter.put(hash);
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(String hash)
    {
        return filter.mightContain(hash);
    }

    /**
     * {@inheritDoc}
     */
    public BloomFilter getFilter()
    {
        return this.filter;
    }

    /**
     * {@inheritDoc}
     */
    public void setFilter(BloomFilter filter)
    {
        this.filter = filter;
    }
}