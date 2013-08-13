package net.nationalfibre.filter.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.commons.vfs.FileObject;
import org.pentaho.di.core.exception.KettleFileException;
import org.pentaho.di.core.vfs.KettleVFS;

import com.skjegstad.utils.BloomFilter;

/**
 * KettleVFS provider
 *
 * @author Fabio B. Silva <fabios@nationalfibre.net>
 */
public class VfsFilterProvider implements FilterProvider
{
    /**
     * Base URI
     */
    private String uri = null;

    /**
     * Base URI
     *
     * @param uri
     */
    public VfsFilterProvider(String uri)
    {
        this.uri = uri;
    }

    /**
     * Gets the base {@link FileObject} directory
     *
     * @return
     * @throws IOException
     */
    private FileObject getFile() throws IOException
    {
        try {
            return KettleVFS.getFileObject(this.uri);
        } catch (KettleFileException e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * Gets the file {@link FileObject}
     *
     * @return
     * @throws IOException
     */
    private FileObject getFile(String name) throws IOException
    {
        try {
            return KettleVFS.getFileObject(this.getFile() + "/" + name);
        } catch (KettleFileException e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasFilter(String name) throws IOException
    {
        return getFile(name).exists();
    }

    /**
     * {@inheritDoc}
     */
    public Serializable loadFilter(String name) throws IOException
    {
        FileObject file                     = getFile(name);
        InputStream fileInputStream         = file.getContent().getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        try {
            Serializable filter = (Serializable) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
            file.close();

            return filter;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void saveFilter(String name, Serializable filter) throws IOException
    {
        FileObject folder = getFile();
        FileObject file   = getFile(name);

        if ( ! folder.exists()) {
            folder.createFolder();
        }

        if ( ! file.exists()) {
            file.createFile();
        }

        OutputStream fileOutputStream           = file.getContent().getOutputStream();
        ObjectOutputStream objectOutputStream   = new ObjectOutputStream(fileOutputStream);

        objectOutputStream.writeObject(filter);
        objectOutputStream.close();
        fileOutputStream.close();
        file.close();
    }
}
