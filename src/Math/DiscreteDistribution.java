package Math;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.*;

public class DiscreteDistribution extends LinkedHashMap<String, Integer> implements Serializable{

    private double sum = 0;

    public DiscreteDistribution(){
        super();
    }

    public void addItem(String item){
        if (containsKey(item)){
            put(item, get(item) + 1);
        } else {
            put(item, 1);
        }
        sum++;
    }

    public void removeItem(String item){
        if (containsKey(item)){
            put(item, get(item) - 1);
            if (get(item) == 0){
                remove(item);
            }
        }
    }

    public void addDistribution(DiscreteDistribution distribution){
        for (Entry<String, Integer> entry : distribution.entrySet()){
            if (containsKey(entry.getKey())){
                put(entry.getKey(), get(entry.getKey()) + entry.getValue());
            } else {
                put(entry.getKey(), entry.getValue());
            }
            sum += entry.getValue();
        }
    }

    public void removeDistribution(DiscreteDistribution distribution){
        for (Entry<String, Integer> entry : distribution.entrySet()){
            if (get(entry.getKey()) - entry.getValue() != 0){
                put(entry.getKey(), get(entry.getKey()) - entry.getValue());
            } else {
                remove(entry.getKey());
            }
            sum -= entry.getValue();
        }
    }

    public double getSum(){
        return sum;
    }

    public int getIndex(String item){
        return (new ArrayList<String>(keySet())).indexOf(item);
    }

    public boolean containsItem(String item){
        return containsKey(item);
    }

    public String getItem(int index){
        return (new ArrayList<String>(keySet())).get(index);
    }

    public Integer getValue(int index){
        return (new ArrayList<Integer>(values())).get(index);
    }

    public Integer getCount(String item){
        return get(item);
    }

    public String getMaxItem(){
        int max = -1;
        String maxItem = null;
        for (Entry<String, Integer> entry : entrySet()){
            if (entry.getValue() > max){
                max = entry.getValue();
                maxItem = entry.getKey();
            }
        }
        return maxItem;
    }

    public String getMaxItem(ArrayList<String> includeTheseOnly){
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

    public double getProbability(String item){
        if (containsKey(item)){
            return get(item) / sum;
        } else {
            return 0.0;
        }
    }

    public double getProbabilityLaplaceSmoothing(String item){
        if (containsKey(item)){
            return (get(item) + 1) / (sum + size() + 1);
        } else {
            return 1.0 / (sum + size() + 1);
        }
    }

    public double entropy(){
        double total = 0.0, probability;
        for (Integer count : values()) {
            probability = count / sum;
            total += -probability * (Math.log(probability) / Math.log(2));
        }
        return total;
    }

}
