package net.nationalfibre.filter;

import net.nationalfibre.filter.provider.FilterProvider;
import net.nationalfibre.filter.provider.HdfsFilterProvider;
import net.nationalfibre.filter.provider.InMemoryFilterProvider;
import net.nationalfibre.filter.provider.VfsFilterProvider;

public class FilterFactory {

	private static FilterProvider createProvider(FilterConfig config) {

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

	public static DataFilter createFilter(FilterConfig config) {

		if (config.getFilter() == FilterType.BLOMM) {
            return new BloomDataFilter(config, createProvider(config));
		}

		throw new RuntimeException("Invalid filter : " + config.getFilter());
	}
}
