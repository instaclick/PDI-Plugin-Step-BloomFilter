package com.instaclick.filter;

/**
 * Defines a behavior that should be implement by all filter
 *
 * @author Fabio B. Silva <fabio.bat.silva@gmail.com>
 */
public interface DataFilter
{
    /**
     * Adds the given {@link Data} if it does not exists
     *
     * @param data
     *
     * @return <b>TRUE</b> if the the {@link Data} does not exists; <b>FALSE</b> otherwise
     */
    public boolean add(Data data);

    /**
     * Check if the given {@link Data} exists
     *
     * @param data
     *
     * @return <b>TRUE</b> if the the {@link Data} does not exists; <b>FALSE</b> otherwise
     */
    public boolean contains(Data data);

    /**
     * Flushes the filter data, this operation should be invoked at the end of the filter
     */
    public void flush();
}