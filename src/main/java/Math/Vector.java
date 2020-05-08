package Math;

import java.io.Serializable;
import java.util.ArrayList;

public class Vector implements Serializable {

    private int size;
    private ArrayList<Double> values;

    /**
     * A constructor of {@link Vector} class which takes an {@link ArrayList} values as an input. Then, initializes
     * values {@link ArrayList} and size variable with given input and ts size.
     *
     * @param values {@link ArrayList} input.
     */
    public Vector(ArrayList<Double> values) {
        this.values = values;
        size = values.size();
    }


    /**
     * Another constructor of {@link Vector} class which takes integer size and double x as inputs. Then, initializes size
     * variable with given size input and creates new values {@link ArrayList} and adds given input x to values {@link ArrayList}.
     *
     * @param size {@link ArrayList} size.
     * @param x    item to add values {@link ArrayList}.
     */
    public Vector(int size, double x) {
        this.size = size;
        values = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            values.add(x);
        }
    }

    /**
     * Another constructor of {@link Vector} class which takes integer size, integer index and double x as inputs. Then, initializes size
     * variable with given size input and creates new values {@link ArrayList} and adds 0.0 to values {@link ArrayList}.
     * Then, sets the item of values {@link ArrayList} at given index as given input x.
     *
     * @param size  {@link ArrayList} size.
     * @param index to set a particular item.
     * @param x     item to add values {@link ArrayList}'s given index.
     */
    public Vector(int size, int index, double x) {
        this.size = size;
        values = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            values.add(0.0);
        }
        values.set(index, x);
    }

    /**
     * Another constructor of {@link Vector} class which takes double values {@link java.lang.reflect.Array} as an input.
     * It creates new values {@link ArrayList} and adds given input values {@link java.lang.reflect.Array}'s each item to the values {@link ArrayList}.
     * Then, initializes size with given values input {@link java.lang.reflect.Array}'s length.
     *
     * @param values double {@link java.lang.reflect.Array} input.
     */
    public Vector(double[] values) {
        this.values = new ArrayList<>();
        for (double value : values) {
            this.values.add(value);
        }
        size = values.length;
    }

    /**
     * The biased method creates a {@link Vector} result, add adds each item of values {@link ArrayList} into the result Vector.
     * Then, insert 1.0 to 0th position and return result {@link Vector}.
     *
     * @return result {@link Vector}.
     */
    public Vector biased() {
        Vector result = new Vector(0, 0);
        for (Double value : values) {
            result.add(value);
        }
        result.insert(0, 1.0);
        return result;
    }

    /**
     * The add method adds given input to the values {@link ArrayList} and increments the size variable by one.
     *
     * @param x double input to add values {@link ArrayList}.
     */
    public void add(double x) {
        values.add(x);
        size++;
    }

    /**
     * The insert method puts given input to the given index of values {@link ArrayList} and increments the size variable by one.
     *
     * @param pos index to insert input.
     * @param x   input to insert to given index of values {@link ArrayList}.
     */
    public void insert(int pos, double x) {
        values.add(pos, x);
        size++;
    }

    /**
     * The remove method deletes the item at given input position of values {@link ArrayList} and decrements the size variable by one.
     *
     * @param pos index to remove from values {@link ArrayList}.
     */
    public void remove(int pos) {
        values.remove(pos);
        size--;
    }

    /**
     * The clear method sets all the elements of values {@link ArrayList} to 0.0.
     */
    public void clear() {
        for (int i = 0; i < values.size(); i++) {
            values.set(i, 0.0);
        }
    }

    /**
     * The sumOfElements method sums up all elements in the vector.
     *
     * @return Sum of all elements in the vector.
     */
    public double sumOfElements() {
        double total = 0;
        for (int i = 0; i < size; i++) {
            total += values.get(i);
        }
        return total;
    }

    /**
     * The maxIndex method gets the first item of values {@link ArrayList} as maximum item, then it loops through the indices
     * and if a greater value than the current maximum item comes, it updates the maximum item and returns the final
     * maximum item's index.
     *
     * @return final maximum item's index.
     */
    public int maxIndex() {
        int index = 0;
        double max = values.get(0);
        for (int i = 1; i < size; i++) {
            if (values.get(i) > max) {
                max = values.get(i);
                index = i;
            }
        }
        return index;
    }

    /**
     * The sigmoid method loops through the values {@link ArrayList} and sets each ith item with sigmoid function, i.e
     * 1 / (1 + Math.exp(-values.get(i))), i ranges from 0 to size.
     */
    public void sigmoid() {
        for (int i = 0; i < size; i++) {
            values.set(i, 1 / (1 + Math.exp(-values.get(i))));
        }
    }

    /**
     * The skipVector method takes a mod and a value as inputs. It creates a new result Vector, and assigns given input value to i.
     * While i is less than the size, it adds the ith item of values {@link ArrayList} to the result and increments i by given mod input.
     *
     * @param mod   integer input.
     * @param value integer input.
     * @return result Vector.
     */
    public Vector skipVector(int mod, int value) {
        Vector result = new Vector(0, 0);
        int i = value;
        while (i < size) {
            result.add(values.get(i));
            i += mod;
        }
        return result;
    }

    /**
     * The add method takes a {@link Vector} v as an input. It sums up the corresponding elements of both given vector's
     * values {@link ArrayList} and values {@link ArrayList} and puts result back to the values {@link ArrayList}.
     * If their sizes do not match, it throws a VectorSizeMismatch exception.
     *
     * @param v Vector to add.
     * @throws VectorSizeMismatch exception if sizes do not match.
     */
    public void add(Vector v) throws VectorSizeMismatch {
        if (size() != v.size()) {
            throw new VectorSizeMismatch();
        }
        for (int i = 0; i < size; i++) {
            values.set(i, values.get(i) + v.values.get(i));
        }
    }

    /**
     * The subtract method takes a {@link Vector} v as an input. It subtracts the corresponding elements of given vector's
     * values {@link ArrayList} from values {@link ArrayList} and puts result back to the values {@link ArrayList}.
     * If their sizes do not match, it throws a VectorSizeMismatch exception.
     *
     * @param v Vector to subtract from values {@link ArrayList}.
     * @throws VectorSizeMismatch exception if sizes do not match.
     */
    public void subtract(Vector v) throws VectorSizeMismatch {
        if (size() != v.size()) {
            throw new VectorSizeMismatch();
        }
        for (int i = 0; i < size; i++) {
            values.set(i, values.get(i) - v.values.get(i));
        }
    }

    /**
     * The difference method takes a {@link Vector} v as an input. It creates a new double {@link java.lang.reflect.Array} result, then
     * subtracts the corresponding elements of given vector's values {@link ArrayList} from values {@link ArrayList} and puts
     * result back to the result {@link java.lang.reflect.Array}. If their sizes do not match, it throws a VectorSizeMismatch exception.
     *
     * @param v Vector to find difference from values {@link ArrayList}.
     * @return new {@link Vector} with result {@link java.lang.reflect.Array}.
     * @throws VectorSizeMismatch exception if sizes do not match.
     */
    public Vector difference(Vector v) throws VectorSizeMismatch {
        if (size() != v.size()) {
            throw new VectorSizeMismatch();
        }
        double[] result = new double[v.size()];
        for (int i = 0; i < size; i++) {
            result[i] = values.get(i) - v.values.get(i);
        }
        return new Vector(result);
    }

    /**
     * The dotProduct method takes a {@link Vector} v as an input. It creates a new double variable result, then
     * multiplies the corresponding elements of given vector's values {@link ArrayList} with values {@link ArrayList} and assigns
     * the multiplication to the result. If their sizes do not match, it throws a VectorSizeMismatch exception.
     *
     * @param v Vector to find dot product.
     * @return double result.
     * @throws VectorSizeMismatch exception if sizes do not match.
     */
    public double dotProduct(Vector v) throws VectorSizeMismatch {
        if (size() != v.size()) {
            throw new VectorSizeMismatch();
        }
        double result = 0;
        for (int i = 0; i < size; i++) {
            result += values.get(i) * v.values.get(i);
        }
        return result;
    }

    /**
     * The dotProduct method creates a new double variable result, then squares the elements of values {@link ArrayList} and assigns
     * the accumulation to the result.
     *
     * @return double result.
     */
    public double dotProduct() {
        double result = 0;
        for (int i = 0; i < size; i++) {
            result += values.get(i) * values.get(i);
        }
        return result;
    }

    /**
     * The elementProduct method takes a {@link Vector} v as an input. It creates a new double {@link java.lang.reflect.Array} result, then
     * multiplies the corresponding elements of given vector's values {@link ArrayList} with values {@link ArrayList} and assigns
     * the multiplication to the result {@link java.lang.reflect.Array}. If their sizes do not match, it throws a VectorSizeMismatch exception.
     *
     * @param v Vector to find dot product.
     * @return Vector with result {@link java.lang.reflect.Array}.
     * @throws VectorSizeMismatch exception if sizes do not match.
     */
    public Vector elementProduct(Vector v) throws VectorSizeMismatch {
        if (size() != v.size()) {
            throw new VectorSizeMismatch();
        }
        double[] result = new double[v.size()];
        for (int i = 0; i < size; i++) {
            result[i] = values.get(i) * v.values.get(i);
        }
        return new Vector(result);
    }

    /**
     * The multiply method takes a {@link Vector} v as an input and creates new {@link Matrix} m of [size x size of input v].
     * It loops through the the both values {@link ArrayList} and given vector's values {@link ArrayList}, then multiply
     * each item with other with other items and puts to the new {@link Matrix} m.
     *
     * @param v Vector input.
     * @return Matrix that has multiplication of two vectors.
     */
    public Matrix multiply(Vector v) {
        Matrix m = new Matrix(size, v.size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < v.size; j++) {
                m.setValue(i, j, values.get(i) * v.values.get(j));
            }
        }
        return m;
    }

    /**
     * The divide method takes a double value as an input and divides each item of values {@link ArrayList} with given value.
     *
     * @param value is used to divide items of values {@link ArrayList}.
     */
    public void divide(double value) {
        for (int i = 0; i < size; i++) {
            values.set(i, values.get(i) / value);
        }
    }

    /**
     * The multiply method takes a double value as an input and multiplies each item of values {@link ArrayList} with given value.
     *
     * @param value is used to multiply items of values {@link ArrayList}.
     */
    public void multiply(double value) {
        for (int i = 0; i < size; i++) {
            values.set(i, values.get(i) * value);
        }
    }

    /**
     * The product method takes a double value as an input and creates a new result {@link Vector}, then multiplies each
     * item of values {@link ArrayList} with given value and adds to the result {@link Vector}.
     *
     * @param value is used to multiply items of values {@link ArrayList}.
     * @return Vector result.
     */
    public Vector product(double value) {
        Vector result = new Vector(0, 0);
        for (int i = 0; i < size; i++) {
            result.add(values.get(i) * value);
        }
        return result;
    }

    /**
     * The l1Normalize method is used to apply Least Absolute Errors, it accumulates items of values {@link ArrayList} and sets
     * each item by dividing it by the summation value.
     */
    public void l1Normalize() {
        double sum = 0;
        for (int i = 0; i < size; i++) {
            sum += values.get(i);
        }
        for (int i = 0; i < size; i++) {
            values.set(i, values.get(i) / sum);
        }
    }

    /**
     * The l2Norm method is used to apply Least Squares, it accumulates second power of each items of values {@link ArrayList}
     * and returns the square root of this summation.
     *
     * @return square root of this summation.
     */
    public double l2Norm() {
        double sum = 0;
        for (int i = 0; i < size; i++) {
            sum += Math.pow(values.get(i), 2);
        }
        return Math.sqrt(sum);
    }

    /**
     * The cosineSimilarity method takes a {@link Vector} v as an input and returns the result of dotProduct(v) / l2Norm() / v.l2Norm().
     * If sizes do not match it throws a {@link VectorSizeMismatch} exception.
     *
     * @param v Vector input.
     * @return dotProduct(v) / l2Norm() / v.l2Norm().
     * @throws VectorSizeMismatch exception.
     */
    public double cosineSimilarity(Vector v) throws VectorSizeMismatch {
        if (size() != v.size) {
            throw new VectorSizeMismatch();
        }
        return dotProduct(v) / l2Norm() / v.l2Norm();
    }

    /**
     * The size method returns the size of the values {@link ArrayList}.
     *
     * @return size of the values {@link ArrayList}.
     */
    public int size() {
        return values.size();
    }

    /**
     * Getter for the item at given index of values {@link ArrayList}.
     *
     * @param index used to get an item.
     * @return the item at given index.
     */
    public double getValue(int index) {
        return values.get(index);
    }

    /**
     * Setter for the setting the value at given index of values {@link ArrayList}.
     *
     * @param index to set.
     * @param value is used to set the given index
     */
    public void setValue(int index, double value) {
        values.set(index, value);
    }

    /**
     * The addValue method adds the given value to the item at given index of values {@link ArrayList}.
     *
     * @param index to add the given value.
     * @param value value to add to given index.
     */
    public void addValue(int index, double value) {
        values.set(index, values.get(index) + value);
    }

}
