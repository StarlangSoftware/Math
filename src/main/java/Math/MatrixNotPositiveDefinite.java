package Math;

public class MatrixNotPositiveDefinite extends Exception{

    public MatrixNotPositiveDefinite(){

    }

    public String toString(){
        return "Matrix should be positive definite.";
    }

}
