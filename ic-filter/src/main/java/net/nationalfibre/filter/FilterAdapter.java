package net.nationalfibre.filter;

import java.io.Serializable;

/**
 * @author Fabio B. Silva <fabios@nationalfibre.net>
 */
public interface FilterAdapter extends Serializable
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
}