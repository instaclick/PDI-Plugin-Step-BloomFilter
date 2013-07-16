package net.nationalfibre.filter;

import net.nationalfibre.filter.provider.FilterProvider;
import net.nationalfibre.filter.provider.VfsFilterProvider;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.junit.Before;
import org.pentaho.di.core.exception.KettleFileException;
import org.pentaho.di.core.vfs.KettleVFS;

public class BloomDataFilterVfsHadoopTest extends BaseFilterTest {

    DataFilter filter;
    FilterProvider provider = null;
    FileObject folder       = null;
    FilterConfig config     = new FilterConfig();

    public BloomDataFilterVfsHadoopTest() throws FileSystemException, KettleFileException {
        folder      = KettleVFS.getFileObject("hdfs://bi-hadoopnamednode01.ss:8020/dev-bloomfilters");
        provider    = new VfsFilterProvider(folder.getURL().toString());
        filter      = new BloomDataFilter(config, provider);
    }

    @Before
    public void setUp() throws FileSystemException {
        if (folder != null && folder.exists()) {
            for (FileObject file : folder.getChildren()) {
                file.delete();
            }
        }
    }

    @Override
    protected DataFilter getFilter() {
        return filter;
    }
}
