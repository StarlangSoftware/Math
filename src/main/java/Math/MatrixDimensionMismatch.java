package Math;

public class MatrixDimensionMismatch extends Exception {

    /**
     * An empty constructor of {@link MatrixDimensionMismatch} class.
     */
    public MatrixDimensionMismatch() {

    }

    /**
     * The overridden toString method returns 'The number of rows and columns of the first matrix should be equal to the
     * number of rows and columns of the second matrix respectively' String.
     *
     * @return 'The number of rows and columns of the first matrix should be equal to the number of rows and columns of
     * the second matrix respectively' String.
     */
    public String toString() {
        return "The number of rows and columns of the first matrix should be equal to the number of rows and columns of the second matrix respectively.";
    }

}
