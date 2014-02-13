package com.instaclick.filter;

import java.io.Serializable;

/**
 * @author Fabio B. Silva <fabio.bat.silva@gmail.com>
 */
public interface FilterAdapter <T extends Serializable> extends Serializable
{
    /**
     * Adds the given {@link String}
     *
     * @param hash
     */
    public void add(String hash);

    /**
     * Check if the given {@link String} exists
     *
     * @param hash
     *
     * @return <b>TRUE</b> if the the {@link String} does not exists; <b>FALSE</b> otherwise
     */
    public boolean contains(String hash);

    /**
     * Gets the Serializable filter
     * 
     * @return
     */
    public T getFilter();

    /**
     * Sets the Serializable filter
     * 
     * @param filter
     */
    public void setFilter(T filter);
}