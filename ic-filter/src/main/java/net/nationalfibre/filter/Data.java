package net.nationalfibre.filter;

import java.util.Date;

/**
 * Represent the data thats if been filtered
 *
 * @author Fabio B. Silva <fabios@nationalfibre.net>
 */
public class Data
{
    /**
     * Data unique value
     */
    private String hash;

    /**
     * epoch timestamp
     */
    private Long timestamp;

    /**
     * @param hash
     * @param date
     */
    public Data(String hash, Date date)
    {
        this.hash       = hash;
        this.timestamp  = (date.getTime() / 1000);
    }

    /**
     * @param hash
     * @param timestamp
     */
    public Data(String hash, Long timestamp)
    {
        this.hash       = hash;
        this.timestamp  = timestamp;
    }

    /**
     * @return
     */
    public String getHash()
    {
        return hash;
    }

    /**
     * @return
     */
    public Long getTimestamp()
    {
        return timestamp;
    }
}
