package Math;

public class VectorSizeMismatch extends Exception {

    /**
     * An empty constructor of {@link VectorSizeMismatch} class.
     */
    public VectorSizeMismatch() {

    }

    /**
     * The overridden toString method returns 'Number of items in both vectors must be the same' String.
     *
     * @return 'Number of items in both vectors must be the same' String.
     */
    public String toString() {
        return "Number of items in both vectors must be the same";
    }

}
