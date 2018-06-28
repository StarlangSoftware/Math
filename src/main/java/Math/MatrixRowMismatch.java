package Math;

public class MatrixRowMismatch extends Exception {

    /**
     * An empty constructor of {@link MatrixRowMismatch} class.
     */
    public MatrixRowMismatch() {

    }

    /**
     * The overridden toString method returns 'Number of rows of the matrix should be equal to the size of the vector' String.
     *
     * @return 'Number of rows of the matrix should be equal to the size of the vector' String.
     */
    public String toString() {
        return "Number of rows of the matrix should be equal to the size of the vector.";
    }

}
