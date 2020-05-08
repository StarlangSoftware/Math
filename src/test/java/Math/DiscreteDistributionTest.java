package Math;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class DiscreteDistributionTest {
    DiscreteDistribution smallDistribution;

    @Before
    public void setUp(){
        smallDistribution = new DiscreteDistribution();
        smallDistribution.addItem("item1");
        smallDistribution.addItem("item2");
        smallDistribution.addItem("item3");
        smallDistribution.addItem("item1");
        smallDistribution.addItem("item2");
        smallDistribution.addItem("item1");
    }

    @Test
    public void testAddItem1() {
        assertEquals(3, (int) smallDistribution.getCount("item1"));
        assertEquals(2, (int) smallDistribution.getCount("item2"));
        assertEquals(1, (int) smallDistribution.getCount("item3"));
    }

    @Test
    public void testAddItem2() {
        Random random = new Random();
        DiscreteDistribution discreteDistribution = new DiscreteDistribution();
        for (int i = 0; i < 1000; i++){
            discreteDistribution.addItem("" + random.nextInt(1000));
        }
        int count = 0;
        for (int i = 0; i < 1000; i++){
            if (discreteDistribution.containsItem("" + i)){
                count += discreteDistribution.getCount("" + i);
            }
        }
        assertEquals(1000, count);
    }

    @Test
    public void testAddItem3() {
        Random random = new Random();
        DiscreteDistribution discreteDistribution = new DiscreteDistribution();
        for (int i = 0; i < 1000; i++){
            discreteDistribution.addItem("" + random.nextInt(1000));
        }
        for (int i = 0; i < 1000000; i++){
            discreteDistribution.addItem("" + random.nextInt(1000000));
        }
        assertEquals(discreteDistribution.size() / 1000000.0, 0.632, 0.001);
    }

    @Test
    public void testRemoveItem() {
        smallDistribution.removeItem("item1");
        smallDistribution.removeItem("item2");
        smallDistribution.removeItem("item3");
        assertEquals(2, (int) smallDistribution.getCount("item1"));
        assertEquals(1, (int) smallDistribution.getCount("item2"));
        smallDistribution.addItem("item1");
        smallDistribution.addItem("item2");
        smallDistribution.addItem("item3");
    }

    @Test
    public void testAddDistribution1() {
        DiscreteDistribution discreteDistribution = new DiscreteDistribution();
        discreteDistribution.addItem("item4");
        discreteDistribution.addItem("item5");
        discreteDistribution.addItem("item5");
        discreteDistribution.addItem("item2");
        smallDistribution.addDistribution(discreteDistribution);
        assertEquals(3, (int) smallDistribution.getCount("item1"));
        assertEquals(3, (int) smallDistribution.getCount("item2"));
        assertEquals(1, (int) smallDistribution.getCount("item3"));
        assertEquals(1, (int) smallDistribution.getCount("item4"));
        assertEquals(2, (int) smallDistribution.getCount("item5"));
        smallDistribution.removeDistribution(discreteDistribution);
    }

    @Test
    public void testAddDistribution2(){
        DiscreteDistribution discreteDistribution1 = new DiscreteDistribution();
        for (int i = 0; i < 1000; i++){
            discreteDistribution1.addItem("" + i);
        }
        DiscreteDistribution discreteDistribution2 = new DiscreteDistribution();
        for (int i = 500; i < 1000; i++){
            discreteDistribution2.addItem("" + (1000 + i));
        }
        discreteDistribution1.addDistribution(discreteDistribution2);
        assertEquals(1500, discreteDistribution1.size());
    }

    @Test
    public void testRemoveDistribution() {
        DiscreteDistribution discreteDistribution = new DiscreteDistribution();
        discreteDistribution.addItem("item1");
        discreteDistribution.addItem("item1");
        discreteDistribution.addItem("item2");
        smallDistribution.removeDistribution(discreteDistribution);
        assertEquals(1, (int) smallDistribution.getCount("item1"));
        assertEquals(1, (int) smallDistribution.getCount("item2"));
        assertEquals(1, (int) smallDistribution.getCount("item3"));
        smallDistribution.addDistribution(discreteDistribution);
    }

    @Test
    public void testGetSum1() {
        assertEquals(6, smallDistribution.getSum(), 0.0);
    }

    @Test
    public void testGetSum2() {
        Random random = new Random();
        DiscreteDistribution discreteDistribution = new DiscreteDistribution();
        for (int i = 0; i < 1000; i++){
            discreteDistribution.addItem("" + random.nextInt(1000));
        }
        assertEquals(1000, discreteDistribution.getSum(), 0.0);
    }

    @Test
    public void testGetIndex() {
        assertEquals(0, smallDistribution.getIndex("item1"));
        assertEquals(1, smallDistribution.getIndex("item2"));
        assertEquals(2, smallDistribution.getIndex("item3"));
    }

    @Test
    public void testContainsItem() {
        assertTrue(smallDistribution.containsItem("item1"));
        assertFalse(smallDistribution.containsItem("item4"));
    }

    @Test
    public void testGetItem() {
        assertEquals("item1", smallDistribution.getItem(0));
        assertEquals("item2", smallDistribution.getItem(1));
        assertEquals("item3", smallDistribution.getItem(2));
    }

    @Test
    public void testGetValue() {
        assertEquals(3, (int) smallDistribution.getValue(0));
        assertEquals(2, (int) smallDistribution.getValue(1));
        assertEquals(1, (int) smallDistribution.getValue(2));
    }

    @Test
    public void testGetCount() {
        assertEquals(3, (int) smallDistribution.getCount("item1"));
        assertEquals(2, (int) smallDistribution.getCount("item2"));
        assertEquals(1, (int) smallDistribution.getCount("item3"));
    }

    @Test
    public void testGetMaxItem1() {
        assertEquals("item1", smallDistribution.getMaxItem());
    }

    @Test
    public void testGetMaxItem2() {
        ArrayList include = new ArrayList();
        include.add("item2");
        include.add("item3");
        assertEquals("item2", smallDistribution.getMaxItem(include));
    }

    @Test
    public void testGetProbability1() {
        Random random = new Random();
        DiscreteDistribution discreteDistribution = new DiscreteDistribution();
        for (int i = 0; i < 1000; i++){
            discreteDistribution.addItem("" + i);
        }
        assertEquals(0.001, discreteDistribution.getProbability("" + random.nextInt(1000)), 0.0);
    }

    @Test
    public void testGetProbability2() {
        assertEquals(0.5, smallDistribution.getProbability("item1"), 0.0);
        assertEquals(0.333333, smallDistribution.getProbability("item2"), 0.0001);
        assertEquals(0.166667, smallDistribution.getProbability("item3"), 0.0001);
    }

    @Test
    public void getProbabilityLaplaceSmoothing1() {
        Random random = new Random();
        DiscreteDistribution discreteDistribution = new DiscreteDistribution();
        for (int i = 0; i < 1000; i++){
            discreteDistribution.addItem("" + i);
        }
        assertEquals(2.0 / 2001, discreteDistribution.getProbabilityLaplaceSmoothing("" + random.nextInt(1000)), 0.0);
        assertEquals(1.0 / 2001, discreteDistribution.getProbabilityLaplaceSmoothing("item0"), 0.0);
    }

    @Test
    public void getProbabilityLaplaceSmoothing2() {
        assertEquals(0.4, smallDistribution.getProbabilityLaplaceSmoothing("item1"), 0.0);
        assertEquals(0.3, smallDistribution.getProbabilityLaplaceSmoothing("item2"), 0.0);
        assertEquals(0.2, smallDistribution.getProbabilityLaplaceSmoothing("item3"), 0.0);
        assertEquals(0.1, smallDistribution.getProbabilityLaplaceSmoothing("item4"), 0.0);
    }

    @Test
    public void testEntropy() {
        assertEquals(1.4591, smallDistribution.entropy(), 0.0001);
    }
}