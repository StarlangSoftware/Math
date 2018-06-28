package Math;

public class MatrixRowColumnMismatch extends Exception {

    /**
     * An empty constructor of {@link MatrixRowColumnMismatch} class.
     */
    public MatrixRowColumnMismatch() {

    }

    /**
     * The overridden toString method returns 'The number of columns of the first matrix should be equal to the number
     * of rows of the second matrix' String.
     *
     * @return 'The number of columns of the first matrix should be equal to the number of rows of the second matrix' String.
     */
    public String toString() {
        return "The number of columns of the first matrix should be equal to the number of rows of the second matrix.";
    }

}
