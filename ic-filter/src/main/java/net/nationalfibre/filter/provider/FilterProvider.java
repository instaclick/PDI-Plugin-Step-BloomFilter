package net.nationalfibre.filter.provider;

import java.io.IOException;
import java.io.Serializable;

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
    public Serializable loadFilter(String name) throws IOException;

    /**
     * Save the given filter into this provider
     *
     * @param name
     * @param filter
     * @throws java.io.IOException
     */
    public void saveFilter(String name, Serializable filter) throws IOException;
}