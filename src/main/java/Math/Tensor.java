package Math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tensor {
    private int[] shape;
    private int[] strides;
    private List<Double> data;

    /**
     * Initializes the tensor with given data and shape.
     *
     * @param data  Nested lists representing the tensor data.
     * @param shape The shape of the tensor. If null, the shape is inferred from the data.
     */
    public Tensor(List<?> data, int[] shape) {
        if (shape == null) {
            this.shape = inferShape(data);
        } else {
            this.shape = shape;
        }

        this.data = flatten(data);
        int totalElements = this.data.size();

        if (computeNumElements(this.shape) != totalElements) {
            throw new IllegalArgumentException("Shape does not match the number of elements in data.");
        }

        this.strides = computeStrides(this.shape);
    }

    /**
     * Creates a tensor filled with zeros.
     *
     * @param shape The shape of the tensor.
     * @return New tensor filled with zeros.
     */
    public static Tensor zeros(int[] shape) {
        int totalElements = computeNumElementsStatic(shape);
        List<Double> data = new ArrayList<>(totalElements);
        for (int i = 0; i < totalElements; i++) {
            data.add(0.0);
        }
        return new Tensor(data, shape);
    }

    /**
     * Creates a tensor filled with ones.
     *
     * @param shape The shape of the tensor.
     * @return New tensor filled with ones.
     */
    public static Tensor ones(int[] shape) {
        int totalElements = computeNumElementsStatic(shape);
        List<Double> data = new ArrayList<>(totalElements);
        for (int i = 0; i < totalElements; i++) {
            data.add(1.0);
        }
        return new Tensor(data, shape);
    }

    /**
     * Creates a tensor filled with random values.
     *
     * @param shape The shape of the tensor.
     * @return New tensor filled with random values.
     */
    public static Tensor random(int[] shape) {
        int totalElements = computeNumElementsStatic(shape);
        List<Double> data = new ArrayList<>(totalElements);
        for (int i = 0; i < totalElements; i++) {
            data.add(Math.random());
        }
        return new Tensor(data, shape);
    }

    /**
     * Computes the total number of elements in the tensor based on its shape.
     *
     * @param shape Array representing the tensor shape.
     * @return Total number of elements.
     */
    private static int computeNumElementsStatic(int[] shape) {
        int product = 1;
        for (int dim : shape) {
            product *= dim;
        }
        return product;
    }

    /**
     * Infers the shape of the tensor from nested lists.
     *
     * @param data Nested lists representing the tensor data.
     * @return Array representing the shape.
     */
    private int[] inferShape(List<?> data) {
        if (data.isEmpty()) {
            return new int[]{0};
        }
    
        // Check if the first element is a list
        if (data.get(0) instanceof List) {
            int[] subShape = inferShape((List<?>) data.get(0));
            int[] shape = new int[subShape.length + 1];
            shape[0] = data.size();
            System.arraycopy(subShape, 0, shape, 1, subShape.length);
            return shape;
        } else {
            // If the first element is not a list, we've reached the innermost level
            return new int[]{data.size()};
        }
    }

    /**
     * Flattens nested lists into a single list.
     *
     * @param data Nested lists representing the tensor data.
     * @return Flattened list of tensor elements.
     */
    private List<Double> flatten(List<?> data) {
        List<Double> flattened = new ArrayList<>();
        for (Object item : data) {
            if (item instanceof List) {
                flattened.addAll(flatten((List<?>) item));
            } else {
                flattened.add((Double) item);
            }
        }
        return flattened;
    }

    /**
     * Computes the strides for each dimension based on the shape.
     *
     * @param shape Array representing the tensor shape.
     * @return Array representing the strides.
     */
    private int[] computeStrides(int[] shape) {
        int[] strides = new int[shape.length];
        int product = 1;
        for (int i = shape.length - 1; i >= 0; i--) {
            strides[i] = product;
            product *= shape[i];
        }
        return strides;
    }

    /**
     * Computes the total number of elements in the tensor based on its shape.
     *
     * @param shape Array representing the tensor shape.
     * @return Total number of elements.
     */
    private int computeNumElements(int[] shape) {
        int product = 1;
        for (int dim : shape) {
            product *= dim;
        }
        return product;
    }

    /**
     * Retrieves the value at the given indices.
     *
     * @param indices Array of indices specifying the position.
     * @return Value at the specified position.
     */
    public double get(int[] indices) {
        if (indices.length != shape.length) {
            throw new IndexOutOfBoundsException("Number of indices must match the tensor dimensions.");
        }
        int flatIndex = 0;
        for (int i = 0; i < indices.length; i++) {
            flatIndex += indices[i] * strides[i];
        }
        return data.get(flatIndex);
    }

    /**
     * Sets the value at the given indices.
     *
     * @param indices Array of indices specifying the position.
     * @param value   Value to set at the specified position.
     */
    public void set(int[] indices, double value) {
        if (indices.length != shape.length) {
            throw new IndexOutOfBoundsException("Number of indices must match the tensor dimensions.");
        }
        int flatIndex = 0;
        for (int i = 0; i < indices.length; i++) {
            flatIndex += indices[i] * strides[i];
        }
        data.set(flatIndex, value);
    }

    /**
     * Gets the shape of the tensor.
     * @return An array containing the dimensions of the tensor.
     */
    public int[] getShape() {
        return shape.clone();
    }

    /**
     * Reshapes the tensor to the specified new shape.
     *
     * @param newShape Array representing the new shape.
     * @return New tensor with the specified shape.
     */
    public Tensor reshape(int[] newShape) {
        if (computeNumElements(newShape) != computeNumElements(shape)) {
            throw new IllegalArgumentException("Total number of elements must remain the same.");
        }
        return new Tensor(data, newShape);
    }

    /**
     * Transposes the tensor according to the specified axes.
     *
     * @param axes Array representing the order of axes. If null, reverses the axes.
     * @return New tensor with transposed axes.
     */
    public Tensor transpose(int[] axes) {
        if (axes == null) {
            axes = new int[shape.length];
            for (int i = 0; i < shape.length; i++) {
                axes[i] = shape.length - 1 - i;
            }
        }
        if (!isValidAxes(axes)) {
            throw new IllegalArgumentException("Invalid transpose axes.");
        }

        int[] newShape = new int[shape.length];
        for (int i = 0; i < shape.length; i++) {
            newShape[i] = shape[axes[i]];
        }

        List<Double> flattenedData = transposeFlattenedData(axes, newShape);
        return new Tensor(flattenedData, newShape);
    }

    private boolean isValidAxes(int[] axes) {
        int[] sortedAxes = Arrays.copyOf(axes, axes.length);
        Arrays.sort(sortedAxes);
        for (int i = 0; i < sortedAxes.length; i++) {
            if (sortedAxes[i] != i) {
                return false;
            }
        }
        return true;
    }

    private List<Double> transposeFlattenedData(int[] axes, int[] newShape) {
        int[] newStrides = computeStrides(newShape);
        List<Double> flattenedData = new ArrayList<>();

        for (int i = 0; i < computeNumElements(newShape); i++) {
            int[] newIndices = unflattenIndex(i, newStrides);
            int[] originalIndices = new int[shape.length];
            for (int j = 0; j < shape.length; j++) {
                originalIndices[j] = newIndices[axes[j]];
            }
            flattenedData.add(get(originalIndices));
        }

        return flattenedData;
    }

    private int[] unflattenIndex(int flatIndex, int[] strides) {
        int[] indices = new int[strides.length];
        for (int i = 0; i < strides.length; i++) {
            indices[i] = flatIndex / strides[i];
            flatIndex %= strides[i];
        }
        return indices;
    }

    /**
     * Broadcasts the tensor to the specified target shape.
     *
     * @param targetShape Array representing the target shape.
     * @return New tensor with the target shape.
     */
    public Tensor broadcastTo(int[] targetShape) {
        int[] expandedShape = new int[targetShape.length];
        Arrays.fill(expandedShape, 1);
        System.arraycopy(shape, 0, expandedShape, targetShape.length - shape.length, shape.length);

        for (int i = 0; i < expandedShape.length; i++) {
            if (expandedShape[i] != targetShape[i] && expandedShape[i] != 1) {
                throw new IllegalArgumentException("Cannot broadcast shape " + Arrays.toString(shape) + " to " + Arrays.toString(targetShape));
            }
        }

        List<Double> newData = new ArrayList<>();
        for (int i = 0; i < computeNumElements(targetShape); i++) {
            int[] indices = unflattenIndex(i, computeStrides(targetShape));
            int[] originalIndices = new int[shape.length];
            for (int j = 0; j < shape.length; j++) {
                originalIndices[j] = indices[j + (targetShape.length - shape.length)];
            }
            newData.add(get(originalIndices));
        }

        return new Tensor(newData, targetShape);
    }

    /**
     * Adds two tensors element-wise with broadcasting.
     *
     * @param other The other tensor to add.
     * @return New tensor with the result of the addition.
     */
    public Tensor add(Tensor other) {
        int[] broadcastShape = broadcastShape(shape, other.shape);
        Tensor tensor1 = broadcastTo(broadcastShape);
        Tensor tensor2 = other.broadcastTo(broadcastShape);
        List<Double> resultData = new ArrayList<>();

        for (int i = 0; i < computeNumElements(broadcastShape); i++) {
            resultData.add(tensor1.get(unflattenIndex(i, tensor1.strides)) +
                    tensor2.get(unflattenIndex(i, tensor2.strides)));
        }

        return new Tensor(resultData, broadcastShape);
    }

    /**
     * Subtracts one tensor from another element-wise with broadcasting.
     *
     * @param other The other tensor to subtract.
     * @return New tensor with the result of the subtraction.
     */
    public Tensor subtract(Tensor other) {
        int[] broadcastShape = broadcastShape(shape, other.shape);
        Tensor tensor1 = broadcastTo(broadcastShape);
        Tensor tensor2 = other.broadcastTo(broadcastShape);
        List<Double> resultData = new ArrayList<>();

        for (int i = 0; i < computeNumElements(broadcastShape); i++) {
            resultData.add(tensor1.get(unflattenIndex(i, tensor1.strides)) -
                    tensor2.get(unflattenIndex(i, tensor2.strides)));
        }

        return new Tensor(resultData, broadcastShape);
    }

    /**
     * Multiplies two tensors element-wise with broadcasting.
     *
     * @param other The other tensor to multiply.
     * @return New tensor with the result of the multiplication.
     */
    public Tensor multiply(Tensor other) {
        int[] broadcastShape = broadcastShape(shape, other.shape);
        Tensor tensor1 = broadcastTo(broadcastShape);
        Tensor tensor2 = other.broadcastTo(broadcastShape);
        List<Double> resultData = new ArrayList<>();

        for (int i = 0; i < computeNumElements(broadcastShape); i++) {
            resultData.add(tensor1.get(unflattenIndex(i, tensor1.strides)) *
                    tensor2.get(unflattenIndex(i, tensor2.strides)));
        }

        return new Tensor(resultData, broadcastShape);
    }

    /**
     * Computes the dot product of two tensors.
     *
     * @param other The other tensor to compute the dot product with.
     * @return New tensor with the result of the dot product.
     */
    public Tensor dot(Tensor other) {
        if (shape[shape.length - 1] != other.shape[other.shape.length - 2]) {
            throw new IllegalArgumentException("Shapes " + Arrays.toString(shape) + " and " + Arrays.toString(other.shape) + " are not aligned for dot product.");
        }

        int[] resultShape = new int[shape.length - 1 + other.shape.length - 1];
        System.arraycopy(shape, 0, resultShape, 0, shape.length - 1);
        System.arraycopy(other.shape, other.shape.length - 1, resultShape, shape.length - 1, other.shape.length - 1);

        List<Double> resultData = new ArrayList<>();
        for (int i = 0; i < computeNumElements(resultShape); i++) {
            int[] resultIndices = unflattenIndex(i, computeStrides(resultShape));
            double dotProduct = 0;

            for (int k = 0; k < shape[shape.length - 1]; k++) {
                int[] aIndices = new int[shape.length];
                System.arraycopy(resultIndices, 0, aIndices, 0, shape.length - 1);
                aIndices[shape.length - 1] = k;

                int[] bIndices = new int[other.shape.length];
                bIndices[0] = k;
                System.arraycopy(resultIndices, shape.length - 1, bIndices, 1, other.shape.length - 1);

                dotProduct += get(aIndices) * other.get(bIndices);
            }

            resultData.add(dotProduct);
        }

        return new Tensor(resultData, resultShape);
    }

    /**
     * Determines the broadcasted shape of two tensors.
     *
     * @param shape1 Array representing the first tensor shape.
     * @param shape2 Array representing the second tensor shape.
     * @return Array representing the broadcasted shape.
     */
    private int[] broadcastShape(int[] shape1, int[] shape2) {
        int maxLength = Math.max(shape1.length, shape2.length);
        int[] reversedShape1 = reverse(shape1);
        int[] reversedShape2 = reverse(shape2);
        int[] resultShape = new int[maxLength];

        for (int i = 0; i < maxLength; i++) {
            int dim1 = i < reversedShape1.length ? reversedShape1[i] : 1;
            int dim2 = i < reversedShape2.length ? reversedShape2[i] : 1;

            if (dim1 == dim2) {
                resultShape[i] = dim1;
            } else if (dim1 == 1 || dim2 == 1) {
                resultShape[i] = Math.max(dim1, dim2);
            } else {
                throw new IllegalArgumentException("Shapes " + Arrays.toString(shape1) + " and " + Arrays.toString(shape2) + " are not broadcastable.");
            }
        }

        return reverse(resultShape);
    }

    private int[] reverse(int[] array) {
        int[] reversed = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            reversed[i] = array[array.length - 1 - i];
        }
        return reversed;
    }

    @Override
    public String toString() {
        return "Tensor(shape=" + Arrays.toString(shape) + ", data=" + formatTensor(data, shape) + ")";
    }

    private Object formatTensor(List<Double> data, int[] shape) {
        if (shape.length == 1) {
            return data;
        }
        int stride = computeNumElements(Arrays.copyOfRange(shape, 1, shape.length));
        List<Object> formattedData = new ArrayList<>();
        for (int i = 0; i < shape[0]; i++) {
            formattedData.add(formatTensor(data.subList(i * stride, (i + 1) * stride), Arrays.copyOfRange(shape, 1, shape.length)));
        }
        return formattedData;
    }
}