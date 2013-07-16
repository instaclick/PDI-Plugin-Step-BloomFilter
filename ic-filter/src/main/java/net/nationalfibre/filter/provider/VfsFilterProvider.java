package net.nationalfibre.filter.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.apache.commons.vfs.FileObject;
import org.pentaho.di.core.exception.KettleFileException;
import org.pentaho.di.core.vfs.KettleVFS;

import com.skjegstad.utils.BloomFilter;

public class VfsFilterProvider implements FilterProvider {

    private String dir = null;

    public VfsFilterProvider(String dir) {
        this.dir = dir;
    }

    private FileObject getFile() throws IOException {
        try {
            return KettleVFS.getFileObject(this.dir);
        } catch (KettleFileException e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    private FileObject getFile(String name) throws IOException {
        try {
            return KettleVFS.getFileObject(this.getFile() + "/" + name);
        } catch (KettleFileException e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    @Override
    public boolean hasFilter(String name) throws IOException {
        return getFile(name).exists();
    }

    @Override
    @SuppressWarnings("unchecked")
    public BloomFilter<String> loadFilter(String name) throws IOException {

        FileObject file                     = getFile(name);
        InputStream fileInputStream         = file.getContent().getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        try {
            BloomFilter<String> filter = (BloomFilter<String>) objectInputStream.readObject();

            fileInputStream.close();
            objectInputStream.close();

            return filter;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void saveFilter(String name, BloomFilter<String> filter) throws IOException {

        FileObject folder   = getFile();
        FileObject file     = getFile(name);

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
    }
}
