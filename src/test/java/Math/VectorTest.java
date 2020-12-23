package Math;

import org.junit.Before;

import static org.junit.Assert.*;

public class VectorTest {
    Vector smallVector1, smallVector2;
    Vector largeVector1, largeVector2;
    double[] data1 = {2, 3, 4, 5, 6};

    @Before
    public void setUp() {
        double[] data2 = {8, 7, 6, 5, 4};
        smallVector1 = new Vector(data1);
        smallVector2 = new Vector(data2);
        double [] largeData1 = new double[1000];
        for (int i = 1; i <= 1000; i++){
            largeData1[i - 1] = i;
        }
        largeVector1 = new Vector(largeData1);
        double [] largeData2 = new double[1000];
        for (int i = 1; i <= 1000; i++){
            largeData2[i - 1] = 1000 - i + 1;
        }
        largeVector2 = new Vector(largeData2);
    }

    @org.junit.Test
    public void testBiased() {
        Vector biased = smallVector1.biased();
        assertEquals(1, biased.getValue(0), 0.0);
        assertEquals(smallVector1.size() + 1, biased.size());
    }

    @org.junit.Test
    public void testElementAdd() {
        smallVector1.add(7);
        assertEquals(7, smallVector1.getValue(5), 0.0);
        assertEquals(6, smallVector1.size());
    }

    @org.junit.Test
    public void testInsert() {
        smallVector1.insert(3, 6);
        assertEquals(6, smallVector1.getValue(3), 0.0);
        assertEquals(6, smallVector1.size());
    }

    @org.junit.Test
    public void testRemove() {
        smallVector1.remove(2);
        assertEquals(5, smallVector1.getValue(2), 0.0);
        assertEquals(4, smallVector1.size());
    }

    @org.junit.Test
    public void testSumOfElementsSmall() {
        assertEquals(20, smallVector1.sumOfElements(), 0.0);
        assertEquals(30, smallVector2.sumOfElements(), 0.0);
    }

    @org.junit.Test
    public void testSumOfElementsLarge() {
        assertEquals(500500, largeVector1.sumOfElements(), 0.0);
        assertEquals(500500, largeVector2.sumOfElements(), 0.0);
    }

    @org.junit.Test
    public void testMaxIndex() {
        assertEquals(4, smallVector1.maxIndex());
        assertEquals(0, smallVector2.maxIndex());
    }

    @org.junit.Test
    public void testSigmoid() {
        Vector smallVector3 = new Vector(data1);
        smallVector3.sigmoid();
        assertEquals(0.8807971, smallVector3.getValue(0), 0.000001);
        assertEquals(0.9975274, smallVector3.getValue(4), 0.000001);
    }

    @org.junit.Test
    public void testSkipVectorSmall() {
        Vector smallVector3;
        smallVector3 = smallVector1.skipVector(2, 0);
        assertEquals(2, smallVector3.getValue(0), 0);
        assertEquals(6, smallVector3.getValue(2), 0);
        smallVector3 = smallVector1.skipVector(3, 1);
        assertEquals(3, smallVector3.getValue(0), 0);
        assertEquals(6, smallVector3.getValue(1), 0);
    }

    @org.junit.Test
    public void testSkipVectorLarge() {
        Vector largeVector3;
        largeVector3 = largeVector1.skipVector(2, 0);
        assertEquals(250000, largeVector3.sumOfElements(), 0);
        largeVector3 = largeVector1.skipVector(5, 3);
        assertEquals(100300, largeVector3.sumOfElements(), 0);
    }

    @org.junit.Test
    public void testVectorAddSmall() throws Exception{
        smallVector1.add(smallVector2);
        assertEquals(50, smallVector1.sumOfElements(), 0.0);
    }

    @org.junit.Test
    public void testVectorAddLarge() throws Exception{
        largeVector1.add(largeVector2);
        assertEquals(1001000, largeVector1.sumOfElements(), 0.0);
    }

