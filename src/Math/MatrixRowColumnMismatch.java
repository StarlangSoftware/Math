package Math;

public class MatrixRowColumnMismatch extends Exception{

    public MatrixRowColumnMismatch(){

    }

    public String toString(){
        return "The number of columns of the first matrix should be equal to the number of rows of the second matrix.";
    }

}
