package Math;

import java.util.ArrayList;

public class Eigenvector extends Vector implements Comparable{
    private double eigenValue;

    public Eigenvector(double eigenValue, ArrayList<Double> values){
        super(values);
        this.eigenValue = eigenValue;
    }

    public double eigenValue(){
        return eigenValue;
    }

    @Override
    public int compareTo(Object o) {
        double result = eigenValue - ((Eigenvector)o).eigenValue;
        if (result > 0){
            return -1;
        } else {
            if (result < 0){
                return 1;
            } else {
                return 0;
            }
        }
    }
}
