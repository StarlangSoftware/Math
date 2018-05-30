package Math;

public class VectorSizeMismatch extends Exception {

    public VectorSizeMismatch(){

    }

    public String toString(){
        return "Number of items in both vectors must be the same";
    }

}
