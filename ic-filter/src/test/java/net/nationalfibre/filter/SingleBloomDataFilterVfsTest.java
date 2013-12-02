package net.nationalfibre.filter;

import com.google.common.hash.Hashing;
import net.nationalfibre.filter.provider.FilterProvider;
import net.nationalfibre.filter.provider.VfsFilterProvider;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;
import org.junit.Before;
import org.pentaho.di.core.exception.KettleFileException;

public class SingleBloomDataFilterVfsTest extends BaseFilterTest {

    FilterProvider provider = null;
    FileObject folder = null;

    public SingleBloomDataFilterVfsTest() throws FileSystemException, KettleFileException
    {
        folder   = VFS.getManager().resolveFile(getParameter("provider.uri.vfs", "tmp://ic-filter/") + System.currentTimeMillis());
        provider = new VfsFilterProvider(folder.getURL().toString());

        config.withFilterFileName("single_filter_" + System.currentTimeMillis());
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
        return new SingleBloomDataFilter(config, provider, Hashing.murmur3_128());
    }
}
