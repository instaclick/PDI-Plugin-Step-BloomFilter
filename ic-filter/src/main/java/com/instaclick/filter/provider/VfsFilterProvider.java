package com.instaclick.filter.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.commons.vfs.FileObject;
import org.pentaho.di.core.exception.KettleFileException;
import org.pentaho.di.core.vfs.KettleVFS;

/**
 * KettleVFS provider
 *
 * @author Fabio B. Silva <fabio.bat.silva@gmail.com>
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
    public VfsFilterProvider(final String uri)
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
    private FileObject getFile(final String name) throws IOException
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
    public boolean hasFilter(final String name) throws IOException
    {
        return getFile(name).exists();
    }

    /**
     * {@inheritDoc}
     */
    public Serializable loadFilter(final String name) throws IOException
    {
        final FileObject file                     = getFile(name);
        final InputStream fileInputStream         = file.getContent().getInputStream();
        final ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

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
    public void saveFilter(final String name, final Serializable filter) throws IOException
    {
        final FileObject folder = getFile();
        final FileObject file   = getFile(name);

        if ( ! folder.exists()) {
            folder.createFolder();
        }

        if ( ! file.exists()) {
            file.createFile();
        }

        final OutputStream fileOutputStream           = file.getContent().getOutputStream();
        final ObjectOutputStream objectOutputStream   = new ObjectOutputStream(fileOutputStream);

        objectOutputStream.writeObject(filter);
        objectOutputStream.close();
        fileOutputStream.close();
        file.close();
    }

    /**
     * {@inheritDoc}
     */
    public void moveFilter(final String source, final String target) throws IOException
    {
        final FileObject sourceFile = getFile(source);
        final FileObject targetFile = getFile(target);
        
        sourceFile.moveTo(targetFile);
    }
}