    @org.junit.Test
    public void testSubtractSmall() throws Exception{
        smallVector1.subtract(smallVector2);
        assertEquals(-10, smallVector1.sumOfElements(), 0.0);
    }

    @org.junit.Test
    public void testSubtractLarge() throws Exception{
        largeVector1.subtract(largeVector2);
        assertEquals(0, largeVector1.sumOfElements(), 0.0);
    }

    @org.junit.Test
    public void testDifferenceSmall() throws Exception{
        Vector smallVector3 = smallVector1.difference(smallVector2);
        assertEquals(-10, smallVector3.sumOfElements(), 0.0);
    }

    @org.junit.Test
    public void testDifferenceLarge() throws Exception{
        Vector largeVector3 = largeVector1.difference(largeVector2);
        assertEquals(0, largeVector3.sumOfElements(), 0.0);
    }

    @org.junit.Test
    public void testDotProductWithVectorSmall() throws Exception{
        double dotProduct = smallVector1.dotProduct(smallVector2);
        assertEquals(110, dotProduct, 0.0);
    }

    @org.junit.Test
    public void testDotProductWithVectorLarge() throws Exception{
        double dotProduct = largeVector1.dotProduct(largeVector2);
        assertEquals(167167000, dotProduct, 0.0);
    }

    @org.junit.Test
    public void testDotProductWithItselfSmall() {
        double dotProduct = smallVector1.dotProduct();
        assertEquals(90, dotProduct, 0.0);
    }

    @org.junit.Test
    public void testDotProductWithItselfLarge() {
        double dotProduct = largeVector1.dotProduct();
        assertEquals(333833500, dotProduct, 0.0);
    }

    @org.junit.Test
    public void testElementProductSmall() throws Exception{
        Vector smallVector3 = smallVector1.elementProduct(smallVector2);
        assertEquals(110, smallVector3.sumOfElements(), 0.0);
    }

    @org.junit.Test
    public void testElementProductLarge() throws Exception{
        Vector largeVector3 = largeVector1.elementProduct(largeVector2);
        assertEquals(167167000, largeVector3.sumOfElements(), 0.0);
    }

    @org.junit.Test
    public void testDivide() {
        smallVector1.divide(10.0);
        assertEquals(2, smallVector1.sumOfElements(), 0.0);
    }

    @org.junit.Test
    public void testMultiply() {
        smallVector1.multiply(10.0);
        assertEquals(200, smallVector1.sumOfElements(), 0.0);
    }

    @org.junit.Test
    public void testProduct() {
        Vector smallVector3 = smallVector1.product(7.0);
        assertEquals(140, smallVector3.sumOfElements(), 0.0);
    }

    @org.junit.Test
    public void testL1NormalizeSmall() {
        smallVector1.l1Normalize();
        assertEquals(1.0, smallVector1.sumOfElements(), 0.0);
    }

    @org.junit.Test
    public void testL1NormalizeLarge() {
        largeVector1.l1Normalize();
        assertEquals(1.0, largeVector1.sumOfElements(), 0.0);
    }

    @org.junit.Test
    public void testL2NormSmall() {
        double norm = smallVector1.l2Norm();
        assertEquals(norm, Math.sqrt(90), 0.0);
    }

    @org.junit.Test
    public void testL2NormLarge() {
        double norm = largeVector1.l2Norm();
        assertEquals(norm, Math.sqrt(333833500), 0.0);
    }

    @org.junit.Test
    public void cosineSimilaritySmall() throws Exception{
        double similarity = smallVector1.cosineSimilarity(smallVector2);
        assertEquals(0.8411910, similarity, 0.000001);
    }

    @org.junit.Test
    public void cosineSimilarityLarge() throws Exception{
        double similarity = largeVector1.cosineSimilarity(largeVector2);
        assertEquals(0.5007497, similarity, 0.000001);
    }

}