package net.nationalfibre.filter;

import net.nationalfibre.filter.provider.FilterProvider;
import net.nationalfibre.filter.provider.InMemoryFilterProvider;

public class BloomDataFilterInMemoryTest extends BaseBloomFilterTest {

    DataFilter filter;
    FilterProvider provider = new InMemoryFilterProvider();

    @Override
    protected DataFilter getFilter()
    {
        return new BloomDataFilter(config, provider);
    }
}
