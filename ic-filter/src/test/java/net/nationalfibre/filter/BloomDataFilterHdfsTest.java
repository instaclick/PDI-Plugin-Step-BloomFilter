package net.nationalfibre.filter;

import java.io.IOException;

import net.nationalfibre.filter.provider.FilterProvider;
import net.nationalfibre.filter.provider.HdfsFilterProvider;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Before;

public class BloomDataFilterHdfsTest extends BaseFilterTest
{

    DataFilter filter;
    FilterProvider provider = null;
    FileSystem hdfs         = null;
    String folder           = null;
    FilterConfig config     = new FilterConfig();
    Configuration hdfsConf  = new Configuration();

    public BloomDataFilterHdfsTest() throws IOException
    {
        folder = getParameter("provider.uri.hdfs", "hdfs://bi-hadoopnamednode01.ss:8020/dev-bloomfilters");

        hdfsConf.set("fs.default.name", folder);

        hdfs     = FileSystem.get(hdfsConf);
        provider = new HdfsFilterProvider(folder);
        filter   = new BloomDataFilter(config, provider);
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
        return filter;
    }
}
