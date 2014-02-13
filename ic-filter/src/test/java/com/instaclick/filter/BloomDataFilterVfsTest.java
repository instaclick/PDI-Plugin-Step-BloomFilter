package com.instaclick.filter;

import com.google.common.hash.Hashing;
import com.instaclick.filter.provider.FilterProvider;
import com.instaclick.filter.provider.VfsFilterProvider;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;
import org.junit.Before;
import org.pentaho.di.core.exception.KettleFileException;

public class BloomDataFilterVfsTest extends BaseBloomFilterTest
{
    FilterProvider provider = null;
    FileObject folder = null;

    public BloomDataFilterVfsTest() throws FileSystemException, KettleFileException
    {
        folder   = VFS.getManager().resolveFile(getParameter("provider.uri.vfs", "tmp://ic-filter/") + System.currentTimeMillis());
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
