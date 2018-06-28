package Math;

public class DeterminantZero extends Exception {

    /**
     * An empty constructor of {@link DeterminantZero} class.
     */
    public DeterminantZero() {

    }

    /**
     * The overridden toString method returns 'Determinant of matrix is zero' String.
     *
     * @return 'Determinant of matrix is zero' String.
     */
    public String toString() {
        return "Determinant of matrix is zero.";
    }
}
