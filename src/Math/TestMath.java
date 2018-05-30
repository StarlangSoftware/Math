package Math;

import java.util.ArrayList;

public class TestMath {

    public static void main(String[] args){
        Matrix m = new Matrix(4, 4);
        m.setValue(0, 0, 2);
        m.setValue(0, 1, 0);
        m.setValue(0, 2, -2);
        m.setValue(0, 3, 0);
        m.setValue(1, 0, 0);
        m.setValue(1, 1, 2);
        m.setValue(1, 2, 0);
        m.setValue(1, 3, -2);
        m.setValue(2, 0, -2);
        m.setValue(2, 1, 0);
        m.setValue(2, 2, 2);
        m.setValue(2, 3, 0);
        m.setValue(3, 0, 0);
        m.setValue(3, 1, -2);
        m.setValue(3, 2, 0);
        m.setValue(3, 3, 2);
        try {
            ArrayList<Eigenvector> result = m.characteristics();
        } catch (MatrixNotSymmetric matrixNotSymmetric) {
        }
    }
}
