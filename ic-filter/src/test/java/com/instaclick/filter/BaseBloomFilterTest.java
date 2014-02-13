package com.instaclick.filter;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.assertEquals;

import static junit.framework.Assert.assertTrue;

import org.junit.Test;

public abstract class BaseBloomFilterTest extends BaseFilterTest
{
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