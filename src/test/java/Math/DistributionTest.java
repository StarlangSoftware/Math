package Math;

import org.junit.Test;

import static org.junit.Assert.*;

public class DistributionTest {

    @Test
    public void testZNormal() {
        assertEquals(0.5, Distribution.zNormal(0.0), 0.0);
        assertEquals(0.69146, Distribution.zNormal(0.5), 0.00001);
        assertEquals(0.84134, Distribution.zNormal(1.0), 0.00001);
        assertEquals(0.93319, Distribution.zNormal(1.5), 0.00001);
        assertEquals(0.97725, Distribution.zNormal(2.0), 0.00001);
        assertEquals(0.99379, Distribution.zNormal(2.5), 0.00001);
        assertEquals(0.99865, Distribution.zNormal(3.0), 0.00001);
        assertEquals(0.99977, Distribution.zNormal(3.5), 0.00001);
        assertEquals(1 - Distribution.zNormal(0.5), Distribution.zNormal(-0.5), 0.00001);
        assertEquals(1 - Distribution.zNormal(1.0), Distribution.zNormal(-1.0), 0.00001);
        assertEquals(1 - Distribution.zNormal(1.5), Distribution.zNormal(-1.5), 0.00001);
        assertEquals(1 - Distribution.zNormal(2.0), Distribution.zNormal(-2.0), 0.00001);
        assertEquals(1 - Distribution.zNormal(2.5), Distribution.zNormal(-2.5), 0.00001);
        assertEquals(1 - Distribution.zNormal(3.0), Distribution.zNormal(-3.0), 0.00001);
        assertEquals(1 - Distribution.zNormal(3.5), Distribution.zNormal(-3.5), 0.00001);
    }

    @Test
    public void testZInverse() {
        assertEquals(0.0, Distribution.zInverse(0.5), 0.00001);
        assertEquals(0.841621, Distribution.zInverse(0.8), 0.00001);
        assertEquals(1.281552, Distribution.zInverse(0.9), 0.00001);
        assertEquals(1.644854, Distribution.zInverse(0.95), 0.00001);
        assertEquals(2.053749, Distribution.zInverse(0.98), 0.00001);
        assertEquals(2.326348, Distribution.zInverse(0.99), 0.00001);
        assertEquals(2.575829, Distribution.zInverse(0.995), 0.00001);
        assertEquals(2.878162, Distribution.zInverse(0.998), 0.00001);
        assertEquals(3.090232, Distribution.zInverse(0.999), 0.00001);
    }

    @Test
    public void testChiSquare() {
        assertEquals(0.05, Distribution.chiSquare(3.841, 1), 0.0001);
        assertEquals(0.005, Distribution.chiSquare(7.879, 1), 0.0001);
        assertEquals(0.95, Distribution.chiSquare(3.940, 10), 0.0001);
        assertEquals(0.05, Distribution.chiSquare(18.307, 10), 0.0001);
        assertEquals(0.995, Distribution.chiSquare(2.156, 10), 0.0001);
        assertEquals(0.005, Distribution.chiSquare(25.188, 10), 0.0001);
        assertEquals(0.95, Distribution.chiSquare(77.929, 100), 0.0001);
        assertEquals(0.05, Distribution.chiSquare(124.342, 100), 0.0001);
        assertEquals(0.995, Distribution.chiSquare(67.328, 100), 0.0001);
        assertEquals(0.005, Distribution.chiSquare(140.169, 100), 0.0001);
    }

    @Test
    public void testChiSquareInverse() {
        assertEquals(2.706, Distribution.chiSquareInverse(0.1, 1), 0.001);
        assertEquals(6.635, Distribution.chiSquareInverse(0.01, 1), 0.001);
        assertEquals(4.865, Distribution.chiSquareInverse(0.9, 10), 0.001);
        assertEquals(15.987, Distribution.chiSquareInverse(0.1, 10), 0.001);
        assertEquals(2.558, Distribution.chiSquareInverse(0.99, 10), 0.001);
        assertEquals(23.209, Distribution.chiSquareInverse(0.01, 10), 0.001);
        assertEquals(82.358, Distribution.chiSquareInverse(0.9, 100), 0.001);
        assertEquals(118.498, Distribution.chiSquareInverse(0.1, 100), 0.001);
        assertEquals(70.065, Distribution.chiSquareInverse(0.99, 100), 0.001);
        assertEquals(135.807, Distribution.chiSquareInverse(0.01, 100), 0.001);
    }

