package Math;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class MatrixTest {
    Matrix small;
    Matrix medium;
    Vector v;
    Matrix large;
    Vector V;
    Vector vr;
    Matrix random;
    Matrix identity;
    double originalSum;

    @Before
    public void setUp(){
        small = new Matrix(3, 3);
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                small.setValue(i, j, 1.0);
            }
        }
        v = new Vector(3, 1.0);
        large = new Matrix(1000, 1000);
        for (int i = 0; i < 1000; i++){
            for (int j = 0; j < 1000; j++){
                large.setValue(i, j, 1.0);
            }
        }
        medium = new Matrix(100, 100);
        for (int i = 0; i < 100; i++){
            for (int j = 0; j < 100; j++){
                medium.setValue(i, j, 1.0);
            }
        }
        V = new Vector(1000, 1.0);
        vr = new Vector(100, 1.0);
        random = new Matrix(100, 100, 1, 10, new Random());
        originalSum = random.sumOfElements();
        identity = new Matrix(100);
    }

    @Test
    public void testColumnWiseNormalize() {
        Matrix mClone = small.clone();
        mClone.columnWiseNormalize();
        assertEquals(3, mClone.sumOfElements(), 0.0);
        Matrix MClone = large.clone();
        MClone.columnWiseNormalize();
        assertEquals(1000, MClone.sumOfElements(), 0.001);
        identity.columnWiseNormalize();
        assertEquals(100, identity.sumOfElements(), 0.0);
    }

    @Test
    public void testMultiplyWithConstant() {
        small.multiplyWithConstant(4);
        assertEquals(36, small.sumOfElements(), 0.0);
        small.divideByConstant(4);
        large.multiplyWithConstant(1.001);
        assertEquals(1001000, large.sumOfElements(), 0.001);
        large.divideByConstant(1.001);
        random.multiplyWithConstant(3.6);
        assertEquals(originalSum * 3.6, random.sumOfElements(), 0.0001);
        random.divideByConstant(3.6);
    }

    @Test
    public void testDivideByConstant() {
        small.divideByConstant(4);
        assertEquals(2.25, small.sumOfElements(), 0.0);
        small.multiplyWithConstant(4);
        large.divideByConstant(10);
        assertEquals(100000, large.sumOfElements(), 0.001);
        large.multiplyWithConstant(10);
        random.divideByConstant(3.6);
        assertEquals(originalSum / 3.6, random.sumOfElements(), 0.0001);
        random.multiplyWithConstant(3.6);
    }

    @Test
    public void testAdd() throws Exception{
        random.add(identity);
        assertEquals(originalSum + 100, random.sumOfElements(), 0.0001);
        random.subtract(identity);
    }

    @Test
    public void testAddVector() throws Exception{
        large.add(4, V);
        assertEquals(1001000, large.sumOfElements(), 0.0);
        V.multiply(-1.0);
        large.add(4, V);
        V.multiply(-1.0);
    }

    @Test
    public void testSubtract() throws Exception{
        random.subtract(identity);
        assertEquals(originalSum - 100, random.sumOfElements(), 0.0001);
        random.add(identity);
    }

    @Test
    public void testMultiplyWithVectorFromLeft() throws Exception{
        Vector result = small.multiplyWithVectorFromLeft(v);
        assertEquals(9, result.sumOfElements(), 0.0);
        result = large.multiplyWithVectorFromLeft(V);
        assertEquals(1000000, result.sumOfElements(), 0.0);
        result = random.multiplyWithVectorFromLeft(vr);
        assertEquals(originalSum, result.sumOfElements(), 0.0001);
    }

    @Test
    public void testMultiplyWithVectorFromRight() throws Exception{
        Vector result = small.multiplyWithVectorFromRight(v);
        assertEquals(9, result.sumOfElements(), 0.0);
        result = large.multiplyWithVectorFromRight(V);
        assertEquals(1000000, result.sumOfElements(), 0.0);
        result = random.multiplyWithVectorFromRight(vr);
        assertEquals(originalSum, result.sumOfElements(), 0.0001);
    }

    @Test
    public void testColumnSum() {
        Random rand = new Random();
        assertEquals(3, small.columnSum(rand.nextInt(3)), 0.0);
        assertEquals(1000, large.columnSum(rand.nextInt(1000)), 0.0);
        assertEquals(1, identity.columnSum(rand.nextInt(100)), 0.0);
    }

    @Test
    public void testSumOfRows() {
        assertEquals(9, small.sumOfRows().sumOfElements(), 0.0);
        assertEquals(1000000, large.sumOfRows().sumOfElements(), 0.0);
        assertEquals(100, identity.sumOfRows().sumOfElements(), 0.0);
        assertEquals(originalSum, random.sumOfRows().sumOfElements(), 0.001);
    }

    @Test
    public void testRowSum() {
        Random rand = new Random();
        assertEquals(3, small.rowSum(rand.nextInt(3)), 0.0);
        assertEquals(1000, large.rowSum(rand.nextInt(1000)), 0.0);
        assertEquals(1, identity.rowSum(rand.nextInt(100)), 0.0);
    }

    @Test
    public void testMultiply() throws Exception{
        Matrix result = small.multiply(small);
        assertEquals(27, result.sumOfElements(), 0.0);
        result = large.multiply(large);
        assertEquals(1000000000.0, result.sumOfElements(), 0.0);
        result = random.multiply(identity);
        assertEquals(originalSum, result.sumOfElements(), 0.0);
        result = identity.multiply(random);
        assertEquals(originalSum, result.sumOfElements(), 0.0);
    }

    @Test
    public void testElementProduct() throws Exception{
        Matrix result = small.elementProduct(small);
        assertEquals(9, result.sumOfElements(), 0.0);
        result = large.elementProduct(large);
        assertEquals(1000000, result.sumOfElements(), 0.0);
        result = random.elementProduct(identity);
        assertEquals(result.trace(), result.sumOfElements(), 0.0);
    }

    @Test
    public void testSumOfElements() {
        assertEquals(9, small.sumOfElements(), 0.0);
        assertEquals(1000000, large.sumOfElements(), 0.0);
        assertEquals(100, identity.sumOfElements(), 0.0);
        assertEquals(originalSum, random.sumOfElements(), 0.0);
    }

    @Test
    public void testTrace() throws Exception{
        assertEquals(3, small.trace(), 0.0);
        assertEquals(1000, large.trace(), 0.0);
        assertEquals(100, identity.trace(), 0.0);
    }

    @Test
    public void testTranspose() {
        assertEquals(9, small.transpose().sumOfElements(), 0.0);
        assertEquals(1000000, large.transpose().sumOfElements(), 0.0);
        assertEquals(100, identity.transpose().sumOfElements(), 0.0);
        assertEquals(originalSum, random.transpose().sumOfElements(), 0.001);
    }

    @Test
    public void testIsSymmetric() throws Exception{
        assertTrue(small.isSymmetric());
        assertTrue(large.isSymmetric());
        assertTrue(identity.isSymmetric());
        assertFalse(random.isSymmetric());
    }

    @Test
    public void testDeterminant() throws Exception{
        assertEquals(0, small.determinant(), 0.0);
        assertEquals(0, large.determinant(), 0.0);
        assertEquals(1, identity.determinant(), 0.0);
    }

    @Test
    public void testInverse() throws Exception{
        identity.inverse();
        assertEquals(100, identity.sumOfElements(), 0.0);
        random.inverse();
        random.inverse();
        assertEquals(originalSum, random.sumOfElements(), 0.00001);
    }

    @Test
    public void testCharacteristics() throws Exception{
        ArrayList<Eigenvector> vectors = small.characteristics();
        assertEquals(2, vectors.size(), 0);
        vectors = identity.characteristics();
        assertEquals(100, vectors.size(), 0);
        vectors = medium.characteristics();
        assertEquals(46, vectors.size(), 0);
    }


}