package Math;

import java.util.ArrayList;

public class TestMath {

    public static void main(String[] args){
        Matrix m = new Matrix(1000, 1000, 0.0, 1.0);
        try {
            m.inverse();
        } catch (DeterminantZero | MatrixNotSquare error) {
        }
    }
}
