package net.nationalfibre.filter;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.skjegstad.utils.BloomFilter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.assertEquals;

import static junit.framework.Assert.assertTrue;

import org.junit.Test;

public abstract class BaseBloomFilterTest extends BaseFilterTest {

    @Test
    public void testAddExpectedNumberOfElementsBlommFilter()
    {
        String data         = null;
        double probability  = 0.001;
        //int elements        = 100;
        int elements        = 142954;

        BloomFilter filter  = new BloomFilter<String>(probability, elements);
        int max             = elements;
        int count           = 0;

        List<Boolean> assertFalseList = new ArrayList<Boolean>();
        List<Boolean> assertTrueList  = new ArrayList<Boolean>();

        while (count < max) {
            count++;

            data = "hash_"+count;

            assertFalseList.add(filter.contains(data));
            filter.add(data);
            assertTrueList.add(filter.contains(data));
        }

        assertEquals(count, assertFalseList.size());
        assertEquals(count, assertTrueList.size());

        double expected         = elements - (elements * probability);
        int assertTrueListSize  = Collections2.filter(assertTrueList, Predicates.equalTo(true)).size();
        int assertFalseListSize = Collections2.filter(assertFalseList, Predicates.equalTo(false)).size();

        assertTrue(assertTrueListSize > expected);
        assertTrue(assertFalseListSize > expected);
    }

    @Test
    public void testAddExpectedNumberOfElements()
    {
        config.withFalsePositiveProbability(0.001)
            .withExpectedNumberOfElements(142954);

        Data data             = null;
        double probability    = config.getFalsePositiveProbability();
        int elements          = config.getExpectedNumberOfElements();
        List<Boolean> result  = new ArrayList<Boolean>();
        Long timestamp        = 1293840000000L;
        DataFilter filter     = getFilter();
        int count             = 0;

        while (count < elements) {
            count++;

            data = new Data("hash_"+count, timestamp);

            result.add(filter.add(data));
        }

        assertEquals(count, result.size());

        double expected    = elements - (elements * probability);
        int resultListSize = Collections2.filter(result, Predicates.equalTo(true)).size();

        assertTrue(resultListSize > expected);
    }
}