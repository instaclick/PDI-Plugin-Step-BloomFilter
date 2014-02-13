package com.instaclick.filter;

import java.io.IOException;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.HashFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.instaclick.filter.provider.FilterProvider;

/**
 * Single Bloom filter {@link com.skjegstad.utils.BloomFilter}
 *
 * @author Fabio B. Silva <fabio.bat.silva@gmail.com>
 */
public class SingleBloomDataFilter implements DataFilter
{
    /**
     * filter file name
     */
    protected String filterFileName;

    /**
     * BloomFilter
     */
    protected BloomFilter<String> filter;

    /**
     * Filter configuration
     */
    protected FilterConfig config;

    /**
     * Filter provider responsible for save and retrieve filter
     */
    protected FilterProvider filterProvider;

    /**
     * HashFunction
     */
    protected HashFunction hashFunction;

    /**
     * @param config            Filter configuration
     * @param filterProvider    Filter provider
     * @param hashFunction      Hash function
     */
    public SingleBloomDataFilter(FilterConfig config, FilterProvider filterProvider, HashFunction hashFunction)
    {
        this.config         = config;
        this.hashFunction   = hashFunction;
        this.filterProvider = filterProvider;
        this.filterFileName = config.getFilterFileName() + ".bloom";
    }

    /**
     * Retrieve an filter base on a data hash obtained from {@link getDataHashCode}
     *
     * @return
     * @throws java.io.IOException
     */
    protected BloomFilter getFilter() throws IOException
    {
        if (this.filter != null) {
            return this.filter;
        }

        if (filterProvider.hasFilter(this.filterFileName)) {
            this.filter = (BloomFilter) filterProvider.loadFilter(this.filterFileName);

            return this.filter;
        }

        double falsePositiveProbability = config.getFalsePositiveProbability();
        int expectedNumberOfElements    = config.getExpectedNumberOfElements();

        this.filter = BloomFilter.create(StringHashFunnel.INSTANCE, expectedNumberOfElements, falsePositiveProbability);
        
        return this.filter;
    }

    /**
     * Hashes the string if the HashFunction is available
     *
     * @param data
     *
     * @return
     */
    private String hashString(String hash)
    {
        if (hashFunction != null) {
            return hashFunction.hashString(hash).toString();
        }

        return hash;
    }

    /**
     * {@inheritDoc}
     */
    public boolean add(Data data)
    {
        try {
            return this.getFilter().put(hashString(data.getHash()));
        } catch (IOException ex) {
            Logger.getLogger(SingleBloomDataFilter.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(Data data)
    {
        try {
            return this.getFilter().mightContain(hashString(data.getHash()));
        } catch (IOException ex) {
            Logger.getLogger(SingleBloomDataFilter.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void flush()
    {
        if (this.filter == null) {
            return;
        }

        try {
            filterProvider.saveFilter(this.filterFileName, this.filter);
        } catch (IOException ex) {
            Logger.getLogger(SingleBloomDataFilter.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }

        this.filter = null;
    }
}