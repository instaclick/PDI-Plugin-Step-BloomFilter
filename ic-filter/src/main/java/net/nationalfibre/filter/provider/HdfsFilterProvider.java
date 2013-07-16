package net.nationalfibre.filter.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.skjegstad.utils.BloomFilter;

public class HdfsFilterProvider implements FilterProvider {

    FileSystem hdfs     = null;
    String dir          = null;
    Configuration conf  = new Configuration();

    public HdfsFilterProvider(String dir) {
        this.dir = dir;

        conf.set("fs.default.name", dir);
    }

    private FileSystem getHdfs() throws IOException {

        if (hdfs == null) {
            hdfs = FileSystem.get(conf);
        }

        return hdfs;
    }

    private Path getPath() {
        return new Path(this.dir);
    }

    private Path getPath(String name) {
        return new Path(this.getPath().toString() + "/" + name);
    }

    @Override
    public boolean hasFilter(String name) throws IOException {
        return getHdfs().exists(getPath(name));
    }

    @Override
    public BloomFilter<String> loadFilter(String name) throws IOException {

        Path file                           = getPath(name);
        InputStream fileInputStream         = getHdfs().open(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        try {
            @SuppressWarnings("unchecked")
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

        FileSystem hdfs = getHdfs();
        Path folder     = getPath();
        Path file       = getPath(name);

        if ( ! hdfs.exists(folder)) {
            hdfs.mkdirs(folder);
        }

        OutputStream fileOutputStream           = hdfs.create(file, true);
        ObjectOutputStream objectOutputStream   = new ObjectOutputStream(fileOutputStream);

        objectOutputStream.writeObject(filter);
        objectOutputStream.close();
    }
}
