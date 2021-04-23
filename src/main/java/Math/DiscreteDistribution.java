package Math;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.*;

public class DiscreteDistribution extends LinkedHashMap<String, Integer> implements Serializable {

    private double sum = 0;

    /**
     * A constructor of {@link DiscreteDistribution} class which calls its super class.
     */
    public DiscreteDistribution() {
        super();
    }

    /**
     * The addItem method takes a String item as an input and if this map contains a mapping for the item it puts the item
     * with given value + 1, else it puts item with value of 1.
     *
     * @param item String input.
     */
    public void addItem(String item) {
        if (containsKey(item)) {
            put(item, get(item) + 1);
        } else {
            put(item, 1);
        }
        sum++;
    }

    /**
     * The removeItem method takes a String item as an input and if this map contains a mapping for the item it puts the item
     * with given value - 1, and if its value is 0, it removes the item.
     *
     * @param item String input.
     */
    public void removeItem(String item) {
        if (containsKey(item)) {
            put(item, get(item) - 1);
            if (get(item) == 0) {
                remove(item);
            }
        }
    }

    /**
     * The addDistribution method takes a {@link DiscreteDistribution} as an input and loops through the entries in this distribution
     * and if this map contains a mapping for the entry it puts the entry with its value + entry, else it puts entry with its value.
     * It also accumulates the values of entries and assigns to the sum variable.
     *
     * @param distribution {@link DiscreteDistribution} type input.
     */
    public void addDistribution(DiscreteDistribution distribution) {
        for (Entry<String, Integer> entry : distribution.entrySet()) {
            if (containsKey(entry.getKey())) {
                put(entry.getKey(), get(entry.getKey()) + entry.getValue());
            } else {
                put(entry.getKey(), entry.getValue());
            }
            sum += entry.getValue();
        }
    }

    /**
     * The removeDistribution method takes a {@link DiscreteDistribution} as an input and loops through the entries in this distribution
     * and if this map contains a mapping for the entry it puts the entry with its key - value, else it removes the entry.
     * It also decrements the value of entry from sum and assigns to the sum variable.
     *
     * @param distribution {@link DiscreteDistribution} type input.
     */
    public void removeDistribution(DiscreteDistribution distribution) {
        for (Entry<String, Integer> entry : distribution.entrySet()) {
            if (get(entry.getKey()) - entry.getValue() != 0) {
                put(entry.getKey(), get(entry.getKey()) - entry.getValue());
            } else {
                remove(entry.getKey());
            }
            sum -= entry.getValue();
        }
    }

    /**
     * The getter for sum variable.
     *
     * @return sum.
     */
    public double getSum() {
        return sum;
    }

    /**
     * The getIndex method takes an item as an input and returns the index of given item.
     *
     * @param item to search for index.
     * @return index of given item.
     */
    public int getIndex(String item) {
        return (new ArrayList<String>(keySet())).indexOf(item);
    }

    /**
     * The containsItem method takes an item as an input and returns true if this map contains a mapping for the
     * given item.
     *
     * @param item to check.
     * @return true if this map contains a mapping for the given item.
     */
    public boolean containsItem(String item) {
        return containsKey(item);
    }

    /**
     * The getItem method takes an index as an input and returns the item at given index.
     *
     * @param index is used for searching the item.
     * @return the item at given index.
     */
    public String getItem(int index) {
        return (new ArrayList<String>(keySet())).get(index);
    }

    /**
     * The getValue method takes an index as an input and returns the value at given index.
     *
     * @param index is used for searching the value.
     * @return the value at given index.
     */
    public Integer getValue(int index) {
        return (new ArrayList<Integer>(values())).get(index);
    }

    /**
     * The getCount method takes an item as an input returns the value to which the specified item is mapped, or {@code null}
     * if this map contains no mapping for the key.
     *
     * @param item is used to search for value.
     * @return the value to which the specified item is mapped
     */
    public Integer getCount(String item) {
        return get(item);
    }

    /**
     * The getMaxItem method loops through the entries and gets the entry with maximum value.
     *
     * @return the entry with maximum value.
     */
    public String getMaxItem() {
        int max = -1;
        String maxItem = null;
        for (Entry<String, Integer> entry : entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                maxItem = entry.getKey();
            }
        }
        return maxItem;
    }

    /**
     * Another getMaxItem method which takes an {@link ArrayList} of Strings. It loops through the items in this {@link ArrayList}
     * and gets the item with maximum value.
     *
     * @param includeTheseOnly {@link ArrayList} of Strings.
     * @return the item with maximum value.
     */
    public String getMaxItem(ArrayList<String> includeTheseOnly) {
        int max = -1;
        String maxItem = null;
        for (String item : includeTheseOnly) {
            int frequency = 0;
            if (containsItem(item)) {
                frequency = get(item);
            }
            if (frequency > max) {
                max = frequency;
                maxItem = item;
            }
        }
        return maxItem;
    }

    /**
     * The getProbability method takes an item as an input returns the value to which the specified item is mapped over sum,
     * or 0.0 if this map contains no mapping for the key.
     *
     * @param item is used to search for probability.
     * @return the probability to which the specified item is mapped.
     */
    public double getProbability(String item) {
        if (containsKey(item)) {
            return get(item) / sum;
        } else {
            return 0.0;
        }
    }

    public HashMap<String, Double> getProbabilityDistribution(){
        HashMap<String, Double> result = new HashMap<>();
        for (String item : keySet()){
            result.put(item, getProbability(item));
        }
        return result;
    }

    /**
     * The getProbabilityLaplaceSmoothing method takes an item as an input returns the smoothed value to which the specified
     * item is mapped over sum, or 1.0 over sum if this map contains no mapping for the key.
     *
     * @param item is used to search for probability.
     * @return the smoothed probability to which the specified item is mapped.
     */
    public double getProbabilityLaplaceSmoothing(String item) {
        if (containsKey(item)) {
            return (get(item) + 1) / (sum + size() + 1);
        } else {
            return 1.0 / (sum + size() + 1);
        }
    }

    /**
     * The entropy method loops through the values and calculates the entropy of these values.
     *
     * @return entropy value.
     */
    public double entropy() {
        double total = 0.0, probability;
        for (Integer count : values()) {
            probability = count / sum;
            total += -probability * (Math.log(probability) / Math.log(2));
        }
        return total;
    }

}
