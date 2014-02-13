package com.instaclick.filter;

import com.google.common.hash.Hashing;
import com.instaclick.filter.provider.FilterProvider;
import com.instaclick.filter.provider.InMemoryFilterProvider;

public class MapDataFilterInMemoryTest extends BaseFilterTest {

    FilterProvider provider = new InMemoryFilterProvider();

    @Override
    protected DataFilter getFilter()
    {
        return new MapDataFilter(config, provider, Hashing.murmur3_128());
    }
}
