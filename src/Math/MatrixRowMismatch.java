package Math;

public class MatrixRowMismatch extends Exception {

    public MatrixRowMismatch(){

    }

    public String toString(){
        return "Number of rows of the matrix should be equal to the size of the vector.";
    }

}
