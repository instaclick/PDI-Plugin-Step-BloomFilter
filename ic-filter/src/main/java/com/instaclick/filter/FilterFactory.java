package com.instaclick.filter;

import com.google.common.hash.HashFunction;
import com.instaclick.filter.provider.FilterProvider;
import com.instaclick.filter.provider.HdfsFilterProvider;
import com.instaclick.filter.provider.InMemoryFilterProvider;
import com.instaclick.filter.provider.VfsFilterProvider;

/**
 * Factory for {@link DataFilter}
 *
 * @author Fabio B. Silva <fabio.bat.silva@gmail.com>
 */
public class FilterFactory
{
    /**
     * Creates a new {@link FilterProvider} based on the given {@link FilterConfig}
     *
     * @param config
     * @return
     */
    private static HashFunction createHashFunction(FilterConfig config)
    {
        if (config.getHashFunctionType() == HashFunctionType.NONE || config.getHashFunctionType() == null)  {
            return null;
        }

        return config.getHashFunctionType().getHashFunction();
    }

    /**
     * Creates a new {@link FilterProvider} based on the given {@link FilterConfig}
     *
     * @param config
     * @return
     */
    private static FilterProvider createProvider(FilterConfig config)
    {
        if (config.getProvider() == ProviderType.HDFS) {
            return new HdfsFilterProvider(config.getURI());
        }

        if (config.getProvider() == ProviderType.VFS) {
            return new VfsFilterProvider(config.getURI());
        }

        if (config.getProvider() == ProviderType.MEMORY) {
            return new InMemoryFilterProvider();
        }

        throw new RuntimeException("Invalid provider : " + config.getProvider());
    }

    /**
     * Creates a new {@link DataFilter} based on the given {@link FilterConfig}
     *
     * @param config
     * @return
     */
    public static DataFilter createFilter(FilterConfig config)
    {
        if (config.getFilter() == FilterType.BLOOM) {
            return new BloomDataFilter(config, createProvider(config), createHashFunction(config));
        }

        if (config.getFilter() == FilterType.MAP) {
            return new MapDataFilter(config, createProvider(config), createHashFunction(config));
        }

        if (config.getFilter() == FilterType.SINGLE_BLOOM) {
            return new SingleBloomDataFilter(config, createProvider(config), createHashFunction(config));
        }

        throw new RuntimeException("Invalid filter : " + config.getFilter());
    }
}
