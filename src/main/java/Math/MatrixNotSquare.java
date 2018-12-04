package Math;

public class MatrixNotSquare extends Exception{

    /**
     * An empty constructor of {@link MatrixNotSquare} class.
     */
    public MatrixNotSquare() {

    }

    /**
     * The overridden toString method returns 'Matrix should be square' String.
     *
     * @return 'Matrix should be square' String.
     */
    public String toString() {
        return "Matrix should be square.";
    }

}
