package Math;

public class MatrixDimensionMismatch extends Exception {

    public MatrixDimensionMismatch(){

    }

    public String toString(){
        return "The number of rows and columns of the first matrix should be equal to the number of rows and columns of the second matrix respectively.";
    }

}
