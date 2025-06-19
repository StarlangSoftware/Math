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
    public void testMultiplySameShape() {
        List<Double> data1 = Arrays.asList(1.0, 2.0, 3.0, 4.0);
        Tensor tensor1 = new Tensor(data1, new int[]{2, 2});
        List<Double> data2 = Arrays.asList(5.0, 6.0, 7.0, 8.0);
        Tensor tensor2 = new Tensor(data2, new int[]{2, 2});
        Tensor product = tensor1.multiply(tensor2);
        assertArrayEquals(new int[]{2, 2}, product.getShape());
        assertEquals(Arrays.asList(5.0, 12.0, 21.0, 32.0), product.getData());
    }

    @Test
    public void testMultiplyWithBroadcasting() {
        List<Double> data1 = Arrays.asList(1.0, 2.0);
        Tensor tensor1 = new Tensor(data1, new int[]{1, 2});
        List<Double> data2 = Arrays.asList(3.0, 4.0);
        Tensor tensor2 = new Tensor(data2, new int[]{2, 1});
        Tensor product = tensor1.multiply(tensor2);
        assertArrayEquals(new int[]{2, 2}, product.getShape());
        assertEquals(Arrays.asList(3.0, 6.0, 4.0, 8.0), product.getData());
    }

    @Test
    public void testDotProductValid() {
        List<List<Double>> data1 = Arrays.asList(Arrays.asList(1.0, 2.0), Arrays.asList(3.0, 4.0));
        Tensor tensor1 = new Tensor(data1);
        List<List<Double>> data2 = Arrays.asList(Arrays.asList(5.0, 6.0), Arrays.asList(7.0, 8.0));
        Tensor tensor2 = new Tensor(data2);
        Tensor dotProduct = tensor1.dot(tensor2);
        assertArrayEquals(new int[]{2, 2}, dotProduct.getShape());
        assertEquals(Arrays.asList(19.0, 22.0, 43.0, 50.0), dotProduct.getData());
    }

    @Test
    public void testDotProductInvalidShapes() {
        List<Double> data1 = Arrays.asList(1.0, 2.0, 3.0);
        Tensor tensor1 = new Tensor(data1, new int[]{3});
        List<List<Double>> data2 = Arrays.asList(Arrays.asList(4.0, 5.0), Arrays.asList(6.0, 7.0));
        Tensor tensor2 = new Tensor(data2);
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Shapes [3] and [2, 2] are not aligned for dot product.");
        tensor1.dot(tensor2);
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
}