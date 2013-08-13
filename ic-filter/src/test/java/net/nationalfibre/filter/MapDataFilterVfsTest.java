package net.nationalfibre.filter;

import net.nationalfibre.filter.provider.FilterProvider;
import net.nationalfibre.filter.provider.VfsFilterProvider;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;
import org.junit.Before;
import org.pentaho.di.core.exception.KettleFileException;

public class MapDataFilterVfsTest extends BaseFilterTest
{
    DataFilter filter;
    FilterProvider provider = null;
    FileObject folder = null;

    public MapDataFilterVfsTest() throws FileSystemException, KettleFileException
    {
        folder   = VFS.getManager().resolveFile(getParameter("provider.uri.vfs", "tmp://ic-filter/") + System.currentTimeMillis());
        provider = new VfsFilterProvider(folder.getURL().toString());
        filter   = new MapDataFilter(config, provider);
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
        return filter;
    }
}
