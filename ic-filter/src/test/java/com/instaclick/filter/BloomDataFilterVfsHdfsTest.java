package com.instaclick.filter;

import com.google.common.hash.Hashing;
import com.instaclick.filter.provider.FilterProvider;
import com.instaclick.filter.provider.VfsFilterProvider;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.junit.Before;
import org.junit.Ignore;
import org.pentaho.di.core.exception.KettleFileException;
import org.pentaho.di.core.vfs.KettleVFS;

@Ignore
public class BloomDataFilterVfsHdfsTest extends BaseBloomFilterTest
{
    FileObject folder;
    FilterProvider provider;

    public BloomDataFilterVfsHdfsTest() throws FileSystemException, KettleFileException
    {
        folder   = KettleVFS.getFileObject(getParameter("provider.uri.hdfs", "hdfs://bi-hadoopnamednode01.ss:8020/dev-bloomfilters"));
        provider = new VfsFilterProvider(folder.getURL().toString());
    }

    @Before
    public void setUp() throws FileSystemException
    {
        if (folder != null && folder.exists()) {
            for (FileObject file : folder.getChildren()) {
                file.delete();
            }
        }
    }

    @Override
    protected DataFilter getFilter()
    {
        return new BloomDataFilter(config, provider, Hashing.murmur3_128());
    }
}
