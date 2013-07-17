package net.nationalfibre.filter.provider;

import java.io.IOException;

import com.skjegstad.utils.BloomFilter;

/**
 * Defines a behavior that should be implement by all filter providers
 *
 * @author Fabio B. Silva <fabios@nationalfibre.net>
 */
public interface FilterProvider
{
    /**
     * Checks if the given filter exists
     * 
     * @param name
     * @return
     * @throws java.io.IOException
     */
    public boolean hasFilter(String name) throws IOException;

    /**
     * Load the given filter exists
     * 
     * @param name
     * @return
     * @throws java.io.IOException
     */
    public BloomFilter<String> loadFilter(String name) throws IOException;

    /**
     * Save the given filter into this provider
     *
     * @param name
     * @param filter
     * @throws java.io.IOException
     */
    public void saveFilter(String name, BloomFilter<String> filter) throws IOException;
}