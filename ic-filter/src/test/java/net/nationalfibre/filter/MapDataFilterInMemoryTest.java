package net.nationalfibre.filter;

import net.nationalfibre.filter.provider.FilterProvider;
import net.nationalfibre.filter.provider.InMemoryFilterProvider;

public class MapDataFilterInMemoryTest extends BaseFilterTest {

    FilterProvider provider = new InMemoryFilterProvider();

    @Override
    protected DataFilter getFilter()
    {
        return new MapDataFilter(config, provider);
    }
}
