package net.nationalfibre.filter;

import com.google.common.hash.Hashing;
import net.nationalfibre.filter.provider.FilterProvider;
import net.nationalfibre.filter.provider.InMemoryFilterProvider;

public class BloomDataFilterInMemoryTest extends BaseBloomFilterTest {

    DataFilter filter;
    FilterProvider provider = new InMemoryFilterProvider();

    @Override
    protected DataFilter getFilter()
    {
        return new BloomDataFilter(config, provider, Hashing.murmur3_128());
    }
}
