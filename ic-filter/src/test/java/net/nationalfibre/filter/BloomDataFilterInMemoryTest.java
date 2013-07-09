package net.nationalfibre.filter;

import net.nationalfibre.filter.provider.FilterProvider;
import net.nationalfibre.filter.provider.InMemoryFilterProvider;

public class BloomDataFilterInMemoryTest  extends BaseFilterTest {

	DataFilter filter;
	FilterConfig config 	= new FilterConfig();
	FilterProvider provider = new InMemoryFilterProvider();

	public BloomDataFilterInMemoryTest() {
		filter = new BloomDataFilter(config, provider);
	}

	@Override
	protected DataFilter getFilter() {
		return filter;
	}
}
