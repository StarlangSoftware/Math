package Math;

public class MatrixNotSymmetric extends Exception {

    /**
     * An empty constructor of {@link MatrixNotSymmetric} class.
     */
    public MatrixNotSymmetric(){

    }
    /**
     * The overridden toString method returns 'Matrix should be symmetric' String.
     *
     * @return 'Matrix should be symmetric' String.
     */
    public String toString(){
        return "Matrix should be symmetric.";
    }

}
