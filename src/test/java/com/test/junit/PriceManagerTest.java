package com.test.junit;

import com.test.PriceManager;
import com.test.model.Price;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RunWith(JUnit4.class)
public class PriceManagerTest {

    final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @Test
    public void test1() throws ParseException {
        Price price1 = new Price(1L, "c1", 0, 1, sdf.parse("01.01.2013 00:00:00"), sdf.parse("31.01.2013 00:00:00"), 5000);
        Price price2 = new Price(2L, "c1", 0, 1, sdf.parse("12.01.2013 00:00:00"), sdf.parse("13.01.2013 00:00:00"), 4000);

        List<Price> list1 = new ArrayList<Price>();
        List<Price> list2 = new ArrayList<Price>();
        list1.add(price1);
        list2.add(price2);

        PriceManager priceManager = new PriceManager();
        List<Price> merging = priceManager.priceMerge(list1, list2);

        Assert.assertEquals(3, merging.size());
        Assert.assertEquals(5000, merging.get(0).getValue());
        Assert.assertEquals(4000, merging.get(1).getValue());
        Assert.assertEquals(5000, merging.get(2).getValue());

        Assert.assertEquals(sdf.parse("01.01.2013 00:00:00"), merging.get(0).getBegin());
        Assert.assertEquals(sdf.parse("12.01.2013 00:00:00"), merging.get(0).getEnd());

        Assert.assertEquals(sdf.parse("12.01.2013 00:00:00"), merging.get(1).getBegin());
        Assert.assertEquals(sdf.parse("13.01.2013 00:00:00"), merging.get(1).getEnd());

        Assert.assertEquals(sdf.parse("13.01.2013 00:00:00"), merging.get(2).getBegin());
        Assert.assertEquals(sdf.parse("31.01.2013 00:00:00"), merging.get(2).getEnd());
    }

    @Test
    public void test2() throws ParseException {
        Price price1 = new Price(1L, "c1", 0, 1, sdf.parse("01.01.2013 00:00:00"), sdf.parse("31.01.2013 23:59:59"), 11000);
        Price price2 = new Price(2L, "c1", 0, 1, sdf.parse("20.01.2013 00:00:00"), sdf.parse("20.02.2013 23:59:59"), 11000);

        List<Price> list1 = new ArrayList<Price>();
        List<Price> list2 = new ArrayList<Price>();
        list1.add(price1);
        list2.add(price2);

        PriceManager priceManager = new PriceManager();
        List<Price> merging = priceManager.priceMerge(list1, list2);

        Assert.assertEquals(1, merging.size());
        Assert.assertEquals(11000, merging.get(0).getValue());

        Assert.assertEquals(sdf.parse("01.01.2013 00:00:00"), merging.get(0).getBegin());
        Assert.assertEquals(sdf.parse("20.02.2013 23:59:59"), merging.get(0).getEnd());
    }

    @Test
    public void test3() throws ParseException {
        Price price11 = new Price(1L, "c1", 0, 1, sdf.parse("01.01.2013 00:00:00"), sdf.parse("10.01.2013 00:00:00"), 100);
        Price price12 = new Price(2L, "c1", 0, 1, sdf.parse("10.01.2013 00:00:00"), sdf.parse("20.01.2013 00:00:00"), 110);
        Price price13 = new Price(3L, "c1", 0, 1, sdf.parse("20.01.2013 00:00:00"), sdf.parse("31.01.2013 00:00:00"), 120);

        Price price21 = new Price(4L, "c1", 0, 1, sdf.parse("08.01.2013 00:00:00"), sdf.parse("15.01.2013 00:00:00"), 80);
        Price price22 = new Price(5L, "c1", 0, 1, sdf.parse("15.01.2013 00:00:00"), sdf.parse("25.01.2013 00:00:00"), 85);

        List<Price> list1 = new ArrayList<Price>();
        List<Price> list2 = new ArrayList<Price>();
        list1.add(price11);
        list1.add(price12);
        list1.add(price13);

        list2.add(price21);
        list2.add(price22);

        PriceManager priceManager = new PriceManager();
        List<Price> mergedPrices = priceManager.priceMerge(list1, list2);

        Collections.sort(mergedPrices, new Comparator<Price>() {
            public int compare(Price o1, Price o2) {
                return o1.getBegin().compareTo(o2.getBegin());
            }
        });

        Assert.assertEquals(4, mergedPrices.size());

        Assert.assertEquals(sdf.parse("01.01.2013 00:00:00"), mergedPrices.get(0).getBegin());
        Assert.assertEquals(sdf.parse("08.01.2013 00:00:00"), mergedPrices.get(0).getEnd());
        Assert.assertEquals(100, mergedPrices.get(0).getValue());

        Assert.assertEquals(sdf.parse("08.01.2013 00:00:00"), mergedPrices.get(1).getBegin());
        Assert.assertEquals(sdf.parse("15.01.2013 00:00:00"), mergedPrices.get(1).getEnd());
        Assert.assertEquals(80, mergedPrices.get(1).getValue());

        Assert.assertEquals(sdf.parse("15.01.2013 00:00:00"), mergedPrices.get(2).getBegin());
        Assert.assertEquals(sdf.parse("25.01.2013 00:00:00"), mergedPrices.get(2).getEnd());
        Assert.assertEquals(85, mergedPrices.get(2).getValue());

        Assert.assertEquals(sdf.parse("25.01.2013 00:00:00"), mergedPrices.get(3).getBegin());
        Assert.assertEquals(sdf.parse("31.01.2013 00:00:00"), mergedPrices.get(3).getEnd());
        Assert.assertEquals(120, mergedPrices.get(3).getValue());
    }

}