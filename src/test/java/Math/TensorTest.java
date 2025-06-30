package Math;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TensorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testConstructorWithInferredShape() {
        List<List<Double>> data = Arrays.asList(Arrays.asList(1.0, 2.0), Arrays.asList(3.0, 4.0));
        Tensor tensor = new Tensor(data);
        assertArrayEquals(new int[]{2, 2}, tensor.getShape());
        assertEquals(Arrays.asList(1.0, 2.0, 3.0, 4.0), tensor.getData());
    }

    @Test
    public void testConstructorWithExplicitShape() {
        List<Double> data = Arrays.asList(1.0, 2.0, 3.0, 4.0);
        Tensor tensor = new Tensor(data, new int[]{2, 2});
        assertArrayEquals(new int[]{2, 2}, tensor.getShape());
        assertEquals(Arrays.asList(1.0, 2.0, 3.0, 4.0), tensor.getData());
    }

    @Test
    public void testConstructorShapeMismatch() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Shape does not match the number of elements in data.");
        List<Double> data = Arrays.asList(1.0, 2.0, 3.0);
        new Tensor(data, new int[]{2, 2});
    }

    @Test
    public void testGetShape() {
        List<Double> data = Arrays.asList(1.0, 2.0, 3.0);
        Tensor tensor = new Tensor(data, new int[]{3});
        assertArrayEquals(new int[]{3}, tensor.getShape());
    }

    @Test
    public void testGetData() {
        List<Double> data = Arrays.asList(1.0, 2.0, 3.0, 4.0);
        Tensor tensor = new Tensor(data, new int[]{2, 2});
        assertEquals(data, tensor.getData());
    }

    @Test
    public void testGetValidIndices() {
        List<Double> data = Arrays.asList(1.0, 2.0, 3.0, 4.0);
        Tensor tensor = new Tensor(data, new int[]{2, 2});
        assertEquals(1.0, tensor.getValue(new int[]{0, 0}), 1e-9);
        assertEquals(4.0, tensor.getValue(new int[]{1, 1}), 1e-9);
    }

    @Test
    public void testGetOutOfBoundsIndices() {
        List<Double> data = Arrays.asList(1.0, 2.0);
        Tensor tensor = new Tensor(data, new int[]{2});
        thrown.expect(IndexOutOfBoundsException.class);
        tensor.getValue(new int[]{2});
    }

    @Test
    public void testSetValidIndices() {
        List<Double> data = Arrays.asList(1.0, 2.0, 3.0, 4.0);
        Tensor tensor = new Tensor(data, new int[]{2, 2});
        tensor.set(new int[]{0, 0}, 5.0);
        assertEquals(5.0, tensor.getValue(new int[]{0, 0}), 1e-9);
    }

    @Test
    public void testSetOutOfBoundsIndices() {
        List<Double> data = Arrays.asList(1.0, 2.0);
        Tensor tensor = new Tensor(data, new int[]{2});
        thrown.expect(IndexOutOfBoundsException.class);
        tensor.set(new int[]{2}, 5.0);
    }

    @Test
    public void testReshapeValid() {
        List<Double> data = Arrays.asList(1.0, 2.0, 3.0, 4.0);
        Tensor tensor = new Tensor(data, new int[]{2, 2});
        Tensor reshaped = tensor.reshape(new int[]{4});
        assertArrayEquals(new int[]{4}, reshaped.getShape());
        assertEquals(data, reshaped.getData());
    }

    @Test
    public void testReshapeInvalid() {
        List<Double> data = Arrays.asList(1.0, 2.0, 3.0);
        Tensor tensor = new Tensor(data, new int[]{3});
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Total number of elements must remain the same.");
        tensor.reshape(new int[]{2, 2});
    }

    @Test
    public void testTransposeNoAxes() {
        List<List<Double>> data = Arrays.asList(Arrays.asList(1.0, 2.0), Arrays.asList(3.0, 4.0));
        Tensor tensor = new Tensor(data);
        Tensor transposed = tensor.transpose(null);
        assertArrayEquals(new int[]{2, 2}, transposed.getShape());
        assertEquals(Arrays.asList(1.0, 3.0, 2.0, 4.0), transposed.getData());
    }

    @Test
    public void testTransposeWithAxes() {
        List<List<Double>> data = Arrays.asList(Arrays.asList(1.0, 2.0, 3.0), Arrays.asList(4.0, 5.0, 6.0));
        Tensor tensor = new Tensor(data);
        Tensor transposed = tensor.transpose(new int[]{1, 0});
        assertArrayEquals(new int[]{3, 2}, transposed.getShape());
        assertEquals(Arrays.asList(1.0, 4.0, 2.0, 5.0, 3.0, 6.0), transposed.getData());
    }

    @Test
    public void testTransposeInvalidAxesLength() {
        List<List<Double>> data = Arrays.asList(Arrays.asList(1.0, 2.0), Arrays.asList(3.0, 4.0));
        Tensor tensor = new Tensor(data);
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid transpose axes.");
        tensor.transpose(new int[]{0});
    }

    @Test
    public void testTransposeInvalidAxesValue() {
        List<List<Double>> data = Arrays.asList(Arrays.asList(1.0, 2.0), Arrays.asList(3.0, 4.0));
        Tensor tensor = new Tensor(data);
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid transpose axes.");
        tensor.transpose(new int[]{0, 2});
    }

    @Test
    public void testBroadcastToValid() {
        List<Double> data = Arrays.asList(1.0, 2.0);
        Tensor tensor = new Tensor(data, new int[]{1, 2});
        Tensor broadcasted = tensor.broadcastTo(new int[]{2, 2});
        assertArrayEquals(new int[]{2, 2}, broadcasted.getShape());
        assertEquals(Arrays.asList(1.0, 2.0, 1.0, 2.0), broadcasted.getData());
    }

    @Test
    public void testBroadcastToInvalid() {
        List<Double> data = Arrays.asList(1.0, 2.0);
        Tensor tensor = new Tensor(data, new int[]{2});
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Cannot broadcast shape [2] to [3]");
        tensor.broadcastTo(new int[]{3});
    }

    @Test
    public void testAddSameShape() {
        List<Double> data1 = Arrays.asList(1.0, 2.0, 3.0, 4.0);
        Tensor tensor1 = new Tensor(data1, new int[]{2, 2});
        List<Double> data2 = Arrays.asList(5.0, 6.0, 7.0, 8.0);
        Tensor tensor2 = new Tensor(data2, new int[]{2, 2});
        Tensor sum = tensor1.add(tensor2);
        assertArrayEquals(new int[]{2, 2}, sum.getShape());
        assertEquals(Arrays.asList(6.0, 8.0, 10.0, 12.0), sum.getData());
    }

    @Test
    public void testAddWithBroadcasting() {
        List<Double> data1 = Arrays.asList(1.0, 2.0);
        Tensor tensor1 = new Tensor(data1, new int[]{1, 2});
        List<Double> data2 = Arrays.asList(3.0, 4.0);
        Tensor tensor2 = new Tensor(data2, new int[]{2, 1});
        Tensor sum = tensor1.add(tensor2);
        assertArrayEquals(new int[]{2, 2}, sum.getShape());
        assertEquals(Arrays.asList(4.0, 5.0, 5.0, 6.0), sum.getData());
    }

    @Test
    public void testSubtractSameShape() {
        List<Double> data1 = Arrays.asList(5.0, 6.0, 7.0, 8.0);
        Tensor tensor1 = new Tensor(data1, new int[]{2, 2});
        List<Double> data2 = Arrays.asList(1.0, 2.0, 3.0, 4.0);
        Tensor tensor2 = new Tensor(data2, new int[]{2, 2});
        Tensor diff = tensor1.subtract(tensor2);
        assertArrayEquals(new int[]{2, 2}, diff.getShape());
        assertEquals(Arrays.asList(4.0, 4.0, 4.0, 4.0), diff.getData());
    }

    @Test
    public void testSubtractWithBroadcasting() {
        List<Double> data1 = Arrays.asList(5.0, 5.0);
        Tensor tensor1 = new Tensor(data1, new int[]{2, 1});
        List<Double> data2 = Arrays.asList(1.0, 2.0);
        Tensor tensor2 = new Tensor(data2, new int[]{1, 2});
        Tensor diff = tensor1.subtract(tensor2);
        assertArrayEquals(new int[]{2, 2}, diff.getShape());
        assertEquals(Arrays.asList(4.0, 3.0, 4.0, 3.0), diff.getData());
    }

    @Test
    public void testHadamardProductSameShape() {
        List<Double> data1 = Arrays.asList(1.0, 2.0, 3.0, 4.0);
        Tensor tensor1 = new Tensor(data1, new int[]{2, 2});
        List<Double> data2 = Arrays.asList(5.0, 6.0, 7.0, 8.0);
        Tensor tensor2 = new Tensor(data2, new int[]{2, 2});
        Tensor product = tensor1.hadamardProduct(tensor2);
        assertArrayEquals(new int[]{2, 2}, product.getShape());
        assertEquals(Arrays.asList(5.0, 12.0, 21.0, 32.0), product.getData());
    }

    @Test
    public void testHadamardProductWithBroadcasting() {
        List<Double> data1 = Arrays.asList(1.0, 2.0);
        Tensor tensor1 = new Tensor(data1, new int[]{1, 2});
        List<Double> data2 = Arrays.asList(3.0, 4.0);
        Tensor tensor2 = new Tensor(data2, new int[]{2, 1});
        Tensor product = tensor1.hadamardProduct(tensor2);
        assertArrayEquals(new int[]{2, 2}, product.getShape());
        assertEquals(Arrays.asList(3.0, 6.0, 4.0, 8.0), product.getData());
    }

    @Test
    public void testMatrixMultiply2D() {
        List<List<Double>> data1 = Arrays.asList(
            Arrays.asList(1.0, 2.0),
            Arrays.asList(3.0, 4.0)
        );
        List<List<Double>> data2 = Arrays.asList(
            Arrays.asList(5.0, 6.0),
            Arrays.asList(7.0, 8.0)
        );
        Tensor tensor1 = new Tensor(data1);
        Tensor tensor2 = new Tensor(data2);
        Tensor result = tensor1.multiply(tensor2);
        assertArrayEquals(new int[]{2, 2}, result.getShape());
        assertEquals(Arrays.asList(19.0, 22.0, 43.0, 50.0), result.getData());
    }

    @Test
    public void testMatrixMultiply3D() {
        List<List<List<Double>>> data1 = Arrays.asList(
            Arrays.asList(Arrays.asList(1.0, 2.0), Arrays.asList(3.0, 4.0)),
            Arrays.asList(Arrays.asList(9.0, 10.0), Arrays.asList(11.0, 12.0))
        );
        List<List<List<Double>>> data2 = Arrays.asList(
            Arrays.asList(Arrays.asList(5.0, 6.0), Arrays.asList(7.0, 8.0)),
            Arrays.asList(Arrays.asList(13.0, 14.0), Arrays.asList(15.0, 16.0))
        );
        Tensor tensor1 = new Tensor(data1);
        Tensor tensor2 = new Tensor(data2);
        Tensor result = tensor1.multiply(tensor2);
        assertArrayEquals(new int[]{2, 2, 2}, result.getShape());
        assertEquals(Arrays.asList(19.0, 22.0, 43.0, 50.0, 267.0, 286.0, 323.0, 346.0), result.getData());
    }

    @Test
    public void testMatrixMultiplyInvalidShapes() {
        List<Double> data1 = Arrays.asList(1.0, 2.0, 3.0);
        Tensor tensor1 = new Tensor(data1, new int[]{3});
        List<List<Double>> data2 = Arrays.asList(Arrays.asList(4.0, 5.0), Arrays.asList(6.0, 7.0));
        Tensor tensor2 = new Tensor(data2);
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Shapes [3] and [2, 2] are not aligned for multiplication.");
        tensor1.multiply(tensor2);
    }

    @Test
    public void testMatrixMultiplyUnsupportedDimensions() {
        // Test with 1D tensors (not supported)
        List<Double> data1 = Arrays.asList(1.0, 2.0);
        Tensor tensor1 = new Tensor(data1, new int[]{2});
        List<Double> data2 = Arrays.asList(3.0, 4.0);
        Tensor tensor2 = new Tensor(data2, new int[]{2});
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Shapes [2] and [2] are not aligned for multiplication.");
        tensor1.multiply(tensor2);
    }

    @Test
    public void testPartialValid() {
        List<List<Double>> data = Arrays.asList(
                Arrays.asList(1.0, 2.0, 3.0),
                Arrays.asList(4.0, 5.0, 6.0),
                Arrays.asList(7.0, 8.0, 9.0)
        );
        Tensor tensor = new Tensor(data);
        Tensor partial = tensor.partial(new int[]{0, 1}, new int[]{2, 3});
        assertArrayEquals(new int[]{2, 2}, partial.getShape());
        assertEquals(Arrays.asList(2.0, 3.0, 5.0, 6.0), partial.getData());
    }

    @Test
    public void testPartialInvalidIndicesLength() {
        List<List<Double>> data = Arrays.asList(Arrays.asList(1.0, 2.0), Arrays.asList(3.0, 4.0));
        Tensor tensor = new Tensor(data);
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("startIndices and endIndices must match the number of dimensions.");
        tensor.partial(new int[]{0}, new int[]{1});
    }

    @Test
    public void testPartialInvalidIndicesRange() {
        List<List<Double>> data = Arrays.asList(Arrays.asList(1.0, 2.0), Arrays.asList(3.0, 4.0));
        Tensor tensor = new Tensor(data);
        thrown.expect(IndexOutOfBoundsException.class);
        thrown.expectMessage("Index [2, 0] is out of bounds for shape [2, 2].");
        tensor.partial(new int[]{0, 0}, new int[]{3, 1});
    }

    @Test
    public void testToStringMethod() {
        List<List<Double>> data = Arrays.asList(Arrays.asList(1.0, 2.0), Arrays.asList(3.0, 4.0));
        Tensor tensor = new Tensor(data);
        assertEquals("Tensor(shape=[2, 2], data=[[1.0, 2.0], [3.0, 4.0]])", tensor.toString());
    }

    @Test
    public void testPrintDemo() {
        System.out.println("=== TENSOR DEMONSTRATION ===");
        
        // 1D Vectors
        Tensor v1 = new Tensor(Arrays.asList(1.0, 2.0, 3.0), new int[]{3});
        Tensor v2 = new Tensor(Arrays.asList(4.0, 5.0, 6.0), new int[]{3});
        Tensor hadamardVec = v1.hadamardProduct(v2);
        System.out.println("--- 1D Hadamard Product ---");
        System.out.println("Vector v1: " + v1);
        System.out.println("Vector v2: " + v2);
        System.out.println("Hadamard Product: " + hadamardVec);
        System.out.println();

        // 2D Matrices
        Tensor a = new Tensor(Arrays.asList(1.0, 2.0, 3.0, 4.0), new int[]{2, 2});
        Tensor b = new Tensor(Arrays.asList(5.0, 6.0, 7.0, 8.0), new int[]{2, 2});
        Tensor hadamardMat = a.hadamardProduct(b);
        Tensor matrixMul = a.multiply(b);
        System.out.println("--- 2D Hadamard Product and Matrix Multiplication ---");
        System.out.println("Matrix a: " + a);
        System.out.println("Matrix b: " + b);
        System.out.println("Hadamard Product: " + hadamardMat);
        System.out.println("Matrix Multiplication: " + matrixMul);
        System.out.println();

        // 3D Batch Matrices
        List<List<List<Double>>> data1 = Arrays.asList(
            Arrays.asList(Arrays.asList(1.0, 2.0), Arrays.asList(3.0, 4.0)),
            Arrays.asList(Arrays.asList(9.0, 10.0), Arrays.asList(11.0, 12.0))
        );
        List<List<List<Double>>> data2 = Arrays.asList(
            Arrays.asList(Arrays.asList(5.0, 6.0), Arrays.asList(7.0, 8.0)),
            Arrays.asList(Arrays.asList(13.0, 14.0), Arrays.asList(15.0, 16.0))
        );
        Tensor batchA = new Tensor(data1);
        Tensor batchB = new Tensor(data2);
        Tensor batchMatrixMul = batchA.multiply(batchB);
        System.out.println("--- 3D Batch Matrix Multiplication ---");
        System.out.println("Batch Matrix A: " + batchA);
        System.out.println("Batch Matrix B: " + batchB);
        System.out.println("Batch Matrix Multiplication: " + batchMatrixMul);
        System.out.println("=== END DEMONSTRATION ===");
    }
}