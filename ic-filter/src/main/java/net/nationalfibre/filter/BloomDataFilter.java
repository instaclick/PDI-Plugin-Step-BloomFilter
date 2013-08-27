package net.nationalfibre.filter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.HashFunction;
import com.google.common.hash.PrimitiveSink;
import net.nationalfibre.filter.provider.FilterProvider;

/**
 * Bloom filter based on {@link com.skjegstad.utils.BloomFilter}
 *
 * @author Fabio B. Silva <fabios@nationalfibre.net>
 */
public class BloomDataFilter extends BaseDataFilter
{
    /**
     * @param config            Filter configuration
     * @param filterProvider    Filter provider
     */
    public BloomDataFilter(FilterConfig config, FilterProvider filterProvider)
    {
        super(config, filterProvider, null);
    }

    /**
     * @param config            Filter configuration
     * @param filterProvider    Filter provider
     * @param hashFunction      Hash function
     */
    public BloomDataFilter(FilterConfig config, FilterProvider filterProvider, HashFunction hashFunction)
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
    protected FilterAdapter createFilter()
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

class BloomFilterAdapter implements FilterAdapter
{
    private BloomFilter<String> filter;

    public BloomFilterAdapter(BloomFilter<String> filter)
    {
        this.filter = filter;
    }

    public void add(String hash)
    {
        filter.put(hash);
    }

    public boolean contains(String hash)
    {
        return filter.mightContain(hash);
    }
}