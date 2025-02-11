package Math;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class TensorTest {

    @Test
    public void testTensorInitialization() {
        List<List<Double>> data = Arrays.asList(
                Arrays.asList(1.0, 2.0, 3.0),
                Arrays.asList(4.0, 5.0, 6.0)
        );
        int[] shape = {2, 3};
        Tensor tensor = new Tensor(data, shape);

        assertArrayEquals(shape, tensor.getShape());
        assertEquals(1.0, tensor.get(new int[]{0, 0}), 0.0003f);
        assertEquals(6.0, tensor.get(new int[]{1, 2}), 0.0003f);
    }

    @Test
    public void testTensorInitializationWithInferredShape() {
        List<List<Double>> data = Arrays.asList(
                Arrays.asList(1.0, 2.0, 3.0),
                Arrays.asList(4.0, 5.0, 6.0)
        );
        Tensor tensor = new Tensor(data, null);

        assertArrayEquals(new int[]{2, 3}, tensor.getShape());
        assertEquals(1.0, tensor.get(new int[]{0, 0}), 0.0003f);
        assertEquals(6.0, tensor.get(new int[]{1, 2}), 0.0003f);
    }

    @Test
    public void testTensorReshape() {
        List<List<Double>> data = Arrays.asList(
                Arrays.asList(1.0, 2.0, 3.0),
                Arrays.asList(4.0, 5.0, 6.0)
        );
        Tensor tensor = new Tensor(data, new int[]{2, 3});
        Tensor reshapedTensor = tensor.reshape(new int[]{3, 2});

        assertArrayEquals(new int[]{3, 2}, reshapedTensor.getShape());
        assertEquals(1.0, reshapedTensor.get(new int[]{0, 0}), 0.0003f);
        assertEquals(6.0, reshapedTensor.get(new int[]{2, 1}), 0.0003f);
    }

    @Test
    public void testTensorTranspose() {
        List<List<Double>> data = Arrays.asList(
                Arrays.asList(1.0, 2.0, 3.0),
                Arrays.asList(4.0, 5.0, 6.0)
        );
        Tensor tensor = new Tensor(data, new int[]{2, 3});
        Tensor transposedTensor = tensor.transpose(null);

        assertArrayEquals(new int[]{3, 2}, transposedTensor.getShape());
        assertEquals(1.0, transposedTensor.get(new int[]{0, 0}), 0.0003f);
        assertEquals(6.0, transposedTensor.get(new int[]{2, 1}), 0.0003f);
    }

    @Test
    public void testTensorBroadcast() {
        List<Double> data = Arrays.asList(1.0, 2.0, 3.0);
        Tensor tensor = new Tensor(data, new int[]{3});
        Tensor broadcastedTensor = tensor.broadcastTo(new int[]{2, 3});

        assertArrayEquals(new int[]{2, 3}, broadcastedTensor.getShape());
        assertEquals(1.0, broadcastedTensor.get(new int[]{0, 0}), 0.0003f);
        assertEquals(3.0, broadcastedTensor.get(new int[]{1, 2}), 0.0003f);
    }

    @Test
    public void testTensorAddition() {
        List<List<Double>> data1 = Arrays.asList(
                Arrays.asList(1.0, 2.0, 3.0),
                Arrays.asList(4.0, 5.0, 6.0)
        );
        List<List<Double>> data2 = Arrays.asList(
                Arrays.asList(7.0, 8.0, 9.0),
                Arrays.asList(10.0, 11.0, 12.0)
        );
        Tensor tensor1 = new Tensor(data1, new int[]{2, 3});
        Tensor tensor2 = new Tensor(data2, new int[]{2, 3});
        Tensor resultTensor = tensor1.add(tensor2);

        assertArrayEquals(new int[]{2, 3}, resultTensor.getShape());
        assertEquals(8.0, resultTensor.get(new int[]{0, 0}), 0.0003f);
        assertEquals(18.0, resultTensor.get(new int[]{1, 2}), 0.0003f);
    }

    @Test
    public void testTensorSubtraction() {
        List<List<Double>> data1 = Arrays.asList(
                Arrays.asList(1.0, 2.0, 3.0),
                Arrays.asList(4.0, 5.0, 6.0)
        );
        List<List<Double>> data2 = Arrays.asList(
                Arrays.asList(7.0, 8.0, 9.0),
                Arrays.asList(10.0, 11.0, 12.0)
        );
        Tensor tensor1 = new Tensor(data1, new int[]{2, 3});
        Tensor tensor2 = new Tensor(data2, new int[]{2, 3});
        Tensor resultTensor = tensor1.subtract(tensor2);

        assertArrayEquals(new int[]{2, 3}, resultTensor.getShape());
        assertEquals(-6.0, resultTensor.get(new int[]{0, 0}), 0.0003f);
        assertEquals(-6.0, resultTensor.get(new int[]{1, 2}), 0.0003f);
    }

    @Test
    public void testTensorMultiplication() {
        List<List<Double>> data1 = Arrays.asList(
                Arrays.asList(1.0, 2.0, 3.0),
                Arrays.asList(4.0, 5.0, 6.0)
        );
        List<List<Double>> data2 = Arrays.asList(
                Arrays.asList(7.0, 8.0, 9.0),
                Arrays.asList(10.0, 11.0, 12.0)
        );
        Tensor tensor1 = new Tensor(data1, new int[]{2, 3});
        Tensor tensor2 = new Tensor(data2, new int[]{2, 3});
        Tensor resultTensor = tensor1.multiply(tensor2);

        assertArrayEquals(new int[]{2, 3}, resultTensor.getShape());
        assertEquals(7.0, resultTensor.get(new int[]{0, 0}), 0.0003f);
        assertEquals(72.0, resultTensor.get(new int[]{1, 2}), 0.0003f);
    }

    @Test
    public void testTensorDotProduct() {
        List<List<Double>> data1 = Arrays.asList(
                Arrays.asList(1.0, 2.0, 3.0),
                Arrays.asList(4.0, 5.0, 6.0)
        );
        List<List<Double>> data2 = Arrays.asList(
                Arrays.asList(7.0, 8.0),
                Arrays.asList(9.0, 10.0),
                Arrays.asList(11.0, 12.0)
        );
        Tensor tensor1 = new Tensor(data1, new int[]{2, 3});
        Tensor tensor2 = new Tensor(data2, new int[]{3, 2});
        Tensor resultTensor = tensor1.dot(tensor2);

        assertArrayEquals(new int[]{2, 2}, resultTensor.getShape());
        assertEquals(58.0, resultTensor.get(new int[]{0, 0}), 0.0003f);
        assertEquals(64.0, resultTensor.get(new int[]{0, 1}), 0.0003f);
        assertEquals(139.0, resultTensor.get(new int[]{1, 0}), 0.0003f);
        assertEquals(154.0, resultTensor.get(new int[]{1, 1}), 0.0003f);
    }
}