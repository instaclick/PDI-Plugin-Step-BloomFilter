package net.nationalfibre;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import static junit.framework.Assert.assertEquals;
import org.apache.hadoop.io.Text;

import org.junit.Test;

public class TimestampPartitionerTest
{
    TimestampPartitioner partitioner = new TimestampPartitioner();

    @Test
    public void testPartitioner1() throws ParseException
    {
        DateFormat df       = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Text value          = new Text("Foo");
        int numReduceTasks  = 6;

        String[] keys = new String[]{
            String.valueOf(df.parse("2001-01-01 00:00:00").getTime() / 1000),
            String.valueOf(df.parse("2001-01-01 00:01:00").getTime() / 1000),
            String.valueOf(df.parse("2001-01-01 00:02:00").getTime() / 1000),
            String.valueOf(df.parse("2001-01-01 00:03:00").getTime() / 1000),
            String.valueOf(df.parse("2001-01-01 00:04:00").getTime() / 1000),
            String.valueOf(df.parse("2001-01-01 00:05:00").getTime() / 1000),
            String.valueOf(df.parse("2001-01-01 00:06:00").getTime() / 1000),
            String.valueOf(df.parse("2001-01-01 00:07:00").getTime() / 1000),
            String.valueOf(df.parse("2001-01-01 00:08:00").getTime() / 1000),
        };

        assertEquals(0, partitioner.getPartition(new Text(keys[0] + "_foo_bar"), value, numReduceTasks));
        assertEquals(1, partitioner.getPartition(new Text(keys[1] + "_foo_bar"), value, numReduceTasks));
        assertEquals(2, partitioner.getPartition(new Text(keys[2] + "_foo_bar"), value, numReduceTasks));
        assertEquals(3, partitioner.getPartition(new Text(keys[3] + "_foo_bar"), value, numReduceTasks));
        assertEquals(4, partitioner.getPartition(new Text(keys[4] + "_foo_bar"), value, numReduceTasks));
        assertEquals(5, partitioner.getPartition(new Text(keys[5] + "_foo_bar"), value, numReduceTasks));
        assertEquals(0, partitioner.getPartition(new Text(keys[6] + "_foo_bar"), value, numReduceTasks));
        assertEquals(1, partitioner.getPartition(new Text(keys[7] + "_foo_bar"), value, numReduceTasks));
    }

    @Test
    public void testPartitioner2() throws ParseException
    {
        DateFormat df       = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Text value          = new Text("Foo");
        int numReduceTasks  = 6;

        String[] keys = new String[]{
            "1375329660",
            "1375329720",
            "1375329780",
            "1375329840",
            "1375329900",
            "1375329960"
        };

        assertEquals(2, partitioner.getPartition(new Text(keys[0] + "_foo_bar"), value, numReduceTasks));
        assertEquals(3, partitioner.getPartition(new Text(keys[1] + "_foo_bar"), value, numReduceTasks));
        assertEquals(4, partitioner.getPartition(new Text(keys[2] + "_foo_bar"), value, numReduceTasks));
        assertEquals(5, partitioner.getPartition(new Text(keys[3] + "_foo_bar"), value, numReduceTasks));
        assertEquals(0, partitioner.getPartition(new Text(keys[4] + "_foo_bar"), value, numReduceTasks));
        assertEquals(1, partitioner.getPartition(new Text(keys[5] + "_foo_bar"), value, numReduceTasks));
    }
}
