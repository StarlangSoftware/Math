package Math;

public class MatrixColumnMismatch extends Exception {

    public MatrixColumnMismatch(){

    }

    public String toString(){
        return "Number of columns of the matrix should be equal to the size of the vector.";
    }

}
