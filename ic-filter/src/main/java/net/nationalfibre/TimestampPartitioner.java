package net.nationalfibre;

import java.util.Calendar;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Partitioner;

/**
 * Custom hadoop partitioner
 *
 * @author Fabio B. Silva <fabios@nationalfibre.net>
 */
public class TimestampPartitioner implements Partitioner<Text, Text>
{
    /**
     * Calendar instance
     */
    Calendar calendar  = Calendar.getInstance();

    /**
     * Timestamp field index
     */
    int timestampIndex = 0;

    /**
     * {@inheritDoc}
     */
    public void configure(JobConf job) 
    {

    }

    /**
     * {@inheritDoc}
     */
    public int getPartition(Text key, Text value, int numReduceTasks)
    {
        String keyString = key.toString();
        String[] splits  = keyString.split("_");
        int timestamp    = Integer.parseInt(splits[timestampIndex]);

        calendar.setTimeInMillis(timestamp * 1000);

        //Return the year mod number of reduce tasks as the partitioner number to send the record to.
        return calendar.get(Calendar.MINUTE) % numReduceTasks;
    }
}