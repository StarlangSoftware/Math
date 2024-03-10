package Math;

import java.util.ArrayList;

public class Eigenvector extends Vector implements Comparable {
    private final double eigenValue;

    /**
     * A constructor of {@link Eigenvector} which takes a double eigenValue and an {@link ArrayList} values as inputs.
     * It calls its super class {@link Vector} with values {@link ArrayList} and initializes eigenValue variable with its
     * eigenValue input.
     *
     * @param eigenValue double input.
     * @param values     {@link ArrayList} input.
     */
    public Eigenvector(double eigenValue, ArrayList<Double> values) {
        super(values);
        this.eigenValue = eigenValue;
    }

    /**
     * The eigenValue method which returns the eigenValue variable.
     *
     * @return eigenValue variable.
     */
    public double eigenValue() {
        return eigenValue;
    }

    /**
     * The overridden compareTo method which takes an {@link Object} as an input. It subtracts given object's eigenValue from
     * eigenValue variable and assigns to the result variable. If the result is positive, it returns -1, if it is negative it returns
     * 1, 0 otherwise.
     *
     * @param o {@link Object} type input.
     * @return 1 if result is positive, 1 if it is negative, and 0 otherwise.
     */
    @Override
    public int compareTo(Object o) {
        double result = eigenValue - ((Eigenvector) o).eigenValue;
        if (result > 0) {
            return -1;
        } else {
            if (result < 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
