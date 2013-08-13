package net.nationalfibre.filter;

import net.nationalfibre.filter.provider.FilterProvider;
import net.nationalfibre.filter.provider.InMemoryFilterProvider;

public class MapDataFilterInMemoryTest extends BaseFilterTest {

    DataFilter filter;
    FilterProvider provider = new InMemoryFilterProvider();

    public MapDataFilterInMemoryTest() {
        filter = new MapDataFilter(config, provider);
    }

    @Override
    protected DataFilter getFilter() {
        return filter;
    }
}
