package net.nationalfibre.pentaho.plugin.filter;

/**
 * Base filter exception
 *
 * @author Fabio B. Silva <fabios@nationalfibre.net>
 */
public class FilterException extends RuntimeException
{
    private static final long serialVersionUID = 392771364876785298L;

    /**
     * Exception message
     *
     * @param message
     */
    public FilterException(String message)
    {
        super(message);
    }
}
