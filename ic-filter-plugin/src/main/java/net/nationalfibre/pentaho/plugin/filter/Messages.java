package net.nationalfibre.pentaho.plugin.filter;

import org.pentaho.di.i18n.BaseMessages;

/**
 * Pentaho filter messages
 *
 * @author Fabio B. Silva <fabios@nationalfibre.net>
 */
public class Messages
{
    public static final Class<Messages> PKG = Messages.class;

    /**
     * {@inheritDoc}
     */
    public static String getString(String key)
    {
        return BaseMessages.getString(PKG, key);
    }

    /**
     * {@inheritDoc}
     */
    public static String getString(String key, String param1)
    {
        return BaseMessages.getString(PKG, key, param1);
    }

    /**
     * {@inheritDoc}
     */
    public static String getString(String key, String param1, String param2)
    {
        return BaseMessages.getString(PKG, key, param1, param2);
    }

    /**
     * {@inheritDoc}
     */
    public static String getString(String key, String param1, String param2, String param3)
    {
        return BaseMessages.getString(PKG, key, param1, param2, param3);
    }

    /**
     * {@inheritDoc}
     */
    public static String getString(String key, String param1, String param2, String param3, String param4)
    {
        return BaseMessages.getString(PKG, key, param1, param2, param3, param4);
    }

    /**
     * {@inheritDoc}
     */
    public static String getString(String key, String param1, String param2, String param3, String param4, String param5)
    {
        return BaseMessages.getString(PKG, key, param1, param2, param3, param4, param5);
    }

    /**
     * {@inheritDoc}
     */
    public static String getString(String key, String param1, String param2, String param3, String param4, String param5, String param6)
    {
        return BaseMessages.getString(PKG, key, param1, param2, param3, param4, param5, param6);
    }
}