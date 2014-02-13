package com.instaclick.filter.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * Hadoop HDFS provider
 *
 * @author Fabio B. Silva <fabio.bat.silva@gmail.com>
 */
public class HdfsFilterProvider implements FilterProvider
{
    /**
     * Hadoop file system
     */
    private FileSystem hdfs = null;

    /**
     * Base directory
     */
    private String dir = null;

    /**
     * Hadoop configuration
     */
    private Configuration conf = new Configuration();

    /**
     * @param dir Base directory
     */
    public HdfsFilterProvider(String dir)
    {
        this.dir = dir;

        conf.set("fs.default.name", dir);
    }

    /**
     * Retrieve the base {@link FileSystem}
     *
     * @return
     * @throws IOException
     */
    private FileSystem getHdfs() throws IOException
    {
        if (hdfs == null) {
            hdfs = FileSystem.get(conf);
        }

        return hdfs;
    }

    /**
     * Retrieve the base {@link Path}
     *
     * @return
     */
    private Path getPath()
    {
        return new Path(this.dir);
    }

    /**
     * Retrieve the file {@link Path}
     *
     * @return
     */
    private Path getPath(String name)
    {
        return new Path(this.getPath().toString() + "/" + name);
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasFilter(String name) throws IOException
    {
        return getHdfs().exists(getPath(name));
    }

    /**
     * {@inheritDoc}
     */
    public Serializable loadFilter(String name) throws IOException
    {
        Path file                           = getPath(name);
        InputStream fileInputStream         = getHdfs().open(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        try {
            Serializable filter = (Serializable) objectInputStream.readObject();

            fileInputStream.close();
            objectInputStream.close();

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
        FileSystem fs   = getHdfs();
        Path folder     = getPath();
        Path file       = getPath(name);

        if ( ! fs.exists(folder)) {
            fs.mkdirs(folder);
        }

        OutputStream fileOutputStream           = hdfs.create(file, true);
        ObjectOutputStream objectOutputStream   = new ObjectOutputStream(fileOutputStream);

        objectOutputStream.writeObject(filter);
        objectOutputStream.close();
        fileOutputStream.close();
    }
}
