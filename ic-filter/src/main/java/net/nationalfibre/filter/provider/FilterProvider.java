package net.nationalfibre.filter.provider;

import java.io.IOException;

import com.skjegstad.utils.BloomFilter;

public interface FilterProvider {

    public boolean hasFilter(String name) throws IOException;

    public BloomFilter<String> loadFilter(String name) throws IOException;

    public void saveFilter(String name, BloomFilter<String> filter) throws IOException;
}