    @Test
    public void testFDistribution() {
        assertEquals(0.1, Distribution.fDistribution(39.86346, 1, 1), 0.00001);
        assertEquals(0.1, Distribution.fDistribution(2.32260, 10, 10), 0.00001);
        assertEquals(0.1, Distribution.fDistribution(1.79384, 20, 20), 0.00001);
        assertEquals(0.1, Distribution.fDistribution(1.60648, 30, 30), 0.00001);
        assertEquals(0.05, Distribution.fDistribution(161.4476, 1, 1), 0.00001);
        assertEquals(0.05, Distribution.fDistribution(2.9782, 10, 10), 0.00001);
        assertEquals(0.05, Distribution.fDistribution(2.1242, 20, 20), 0.00001);
        assertEquals(0.05, Distribution.fDistribution(1.8409, 30, 30), 0.00001);
        assertEquals(0.01, Distribution.fDistribution(4052.181, 1, 1), 0.00001);
        assertEquals(0.01, Distribution.fDistribution(4.849, 10, 10), 0.00001);
        assertEquals(0.01, Distribution.fDistribution(2.938, 20, 20), 0.00001);
        assertEquals(0.01, Distribution.fDistribution(2.386, 30, 30), 0.00001);
    }

    @Test
    public void testFDistributionInverse() {
        assertEquals(3.818, Distribution.fDistributionInverse(0.01, 5, 26), 0.001);
        assertEquals(15.1010, Distribution.fDistributionInverse(0.025, 4, 3), 0.001);
        assertEquals(2.19535, Distribution.fDistributionInverse(0.1, 8, 13), 0.001);
        assertEquals(2.29871, Distribution.fDistributionInverse(0.1, 3, 27), 0.001);
        assertEquals(3.4381, Distribution.fDistributionInverse(0.05, 8, 8), 0.001);
        assertEquals(2.6283, Distribution.fDistributionInverse(0.05, 6, 19), 0.001);
        assertEquals(3.3120, Distribution.fDistributionInverse(0.025, 9, 13), 0.001);
        assertEquals(3.7505, Distribution.fDistributionInverse(0.025, 3, 23), 0.001);
        assertEquals(4.155, Distribution.fDistributionInverse(0.01, 12, 12), 0.001);
        assertEquals(6.851, Distribution.fDistributionInverse(0.01, 1, 120), 0.001);
    }

    @Test
    public void testTDistribution() {
        assertEquals(0.05, Distribution.tDistribution(6.314, 1), 0.0001);
        assertEquals(0.005, Distribution.tDistribution(63.656, 1), 0.0001);
        assertEquals(0.05, Distribution.tDistribution(1.812, 10), 0.0001);
        assertEquals(0.01, Distribution.tDistribution(2.764, 10), 0.0001);
        assertEquals(0.005, Distribution.tDistribution(3.169, 10), 0.0001);
        assertEquals(0.001, Distribution.tDistribution(4.144, 10), 0.0001);
        assertEquals(0.05, Distribution.tDistribution(1.725, 20), 0.0001);
        assertEquals(0.01, Distribution.tDistribution(2.528, 20), 0.0001);
        assertEquals(0.005, Distribution.tDistribution(2.845, 20), 0.0001);
        assertEquals(0.001, Distribution.tDistribution(3.552, 20), 0.0001);
    }

    @Test
    public void testTDistributionInverse() {
        assertEquals(2.947, Distribution.tDistributionInverse(0.005, 15), 0.001);
        assertEquals(1.717, Distribution.tDistributionInverse(0.05, 22), 0.001);
        assertEquals(3.365, Distribution.tDistributionInverse(0.01, 5), 0.001);
        assertEquals(3.922, Distribution.tDistributionInverse(0.0005, 18), 0.001);
        assertEquals(3.467, Distribution.tDistributionInverse(0.001, 24), 0.001);
        assertEquals(6.314, Distribution.tDistributionInverse(0.05, 1), 0.001);
        assertEquals(2.306, Distribution.tDistributionInverse(0.025, 8), 0.001);
        assertEquals(3.646, Distribution.tDistributionInverse(0.001, 17), 0.001);
        assertEquals(3.373, Distribution.tDistributionInverse(0.0005, 120), 0.001);
    }
}