package com.instaclick.filter;

import com.google.common.hash.Hashing;
import java.io.IOException;

import com.instaclick.filter.provider.FilterProvider;
import com.instaclick.filter.provider.HdfsFilterProvider;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Before;
import org.junit.Ignore;

@Ignore
public class BloomDataFilterHdfsTest extends BaseBloomFilterTest
{
    FilterProvider provider = null;
    FileSystem hdfs         = null;
    String folder           = null;
    Configuration hdfsConf  = new Configuration();

    public BloomDataFilterHdfsTest() throws IOException
    {
        folder = getParameter("provider.uri.hdfs", "hdfs://bi-hadoopnamednode01.ss:8020/dev-bloomfilters");

        hdfsConf.set("fs.default.name", folder);

        hdfs     = FileSystem.get(hdfsConf);
        provider = new HdfsFilterProvider(folder);
    }

    @Before
    @SuppressWarnings("deprecation")
    public void setUp() throws IOException
    {

        if (hdfs == null || ! hdfs.exists(new Path(folder))) {
            return;
        }

        for (FileStatus status : hdfs.listStatus(new Path(folder))) {
            hdfs.delete(status.getPath());
        }
    }

    @Override
    protected DataFilter getFilter()
    {
        return new BloomDataFilter(config, provider, Hashing.murmur3_128());
    }
}
