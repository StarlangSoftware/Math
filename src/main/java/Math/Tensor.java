package Math;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Tensor implements Serializable {
    /**
     * A class representing a multidimensional tensor that supports basic operations and broadcasting.
     */

    private final int[] shape;
    private final int[] strides;
    private final double[] data;

    /**
     * Initializes the tensor directly with a primitive double array and shape.
     * This is the fastest constructor used for internal mathematical operations.
     *
     * @param data  Primitive array representing the flattened tensor data.
     * @param shape The shape of the tensor.
     */
    public Tensor(double[] data, int[] shape) {
        this.shape = shape;
        this.data = data;
        int totalElements = this.data.length;
        if (computeNumElements(this.shape) != totalElements) {
            throw new IllegalArgumentException("Shape does not match the number of elements in data.");
        }
        this.strides = computeStrides(this.shape);
    }

    /**
     * Initializes the tensor with given nested list data and shape.
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
        int totalElements = computeNumElements(this.shape);
        this.data = new double[totalElements];
        if (flatten(data, this.data, new int[]{0}) != totalElements) {
            throw new IllegalArgumentException("Shape does not match the number of elements in data.");
        }
        this.strides = computeStrides(this.shape);
    }

    public Tensor(List<?> data) {
        this(data, null);
    }

    /**
     * Infers the shape of the tensor from nested lists.
     *
     * @param data Nested lists representing the tensor data.
     * @return Array representing the shape.
     */
    private int[] inferShape(List<?> data) {
        if (data instanceof List) {
            if (data.isEmpty()) {
                return new int[]{0};
            }
            Object firstElement = data.get(0);
            if (firstElement instanceof List) {
                List<?> firstList = (List<?>) firstElement;
                int[] restOfShape = inferShape(firstList);
                int[] shape = new int[restOfShape.length + 1];
                shape[0] = data.size();
                System.arraycopy(restOfShape, 0, shape, 1, restOfShape.length);
                return shape;
            } else {
                return new int[]{data.size()};
            }
        }
        return new int[]{};
    }

    /**
     * Concatenates two tensors into a one.
     *
     * @param tensor 2nd tensor for concatenation.
     * @param dimension to concatenate.
     * @return Concatenated {@link Tensor}.
     */
    public Tensor concat(Tensor tensor, int dimension) {
        if (dimension >= this.shape.length) {
            throw new IllegalArgumentException("Dimension out of bounds.");
        }
        if (this.shape.length != tensor.shape.length) {
            throw new IllegalArgumentException("Dimensions length do not match.");
        }
        int startIndex = 1;
        int endIndex1 = 1;
        int endIndex2 = 1;
        for (int i = 0; i < this.shape.length; i++) {
            if (i != dimension && this.shape[i] != tensor.shape[i]) {
                throw new IllegalArgumentException("Dimensions do not match.");
            }
            if (i >= dimension) {
                endIndex1 *= this.shape[i];
                endIndex2 *= tensor.shape[i];
            } else {
                startIndex *= this.shape[i];
            }
        }
        int[] newShape = new int[this.shape.length];
        for (int i = 0; i < this.shape.length; i++) {
            if (i == dimension) {
                newShape[i] = this.shape[i] + tensor.shape[i];
            } else {
                newShape[i] = this.shape[i];
            }
        }
        double[] newData = new double[startIndex * (endIndex1 + endIndex2)];
        int ptr = 0;
        for (int i = 0; i < startIndex; i++) {
            for (int j = 0; j < endIndex1; j++) {
                newData[ptr++] = this.data[i * endIndex1 + j];
            }
            for (int j = 0; j < endIndex2; j++) {
                newData[ptr++] = tensor.data[i * endIndex2 + j];
            }
        }
        return new Tensor(newData, newShape);
    }

    /**
     * Returns the sub-{@link Tensor} taking the given dimensions.
     *
     * @return a sub-{@link Tensor}.
     */
    public Tensor get(int[] dimensions) {
        if (dimensions.length >= this.shape.length) {
            throw new IllegalArgumentException("Dimensions exceeds or same as the tensor's dimension.");
        }
        for (int i = 0; i < dimensions.length; i++) {
            if (dimensions[i] >= this.shape[i]) {
                throw new IndexOutOfBoundsException("There is a dimension length exceed the tensor's dimension length.");
            }
        }
        int[] newShape = new int[this.shape.length - dimensions.length];
        System.arraycopy(this.shape, dimensions.length, newShape, 0, newShape.length);
        int i = 0, start = 0, end = data.length;
        do {
            int parts = (end - start) / this.shape[i];
            start += parts * dimensions[i];
            end = start + parts;
            i++;
        } while (i < dimensions.length);
        double[] newData = new double[end - start];
        System.arraycopy(this.data, start, newData, 0, end - start);
        return new Tensor(newData, newShape);
    }

    /**
     * Flattens nested lists directly into the primitive double array.
     *
     * @param dataList Nested lists representing the tensor data.
     * @param flatData The target primitive array.
     * @param index    An array of size 1 keeping track of the current insertion index.
     */
    private int flatten(List<?> dataList, double[] flatData, int[] index) {
        int totalElements = 0;
        for (Object item : dataList) {
            if (item instanceof List) {
                totalElements += flatten((List<?>) item, flatData, index);
            } else if (item instanceof Number) {
                totalElements++;
                flatData[index[0]++] = ((Number) item).doubleValue();
            }
        }
        return totalElements;
    }

    /**
     * Computes the strides for each dimension based on the shape.
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
     */
    private int computeNumElements(int[] shape) {
        int product = 1;
        for (int dim : shape) {
            product *= dim;
        }
        return product;
    }

    /**
     * Validates that indices are within the valid range for each dimension.
     */
    private void validateIndices(int[] indices) {
        if (indices.length != shape.length) {
            throw new IndexOutOfBoundsException("Expected " + shape.length + " indices but got " + indices.length + ".");
        }
        for (int i = 0; i < indices.length; i++) {
            if (!(0 <= indices[i] && indices[i] < shape[i])) {
                throw new IndexOutOfBoundsException("Index " + Arrays.toString(indices) + " is out of bounds for shape " + Arrays.toString(shape) + ".");
            }
        }
    }

    /**
     * Retrieves the value at the given indices.
     */
    public double getValue(int[] indices) {
        validateIndices(indices);
        int flatIndex = 0;
        for (int i = 0; i < indices.length; i++) {
            flatIndex += indices[i] * strides[i];
        }
        return data[flatIndex];
    }

    /**
     * Sets the value at the given indices.
     */
    public void set(int[] indices, double value) {
        validateIndices(indices);
        int flatIndex = 0;
        for (int i = 0; i < indices.length; i++) {
            flatIndex += indices[i] * strides[i];
        }
        data[flatIndex] = value;
    }

    /**
     * Reshapes the tensor to the specified new shape. Memory is shared!
     */
    public Tensor reshape(int[] newShape) {
        if (computeNumElements(newShape) != computeNumElements(this.shape)) {
            throw new IllegalArgumentException("Total number of elements must remain the same.");
        }
        return new Tensor(this.data, newShape);
    }

    /**
     * Transposes the tensor according to the specified axes.
     */
    public Tensor transpose(int[] axes) {
        if (axes == null) {
            axes = new int[shape.length];
            for (int i = 0; i < shape.length; i++) {
                axes[i] = shape.length - 1 - i;
            }
        }
        List<Integer> sortedAxes = Arrays.stream(axes).boxed().sorted().collect(Collectors.toList());
        List<Integer> expectedAxes = new ArrayList<>();
        for (int i = 0; i < shape.length; i++) {
            expectedAxes.add(i);
        }
        if (!sortedAxes.equals(expectedAxes)) {
            throw new IllegalArgumentException("Invalid transpose axes.");
        }
        int[] newShape = new int[shape.length];
        for (int i = 0; i < axes.length; i++) {
            newShape[i] = shape[axes[i]];
        }
        double[] flattenedData = transposeFlattenedData(axes, newShape);
        return new Tensor(flattenedData, newShape);
    }

    /**
     * Rearranges the flattened data for transposition.
     */
    private double[] transposeFlattenedData(int[] axes, int[] newShape) {
        int[] newStrides = computeStrides(newShape);
        int numElements = computeNumElements(newShape);
        double[] flattenedData = new double[numElements];
        for (int i = 0; i < numElements; i++) {
            int[] newIndices = unflattenIndex(i, newStrides);
            int[] originalIndices = new int[shape.length];
            for (int dim = 0; dim < shape.length; dim++) {
                int originalDimIndex = -1;
                for (int j = 0; j < axes.length; j++) {
                    if (axes[j] == dim) {
                        originalDimIndex = j;
                        break;
                    }
                }
                if (originalDimIndex != -1) {
                    originalIndices[dim] = newIndices[originalDimIndex];
                }
            }
            flattenedData[i] = getValue(originalIndices);
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

    private int[] broadcastShape(int[] shape1, int[] shape2) {
        int[] reversedShape1 = new int[shape1.length];
        int[] reversedShape2 = new int[shape2.length];
        for (int i = 0; i < shape1.length; i++) {
            reversedShape1[i] = shape1[shape1.length - 1 - i];
        }
        for (int i = 0; i < shape2.length; i++) {
            reversedShape2[i] = shape2[shape2.length - 1 - i];
        }
        List<Integer> resultShape = new ArrayList<>();
        int maxLength = Math.max(shape1.length, shape2.length);
        for (int i = 0; i < maxLength; i++) {
            int dim1 = (i < reversedShape1.length) ? reversedShape1[i] : 1;
            int dim2 = (i < reversedShape2.length) ? reversedShape2[i] : 1;
            if (dim1 == dim2) {
                resultShape.add(dim1);
            } else if (dim1 == 1 || dim2 == 1) {
                resultShape.add(Math.max(dim1, dim2));
            } else {
                throw new IllegalArgumentException("Shapes " + Arrays.toString(shape1) + " and " + Arrays.toString(shape2) + " are not broadcastable");
            }
        }
        for (int i = reversedShape2.length; i < reversedShape1.length; i++) {
            resultShape.add(reversedShape1[i]);
        }
        for (int i = reversedShape1.length; i < reversedShape2.length; i++) {
            resultShape.add(reversedShape2[i]);
        }
        int[] finalShape = new int[resultShape.size()];
        for (int i = 0; i < resultShape.size(); i++) {
            finalShape[i] = resultShape.get(resultShape.size() - 1 - i);
        }
        return finalShape;
    }

    public Tensor broadcastTo(int[] targetShape) {
        int diff = targetShape.length - shape.length;
        int[] expandedShape = new int[targetShape.length];
        for (int i = 0; i < diff; i++) {
            expandedShape[i] = 1;
        }
        System.arraycopy(shape, 0, expandedShape, diff, shape.length);
        for (int i = 0; i < targetShape.length; i++) {
            if (!(expandedShape[i] == targetShape[i] || expandedShape[i] == 1)) {
                throw new IllegalArgumentException("Cannot broadcast shape " + Arrays.toString(shape) + " to " + Arrays.toString(targetShape));
            }
        }
        int numElements = computeNumElements(targetShape);
        double[] newData = new double[numElements];
        int[] targetStrides = computeStrides(targetShape);
        for (int i = 0; i < numElements; i++) {
            int[] indices = unflattenIndex(i, targetStrides);
            int[] originalIndices = new int[indices.length];
            for (int j = 0; j < indices.length; j++) {
                originalIndices[j] = (expandedShape[j] > 1) ? indices[j] : 0;
            }
            newData[i] = getValue(originalIndices);
        }
        return new Tensor(newData, targetShape);
    }

    public Tensor add(Tensor other) {
        int[] broadcastShape = broadcastShape(this.shape, other.shape);
        Tensor tensor1 = this.broadcastTo(broadcastShape);
        Tensor tensor2 = other.broadcastTo(broadcastShape);
        int numElements = computeNumElements(broadcastShape);
        double[] resultData = new double[numElements];
        for (int i = 0; i < numElements; i++) {
            resultData[i] = tensor1.data[i] + tensor2.data[i];
        }
        return new Tensor(resultData, broadcastShape);
    }

    public Tensor subtract(Tensor other) {
        int[] broadcastShape = broadcastShape(this.shape, other.shape);
        Tensor tensor1 = this.broadcastTo(broadcastShape);
        Tensor tensor2 = other.broadcastTo(broadcastShape);
        int numElements = computeNumElements(broadcastShape);
        double[] resultData = new double[numElements];
        for (int i = 0; i < numElements; i++) {
            resultData[i] = tensor1.data[i] - tensor2.data[i];
        }
        return new Tensor(resultData, broadcastShape);
    }

    public Tensor hadamardProduct(Tensor other) {
        int[] broadcastShape = broadcastShape(this.shape, other.shape);
        Tensor tensor1 = this.broadcastTo(broadcastShape);
        Tensor tensor2 = other.broadcastTo(broadcastShape);
        int numElements = computeNumElements(broadcastShape);
        double[] resultData = new double[numElements];
        for (int i = 0; i < numElements; i++) {
            resultData[i] = tensor1.data[i] * tensor2.data[i];
        }
        return new Tensor(resultData, broadcastShape);
    }

    public Tensor multiply(Tensor other) {
        if (this.shape.length < 2 || other.shape.length < 2) {
            throw new IllegalArgumentException("Shapes " + Arrays.toString(this.shape) + " and " + Arrays.toString(other.shape) + " are not aligned for multiplication.");
        }
        if (this.shape[this.shape.length - 1] != other.shape[other.shape.length - 2]) {
            throw new IllegalArgumentException("Shapes " + Arrays.toString(this.shape) + " and " + Arrays.toString(other.shape) + " are not aligned for multiplication.");
        }
        int[] batchShape = Arrays.copyOfRange(this.shape, 0, this.shape.length - 2);
        int m = this.shape[this.shape.length - 2];
        int k1 = this.shape[this.shape.length - 1];
        int k2 = other.shape[other.shape.length - 2];
        int n = other.shape[other.shape.length - 1];
        if (k1 != k2) {
            throw new IllegalArgumentException("Inner dimensions must match for matrix multiplication.");
        }
        int[] otherBatchShape = Arrays.copyOfRange(other.shape, 0, other.shape.length - 2);
        int[] broadcastShape;
        Tensor selfBroadcasted;
        Tensor otherBroadcasted;
        if (!Arrays.equals(batchShape, otherBatchShape)) {
            broadcastShape = broadcastShape(batchShape, otherBatchShape);
            int[] selfBroadcastShape = concat(broadcastShape, new int[]{m, k1});
            int[] otherBroadcastShape = concat(broadcastShape, new int[]{k2, n});
            selfBroadcasted = this.broadcastTo(selfBroadcastShape);
            otherBroadcasted = other.broadcastTo(otherBroadcastShape);
        } else {
            broadcastShape = batchShape;
            selfBroadcasted = this;
            otherBroadcasted = other;
        }
        int[] resultShape = concat(broadcastShape, new int[]{m, n});
        int numElements = computeNumElements(resultShape);
        double[] resultData = new double[numElements];
        int[] resultStrides = computeStrides(resultShape);
        for (int i = 0; i < numElements; i++) {
            int[] indices = unflattenIndex(i, resultStrides);
            int[] batchIdx = Arrays.copyOfRange(indices, 0, indices.length - 2);
            int row = indices[indices.length - 2];
            int col = indices[indices.length - 1];
            double sumResult = 0;
            for (int k = 0; k < k1; k++) {
                int[] aIdx = concat(batchIdx, new int[]{row, k});
                int[] bIdx = concat(batchIdx, new int[]{k, col});
                sumResult += selfBroadcasted.getValue(aIdx) * otherBroadcasted.getValue(bIdx);
            }
            resultData[i] = sumResult;
        }
        return new Tensor(resultData, resultShape);
    }

    private int[] concat(int[] a, int[] b) {
        int[] result = new int[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public Tensor partial(int[] startIndices, int[] endIndices) {
        if (startIndices.length != shape.length || endIndices.length != shape.length) {
            throw new IllegalArgumentException("startIndices and endIndices must match the number of dimensions.");
        }
        int[] newShape = new int[shape.length];
        for (int i = 0; i < shape.length; i++) {
            newShape[i] = endIndices[i] - startIndices[i];
            if (newShape[i] < 0) {
                throw new IllegalArgumentException("End index must be greater than or equal to start index for dimension " + i);
            }
        }
        int numElements = computeNumElements(newShape);
        double[] subData = new double[numElements];
        int[] newStrides = computeStrides(newShape);
        for (int i = 0; i < numElements; i++) {
            int[] subIndices = unflattenIndex(i, newStrides);
            int[] originalIndices = new int[shape.length];
            for (int j = 0; j < shape.length; j++) {
                originalIndices[j] = startIndices[j] + subIndices[j];
            }
            subData[i] = getValue(originalIndices);
        }
        return new Tensor(subData, newShape);
    }

    @Override
    public String toString() {
        Object formattedData = formatTensor(data, shape, 0);
        return "Tensor(shape=" + Arrays.toString(shape) + ", data=" + formattedData + ")";
    }

    private Object formatTensor(double[] dataArray, int[] shape, int offset) {
        if (shape.length == 0) return new ArrayList<>();
        if (shape.length == 1) {
            List<Double> row = new ArrayList<>(shape[0]);
            for (int i = 0; i < shape[0]; i++) {
                row.add(dataArray[offset + i]);
            }
            return row;
        }
        int stride = computeNumElements(Arrays.copyOfRange(shape, 1, shape.length));
        List<Object> formattedList = new ArrayList<>(shape[0]);
        for (int i = 0; i < shape[0]; i++) {
            formattedList.add(formatTensor(dataArray, Arrays.copyOfRange(shape, 1, shape.length), offset + (i * stride)));
        }
        return formattedList;
    }

    public int[] getShape() {
        return shape;
    }

    /**
     * Returns the primitive double array holding the tensor data.
     * WARNING: Be cautious not to mutate this array directly outside the class if you want to maintain immutability.
     */
    public double[] getData() {
        return data;
    }
}