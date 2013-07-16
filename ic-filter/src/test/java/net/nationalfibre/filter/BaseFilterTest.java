package net.nationalfibre.filter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import junit.framework.Assert;

import org.junit.Test;

public abstract class BaseFilterTest {

    protected abstract DataFilter getFilter();

    @Test
    public void testAddAndContains() throws ParseException {
        DataFilter filter   = getFilter();
        DateFormat df       = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Data d1             = new Data("1", df.parse("2001-11-12 11:22:31"));
        Data d2             = new Data("1", df.parse("2001-11-12 11:22:32"));

        Assert.assertFalse(filter.contains(d1));
        Assert.assertFalse(filter.contains(d2));

        Assert.assertTrue(filter.add(d1));
        Assert.assertFalse(filter.add(d2));

        Assert.assertTrue(filter.contains(d1));
        Assert.assertTrue(filter.contains(d2));
    }

    @Test
    public void testContainsAfterFlush() throws ParseException {
        DataFilter filter   = getFilter();
        DateFormat df       = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Data d1             = new Data("1", df.parse("2001-11-12 11:22:31"));
        Data d2             = new Data("1", df.parse("2001-11-12 11:22:32"));

        Assert.assertFalse(filter.contains(d1));
        Assert.assertFalse(filter.contains(d2));

        Assert.assertTrue(filter.add(d1));

        filter.flush();

        Assert.assertTrue(filter.contains(d1));
        Assert.assertTrue(filter.contains(d2));
    }

    @Test
    public void testAddAfterFlush() throws ParseException {
        DataFilter filter   = getFilter();
        DateFormat df       = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Data d1             = new Data("1", df.parse("2001-11-12 11:22:33"));
        Data d2             = new Data("1", df.parse("2001-11-12 11:22:34"));
        Data d3             = new Data("1", df.parse("2001-11-12 11:22:33"));
        Data d4             = new Data("1", df.parse("2001-11-12 11:22:34"));

        Assert.assertFalse(filter.contains(d1));
        Assert.assertFalse(filter.contains(d2));
        Assert.assertFalse(filter.contains(d3));
        Assert.assertFalse(filter.contains(d4));

        Assert.assertTrue(filter.add(d1));
        Assert.assertFalse(filter.add(d2));

        filter.flush();

        Assert.assertFalse(filter.add(d3));
        Assert.assertFalse(filter.add(d4));

        Assert.assertTrue(filter.contains(d1));
        Assert.assertTrue(filter.contains(d2));
        Assert.assertTrue(filter.contains(d3));
        Assert.assertTrue(filter.contains(d4));
    }

    @Test
    public void testProviderLookup() throws ParseException {
        DataFilter filter   = getFilter();
        DateFormat df       = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Data d1             = new Data("1", df.parse("2001-11-12 00:00:00"));
        Data d2             = new Data("1", df.parse("2001-11-12 00:01:00"));
        Data d3             = new Data("1", df.parse("2001-11-12 00:59:00"));
        Data d4             = new Data("1", df.parse("2001-11-12 12:02:00"));

        Assert.assertFalse(filter.contains(d1));
        Assert.assertFalse(filter.contains(d2));
        Assert.assertFalse(filter.contains(d3));
        Assert.assertFalse(filter.contains(d4));

        Assert.assertTrue(filter.add(d1));
        Assert.assertFalse(filter.add(d2));

        filter.flush();

        Assert.assertFalse(filter.add(d3));
        Assert.assertFalse(filter.add(d4));

        Assert.assertTrue(filter.contains(d1));
        Assert.assertTrue(filter.contains(d2));
        Assert.assertTrue(filter.contains(d3));
        Assert.assertTrue(filter.contains(d4));
    }

    @Test
    public void testShoudNotAddDuringTheSameHour() {
        DataFilter filter   = getFilter();
        Long startTime      = 1293861600L;
        Long finalTime      = startTime + 60;
        Data data           = new Data("1", startTime);

        Assert.assertFalse(filter.contains(data));
        Assert.assertTrue(filter.add(data));
        Assert.assertTrue(filter.contains(data));

        while (startTime < finalTime) {
            Assert.assertFalse(filter.add(new Data("1", startTime++)));
        }
    }
}
