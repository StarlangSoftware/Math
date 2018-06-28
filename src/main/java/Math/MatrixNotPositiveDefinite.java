package Math;

public class MatrixNotPositiveDefinite extends Exception {

    /**
     * An empty constructor of {@link MatrixNotPositiveDefinite} class.
     */
    public MatrixNotPositiveDefinite() {

    }

    /**
     * The overridden toString method returns 'Matrix should be positive definite' String.
     *
     * @return 'Matrix should be positive definite' String.
     */
    public String toString() {
        return "Matrix should be positive definite.";
    }

}
