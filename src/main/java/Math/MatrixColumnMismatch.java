package Math;

public class MatrixColumnMismatch extends Exception {

    /**
     * An empty constructor of {@link MatrixColumnMismatch} class.
     */
    public MatrixColumnMismatch() {

    }

    /**
     * The overridden toString method returns 'Number of columns of the matrix should be equal to the size of the vector' String.
     *
     * @return 'Number of columns of the matrix should be equal to the size of the vector' String.
     */
    public String toString() {
        return "Number of columns of the matrix should be equal to the size of the vector.";
    }

}
