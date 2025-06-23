package Math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Tensor {
    /**
     * A class representing a multi-dimensional tensor that supports basic operations and broadcasting.
     */

    private final int[] shape;
    private final int[] strides;
    private final ArrayList<Double> data;

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
                // If the first element is not a list, then it's the innermost dimension
                return new int[]{data.size()};
            }
        }
        return new int[]{};
    }

    public Tensor concat(Tensor tensor) {
        if (this.shape.length != tensor.shape.length) {
            throw new IllegalArgumentException("Dimensions length do not match.");
        }
        int endIndex = 0;
        for (int i = 0; i < this.shape.length - 1; i++) {
            if (this.shape[i] != tensor.shape[i]) {
                throw new IllegalArgumentException("Dimensions do not match.");
            }
            endIndex += this.shape[i];
        }
        int[] newShape = new int[this.shape.length];
        if (this.shape.length - 1 >= 0) System.arraycopy(this.shape, 0, newShape, 0, this.shape.length - 1);
        newShape[this.shape.length - 1] = this.shape[this.shape.length - 1] + tensor.shape[tensor.shape.length - 1];
        List<Double> newList = new ArrayList<>();
        for (int i = 0; i < endIndex; i++) {
            for (int j = 0; j < this.shape[this.shape.length - 1]; j++) {
                newList.add(this.data.get(i * this.shape[this.shape.length - 1] + j));
            }
            for (int j = 0; j < tensor.shape[tensor.shape.length - 1]; j++) {
                newList.add(tensor.data.get(i * tensor.shape[tensor.shape.length - 1] + j));
            }
        }
        return new Tensor(newList, newShape);
    }

    public Tensor get(int[] dimensions) {
        if (dimensions.length >= this.shape.length) {
            throw new IllegalArgumentException("dimensions exceeds or same as the tensor's dimension.");
        }
        for (int i = 0; i < dimensions.length; i++) {
            if (dimensions[i] >= this.shape[i]) {
                throw new IndexOutOfBoundsException("there is a dimension length exceed the tensor's dimension length.");
            }
        }
        int[] newShape = new int[this.shape.length - dimensions.length];
        System.arraycopy(this.shape, dimensions.length, newShape, 0, newShape.length);
        int i = 0, start = 0, end = data.size();
        do {
            int parts = (end - start) / this.shape[i];
            start += parts * dimensions[i];
            end = start + parts;
            i++;
        } while (i < dimensions.length);
        return new Tensor(data.subList(start, end),  newShape);
    }

    /**
     * Flattens nested lists into a single list.
     *
     * @param data Nested lists representing the tensor data.
     * @return Flattened list of tensor elements.
     */
    private ArrayList<Double> flatten(List<?> data) {
        ArrayList<Double> flattenedList = new ArrayList<>();
        if (data instanceof List) {
            for (Object item : data) {
                if (item instanceof List) {
                    flattenedList.addAll(flatten((List<?>) item));
                } else if (item instanceof Number) {
                    flattenedList.add(((Number) item).doubleValue());
                }
            }
        } else if (data instanceof Number) {
            flattenedList.add(((Number) data).doubleValue());
        }
        return flattenedList;
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
     * Validates that indices are within the valid range for each dimension.
     * @param indices Array of indices specifying the position.
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
     *
     * @param indices Array of indices specifying the position.
     * @return Value at the specified position.
     */
    public double getValue(int[] indices) {
        validateIndices(indices); // Ensure indices are valid
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
        validateIndices(indices); // Ensure indices are valid
        int flatIndex = 0;
        for (int i = 0; i < indices.length; i++) {
            flatIndex += indices[i] * strides[i];
        }
        data.set(flatIndex, value);
    }

    /**
     * Reshapes the tensor to the specified new shape.
     *
     * @param newShape Array representing the new shape.
     * @return New tensor with the specified shape.
     */
    public Tensor reshape(int[] newShape) {
        if (computeNumElements(newShape) != computeNumElements(this.shape)) {
            throw new IllegalArgumentException("Total number of elements must remain the same.");
        }
        return new Tensor(this.data, newShape);
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
        List<Double> flattenedData = transposeFlattenedData(axes, newShape);
        return new Tensor(flattenedData, newShape);
    }

    /**
     * Rearranges the flattened data for transposition.
     *
     * @param axes     Array representing the order of axes.
     * @param newShape Array representing the new shape.
     * @return Flattened list of transposed data.
     */
    private List<Double> transposeFlattenedData(int[] axes, int[] newShape) {
        int[] newStrides = computeStrides(newShape);
        List<Double> flattenedData = new ArrayList<>();
        int numElements = computeNumElements(newShape);
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
            flattenedData.add(getValue(originalIndices));
        }
        return flattenedData;
    }

    /**
     * Converts a flat index to multi-dimensional indices based on strides.
     *
     * @param flatIndex The flat index to convert.
     * @param strides   Array representing the strides.
     * @return Array of multi-dimensional indices.
     */
    private int[] unflattenIndex(int flatIndex, int[] strides) {
        int[] indices = new int[strides.length];
        for (int i = 0; i < strides.length; i++) {
            indices[i] = flatIndex / strides[i];
            flatIndex %= strides[i];
        }
        return indices;
    }

    /**
     * Determines the broadcasted shape of two tensors.
     *
     * @param shape1 Array representing the first tensor shape.
     * @param shape2 Array representing the second tensor shape.
     * @return Array representing the broadcasted shape.
     */
    private int[] broadcastShape(int[] shape1, int[] shape2) {
        int len1 = shape1.length;
        int len2 = shape2.length;
        int maxLength = Math.max(len1, len2);
        int[] resultShape = new int[maxLength];
        for (int i = 0; i < maxLength; i++) {
            int dim1 = (i < len1) ? shape1[len1 - 1 - i] : 1;
            int dim2 = (i < len2) ? shape2[len2 - 1 - i] : 1;

            if (dim1 == dim2) {
                resultShape[maxLength - 1 - i] = dim1;
            } else if (dim1 == 1 || dim2 == 1) {
                resultShape[maxLength - 1 - i] = Math.max(dim1, dim2);
            } else {
                throw new IllegalArgumentException("Shapes " + Arrays.toString(shape1) + " and " + Arrays.toString(shape2) + " are not broadcastable");
            }
        }
        return resultShape;
    }

    /**
     * Broadcasts the tensor to the specified target shape.
     *
     * @param targetShape Array representing the target shape.
     * @return New tensor with the target shape.
     */
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
        List<Double> newData = new ArrayList<>();
        int numElements = computeNumElements(targetShape);
        int[] targetStrides = computeStrides(targetShape);
        for (int i = 0; i < numElements; i++) {
            int[] indices = unflattenIndex(i, targetStrides);
            int[] originalIndices = new int[indices.length];
            for (int j = 0; j < indices.length; j++) {
                originalIndices[j] = (expandedShape[j] > 1) ? indices[j] : 0;
            }
            newData.add(getValue(originalIndices));
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
        int[] broadcastShape = broadcastShape(this.shape, other.shape);
        Tensor tensor1 = this.broadcastTo(broadcastShape);
        Tensor tensor2 = other.broadcastTo(broadcastShape);
        List<Double> resultData = new ArrayList<>();
        int numElements = computeNumElements(broadcastShape);
        int[] strides1 = tensor1.strides;
        int[] strides2 = tensor2.strides;
        for (int i = 0; i < numElements; i++) {
            int[] indices1 = unflattenIndex(i, strides1);
            int[] indices2 = unflattenIndex(i, strides2);
            resultData.add(tensor1.getValue(indices1) + tensor2.getValue(indices2));
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
        int[] broadcastShape = broadcastShape(this.shape, other.shape);
        Tensor tensor1 = this.broadcastTo(broadcastShape);
        Tensor tensor2 = other.broadcastTo(broadcastShape);
        List<Double> resultData = new ArrayList<>();
        int numElements = computeNumElements(broadcastShape);
        int[] strides1 = tensor1.strides;
        int[] strides2 = tensor2.strides;
        for (int i = 0; i < numElements; i++) {
            int[] indices1 = unflattenIndex(i, strides1);
            int[] indices2 = unflattenIndex(i, strides2);
            resultData.add(tensor1.getValue(indices1) - tensor2.getValue(indices2));
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
        int[] broadcastShape = broadcastShape(this.shape, other.shape);
        Tensor tensor1 = this.broadcastTo(broadcastShape);
        Tensor tensor2 = other.broadcastTo(broadcastShape);
        List<Double> resultData = new ArrayList<>();
        int numElements = computeNumElements(broadcastShape);
        int[] strides1 = tensor1.strides;
        int[] strides2 = tensor2.strides;
        for (int i = 0; i < numElements; i++) {
            int[] indices1 = unflattenIndex(i, strides1);
            int[] indices2 = unflattenIndex(i, strides2);
            resultData.add(tensor1.getValue(indices1) * tensor2.getValue(indices2));
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
        if (this.shape[this.shape.length - 1] != other.shape[other.shape.length - 2]) {
            throw new IllegalArgumentException("Shapes " + Arrays.toString(this.shape) + " and " + Arrays.toString(other.shape) + " are not aligned for dot product.");
        }
        int[] resultShape = Arrays.copyOf(this.shape, this.shape.length - 1);
        int[] otherShapeWithoutLast = Arrays.copyOf(other.shape, other.shape.length - 1);
        int[] resultShapeCombined = new int[resultShape.length + otherShapeWithoutLast.length];
        System.arraycopy(resultShape, 0, resultShapeCombined, 0, resultShape.length);
        System.arraycopy(otherShapeWithoutLast, 0, resultShapeCombined, resultShape.length, otherShapeWithoutLast.length);
        int[] finalResultShape = Arrays.copyOf(resultShapeCombined, resultShapeCombined.length -1);
        int[] lastDimension = {other.shape[other.shape.length - 1]};
        int[] finalShape = new int[finalResultShape.length + lastDimension.length];
        System.arraycopy(finalResultShape, 0, finalShape, 0, finalResultShape.length);
        System.arraycopy(lastDimension, 0, finalShape, finalResultShape.length, lastDimension.length);
        List<Double> resultData = new ArrayList<>();
        int numResultElements = computeNumElements(finalShape);
        int[] resultStrides = computeStrides(finalShape);
        for (int i = 0; i < numResultElements; i++) {
            int[] resultIndices = unflattenIndex(i, resultStrides);
            double dotProduct = 0;
            for (int k = 0; k < this.shape[this.shape.length - 1]; k++) {
                int[] aIndices = Arrays.copyOf(resultIndices, resultIndices.length - 1);
                int[] aIndicesWithK = Arrays.copyOf(aIndices, aIndices.length + 1);
                aIndicesWithK[aIndices.length] = k;
                int[] bIndices = new int[other.shape.length];
                bIndices[0] = k;
                for(int j = 1; j < resultIndices.length; j++) {
                    bIndices[j] = resultIndices[resultIndices.length - 1];
                }
                if (other.shape.length > 1) {
                    System.arraycopy(resultIndices, resultIndices.length - 1, bIndices, 1, 1);
                }
                dotProduct += this.getValue(aIndicesWithK) * other.getValue(bIndices);
            }
            resultData.add(dotProduct);
        }
        return new Tensor(resultData, finalShape);
    }

    /**
     * Extracts a sub-tensor from the given start indices to the end indices.
     *
     * @param startIndices Array specifying the start indices for each dimension.
     * @param endIndices   Array specifying the end indices (exclusive) for each dimension.
     * @return A new Tensor containing the extracted sub-tensor.
     */
    public Tensor partial(int[] startIndices, int[] endIndices) {
        if (startIndices.length != shape.length || endIndices.length != shape.length) {
            throw new IllegalArgumentException("startIndices and endIndices must match the number of dimensions.");
        }
        // Compute the new shape of the extracted sub-tensor
        int[] newShape = new int[shape.length];
        for (int i = 0; i < shape.length; i++) {
            newShape[i] = endIndices[i] - startIndices[i];
            if (newShape[i] < 0) {
                throw new IllegalArgumentException("End index must be greater than or equal to start index for dimension " + i);
            }
        }
        // Extract data from the original tensor
        List<Double> subData = new ArrayList<>();
        int numElements = computeNumElements(newShape);
        int[] newStrides = computeStrides(newShape);

        for (int i = 0; i < numElements; i++) {
            int[] subIndices = unflattenIndex(i, newStrides);
            int[] originalIndices = new int[shape.length];
            for (int j = 0; j < shape.length; j++) {
                originalIndices[j] = startIndices[j] + subIndices[j];
            }
            subData.add(getValue(originalIndices));
        }
        return new Tensor(subData, newShape);
    }

    /**
     * Returns a string representation of the tensor.
     *
     * @return String representing the tensor.
     */
    @Override
    public String toString() {
        return "Tensor(shape=" + Arrays.toString(shape) + ", data=" + formatTensor(data, shape) + ")";
    }
    private Object formatTensor(List<Double> data, int[] shape) {
        if (shape.length == 1) {
            return data;
        }
        int stride = computeNumElements(Arrays.copyOfRange(shape, 1, shape.length));
        List<Object> formattedList = new ArrayList<>();
        for (int i = 0; i < shape[0]; i++) {
            formattedList.add(formatTensor(data.subList(i * stride, (i + 1) * stride), Arrays.copyOfRange(shape, 1, shape.length)));
        }
        return formattedList;
    }

    public int[] getShape() {
        return shape;
    }

    public List<Double> getData() {
        return data;
    }
}