package Math;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Matrix implements Serializable {

    private int row;
    private int col;
    private double[][] values;

    /**
     * A constructor of {@link Matrix} class which takes a filename as an input and reads numbers into values {@link java.lang.reflect.Array}
     * and row and column variables.
     *
     * @param filename is used to read file.
     */
    public Matrix(String filename) {
        Scanner sc;
        int i, j;
        try {
            sc = new Scanner(new File(filename));
            row = sc.nextInt();
            col = sc.nextInt();
            values = new double[row][col];
            for (i = 0; i < row; i++) {
                for (j = 0; j < col; j++) {
                    values[i][j] = sc.nextDouble();
                }
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("File " + filename + " not found");
        }
    }

    /**
     * Another constructor of {@link Matrix} class which takes row and column numbers as inputs and creates new values
     * {@link java.lang.reflect.Array} with given parameters.
     *
     * @param row is used to create matrix.
     * @param col is used to create matrix.
     */
    public Matrix(int row, int col) {
        values = new double[row][col];
        this.row = row;
        this.col = col;
    }

    /**
     * Another constructor of {@link Matrix} class which takes row, column, minimum and maximum values as inputs.
     * First it creates new values {@link java.lang.reflect.Array} with given row and column numbers. Then fills in the
     * positions with random numbers using minimum and maximum inputs.
     *
     * @param row is used to create matrix.
     * @param col is used to create matrix.
     * @param min minimum value.
     * @param max maximum value.
     * @param random random function to set the random values in the matrix.
     */
    public Matrix(int row, int col, double min, double max, Random random) {
        values = new double[row][col];
        this.row = row;
        this.col = col;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                values[i][j] = min + (max - min) * random.nextDouble();
            }
        }
    }

    /**
     * The overridden clone method creates new Matrix and copies the content of values {@link java.lang.reflect.Array} into new matrix.
     *
     * @return Matrix which is the copy of values {@link java.lang.reflect.Array}.
     */
    public Matrix clone() {
        Matrix result = new Matrix(row, col);
        for (int i = 0; i < row; i++)
            System.arraycopy(values[i], 0, result.values[i], 0, col);
        return result;
    }

    /**
     * Another constructor of {@link Matrix} class which takes size as input and creates new values {@link java.lang.reflect.Array}
     * with using size input and assigns 1 to each element at the diagonal.
     *
     * @param size is used declaring the size of the array.
     */
    public Matrix(int size) {
        int i;
        values = new double[size][size];
        row = size;
        col = size;
        for (i = 0; i < size; i++) {
            values[i][i] = 1;
        }
    }

    /**
     * The printToFile method takes a fileName as an input and prints values {@link java.lang.reflect.Array} into the file.
     *
     * @param fileName String input to write to file.
     */
    public void printToFile(String fileName) {
        PrintWriter output;
        try {
            output = new PrintWriter(new File(fileName));
            for (int i = 0; i < row; i++) {
                output.print(String.format("%.5f", values[i][0]));
                for (int j = 1; j < col; j++) {
                    output.print(" " + String.format("%.5f", values[i][j]));
                }
                output.println();
            }
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * The getter for the index at given rowNo and colNo of values {@link java.lang.reflect.Array}.
     *
     * @param rowNo integer input for row number.
     * @param colNo integer input for column number.
     * @return item at given index of values {@link java.lang.reflect.Array}.
     */
    public double getValue(int rowNo, int colNo) {
        return values[rowNo][colNo];
    }

    /**
     * The setter for the value at given index of values {@link java.lang.reflect.Array}.
     *
     * @param rowNo integer input for row number.
     * @param colNo integer input for column number.
     * @param value is used to set at given index.
     */
    public void setValue(int rowNo, int colNo, double value) {
        values[rowNo][colNo] = value;
    }

    /**
     * The addValue method adds the given value to the item at given index of values {@link java.lang.reflect.Array}.
     *
     * @param rowNo integer input for row number.
     * @param colNo integer input for column number.
     * @param value is used to add to given item at given index.
     */
    public void addValue(int rowNo, int colNo, double value) {
        values[rowNo][colNo] += value;
    }

    /**
     * The increment method adds 1 to the item at given index of values {@link java.lang.reflect.Array}.
     *
     * @param rowNo integer input for row number.
     * @param colNo integer input for column number.
     */
    public void increment(int rowNo, int colNo) {
        values[rowNo][colNo] += 1;
    }

    /**
     * The getter for the row variable.
     *
     * @return row number.
     */
    public int getRow() {
        return row;
    }

    /**
     * The getRow method returns the vector of values {@link java.lang.reflect.Array} at given row input.
     *
     * @param row integer input for row number.
     * @return Vector of values {@link java.lang.reflect.Array} at given row input.
     */
    public Vector getRow(int row) {
        return new Vector(values[row]);
    }

    /**
     * The getColumn method creates an {@link ArrayList} and adds items at given column number of values {@link java.lang.reflect.Array}
     * to the {@link ArrayList}.
     *
     * @param column integer input for column number.
     * @return ArrayList of given column number.
     */
    public ArrayList<Double> getColumn(int column) {
        ArrayList<Double> vector = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            vector.add(values[i][column]);
        }
        return vector;
    }

    /**
     * The getter for column variable.
     *
     * @return column variable.
     */
    public int getColumn() {
        return col;
    }

    /**
     * The columnWiseNormalize method, first accumulates items column by column then divides items by the summation.
     */
    public void columnWiseNormalize() {
        for (int i = 0; i < row; i++) {
            double sum = 0.0;
            for (int j = 0; j < col; j++) {
                sum += values[i][j];
            }
            for (int j = 0; j < col; j++) {
                values[i][j] /= sum;
            }
        }
    }

    /**
     * The multiplyWithConstant method takes a constant as an input and multiplies each item of values {@link java.lang.reflect.Array}
     * with given constant.
     *
     * @param constant value to multiply items of values {@link java.lang.reflect.Array}.
     */
    public void multiplyWithConstant(double constant) {
        int i, j;
        for (i = 0; i < row; i++) {
            for (j = 0; j < col; j++) {
                values[i][j] *= constant;
            }
        }
    }

    /**
     * The divideByConstant method takes a constant as an input and divides each item of values {@link java.lang.reflect.Array}
     * with given constant.
     *
     * @param constant value to divide items of values {@link java.lang.reflect.Array}.
     */
    public void divideByConstant(double constant) {
        int i, j;
        for (i = 0; i < row; i++) {
            for (j = 0; j < col; j++) {
                values[i][j] /= constant;
            }
        }
    }

    /**
     * The add method takes a {@link Matrix} as an input and accumulates values {@link java.lang.reflect.Array} with the
     * corresponding items of given Matrix. If the sizes of both Matrix and values {@link java.lang.reflect.Array} do not match,
     * it throws {@link MatrixDimensionMismatch} exception.
     *
     * @param m Matrix type input.
     * @throws MatrixDimensionMismatch exception if sizes of both input Matrix and values Array do not match.
     */
    public void add(Matrix m) throws MatrixDimensionMismatch {
        int i, j;
        if (row != m.row || col != m.col) {
            throw new MatrixDimensionMismatch();
        }
        for (i = 0; i < row; i++) {
            for (j = 0; j < col; j++) {
                values[i][j] += m.values[i][j];
            }
        }
    }

    public Matrix sum(Matrix m) throws MatrixDimensionMismatch{
        int i, j;
        if (row != m.row || col != m.col) {
            throw new MatrixDimensionMismatch();
        }
        Matrix result = new Matrix(row, col);
        for (i = 0; i < row; i++) {
            for (j = 0; j < col; j++) {
                result.values[i][j] = values[i][j] + m.values[i][j];
            }
        }
        return result;
    }

    /**
     * The add method which takes a row number and a Vector as inputs. It sums up the corresponding values at the given row of
     * values {@link java.lang.reflect.Array} and given {@link Vector}. If the sizes of both Matrix and values
     * {@link java.lang.reflect.Array} do not match, it throws {@link MatrixColumnMismatch} exception.
     *
     * @param rowNo integer input for row number.
     * @param v     Vector type input.
     * @throws MatrixColumnMismatch exception if sizes of both input Matrix and values Array do not match.
     */
    public void add(int rowNo, Vector v) throws MatrixColumnMismatch {
        if (col != v.size()) {
            throw new MatrixColumnMismatch();
        }
        for (int i = 0; i < col; i++) {
            values[rowNo][i] += v.getValue(i);
        }
    }

    /**
     * The subtract method takes a {@link Matrix} as an input and subtracts from values {@link java.lang.reflect.Array} the
     * corresponding items of given Matrix. If the sizes of both Matrix and values {@link java.lang.reflect.Array} do not match,
     * it throws {@link MatrixDimensionMismatch} exception.
     *
     * @param m Matrix type input.
     * @throws MatrixDimensionMismatch exception if sizes of both input Matrix and values Array do not match.
     */
    public void subtract(Matrix m) throws MatrixDimensionMismatch {
        int i, j;
        if (row != m.row || col != m.col) {
            throw new MatrixDimensionMismatch();
        }
        for (i = 0; i < row; i++) {
            for (j = 0; j < col; j++) {
                values[i][j] -= m.values[i][j];
            }
        }
    }

    public Matrix difference(Matrix m) throws MatrixDimensionMismatch {
        int i, j;
        if (row != m.row || col != m.col) {
            throw new MatrixDimensionMismatch();
        }
        Matrix result = new Matrix(row, col);
        for (i = 0; i < row; i++) {
            for (j = 0; j < col; j++) {
                result.values[i][j] = values[i][j] - m.values[i][j];
            }
        }
        return result;
    }

    /**
     * The multiplyWithVectorFromLeft method takes a Vector as an input and creates a result {@link java.lang.reflect.Array}.
     * Then, multiplies values of input Vector starting from the left side with the values {@link java.lang.reflect.Array},
     * accumulates the multiplication, and assigns to the result {@link java.lang.reflect.Array}. If the sizes of both Vector
     * and row number do not match, it throws {@link MatrixRowMismatch} exception.
     *
     * @param v {@link Vector} type input.
     * @return Vector that holds the result.
     * @throws MatrixRowMismatch exception if sizes of both input Vector and row number do not match.
     */
    public Vector multiplyWithVectorFromLeft(Vector v) throws MatrixRowMismatch {
        if (row != v.size()) {
            throw new MatrixRowMismatch();
        }
        double[] result = new double[col];
        for (int i = 0; i < col; i++) {
            result[i] = 0.0;
            for (int j = 0; j < row; j++) {
                result[i] += v.getValue(j) * values[j][i];
            }
        }
        return new Vector(result);
    }

    /**
     * The multiplyWithVectorFromRight method takes a Vector as an input and creates a result {@link java.lang.reflect.Array}.
     * Then, multiplies values of input Vector starting from the right side with the values {@link java.lang.reflect.Array},
     * accumulates the multiplication, and assigns to the result {@link java.lang.reflect.Array}. If the sizes of both Vector
     * and row number do not match, it throws {@link MatrixColumnMismatch} exception.
     *
     * @param v {@link Vector} type input.
     * @return Vector that holds the result.
     * @throws MatrixColumnMismatch exception if sizes of both input Vector and column number do not match.
     */
    public Vector multiplyWithVectorFromRight(Vector v) throws MatrixColumnMismatch {
        if (col != v.size()) {
            throw new MatrixColumnMismatch();
        }
        double[] result = new double[row];
        for (int i = 0; i < row; i++) {
            result[i] = 0.0;
            for (int j = 0; j < col; j++) {
                result[i] += v.getValue(j) * values[i][j];
            }
        }
        return new Vector(result);
    }

    /**
     * The columnSum method takes a column number as an input and accumulates items at given column number of values
     * {@link java.lang.reflect.Array}.
     *
     * @param columnNo Column number input.
     * @return summation of given column of values {@link java.lang.reflect.Array}.
     */
    public double columnSum(int columnNo) {
        double sum = 0;
        for (int i = 0; i < row; i++) {
            sum += values[i][columnNo];
        }
        return sum;
    }

    /**
     * The sumOfRows method creates a mew result {@link Vector} and adds the result of columnDum method's corresponding
     * index to the newly created result {@link Vector}.
     *
     * @return Vector that holds column sum.
     */
    public Vector sumOfRows() {
        Vector result = new Vector(0, 0.0);
        for (int i = 0; i < col; i++) {
            result.add(columnSum(i));
        }
        return result;
    }

    /**
     * The rowSum method takes a row number as an input and accumulates items at given row number of values
     * {@link java.lang.reflect.Array}.
     *
     * @param rowNo Row number input.
     * @return summation of given row of values {@link java.lang.reflect.Array}.
     */
    public double rowSum(int rowNo) {
        double sum = 0;
        for (int i = 0; i < col; i++) {
            sum += values[rowNo][i];
        }
        return sum;
    }

    /**
     * The multiply method takes a {@link Matrix} as an input. First it creates a result {@link Matrix} and puts the
     * accumulated multiplication of values {@link java.lang.reflect.Array} and given {@link Matrix} into result
     * {@link Matrix}. If the size of Matrix's row size and values {@link java.lang.reflect.Array}'s column size do not match,
     * it throws {@link MatrixRowColumnMismatch} exception.
     *
     * @param m Matrix type input.
     * @return result {@link Matrix}.
     * @throws MatrixRowColumnMismatch if row and column size does not match.
     */
    public Matrix multiply(Matrix m) throws MatrixRowColumnMismatch {
        int i, j, k;
        double sum;
        Matrix result;
        if (col != m.row) {
            throw new MatrixRowColumnMismatch();
        }
        result = new Matrix(row, m.col);
        for (i = 0; i < row; i++) {
            for (j = 0; j < m.col; j++) {
                sum = 0.0;
                for (k = 0; k < col; k++) {
                    sum += values[i][k] * m.values[k][j];
                }
                result.values[i][j] = sum;
            }
        }
        return result;
    }

    /**
     * The elementProduct method takes a {@link Matrix} as an input and performs element wise multiplication. Puts result
     * to the newly created Matrix. If the size of Matrix's row and column size does not match with the values
     * {@link java.lang.reflect.Array}'s row and column size, it throws {@link MatrixDimensionMismatch} exception.
     *
     * @param m Matrix type input.
     * @return result {@link Matrix}.
     * @throws MatrixDimensionMismatch if row and column sizes do not match.
     */
    public Matrix elementProduct(Matrix m) throws MatrixDimensionMismatch {
        int i, j;
        if (row != m.row || col != m.col) {
            throw new MatrixDimensionMismatch();
        }
        Matrix result;
        result = new Matrix(row, m.col);
        for (i = 0; i < row; i++) {
            for (j = 0; j < col; j++) {
                result.values[i][j] = values[i][j] * m.values[i][j];
            }
        }
        return result;
    }

    /**
     * The elementProduct method takes a {@link Vector} as an input and performs element wise multiplication. Puts result
     * to the newly created Matrix.
     *
     * @param v Vector type input.
     * @return result {@link Matrix}.
     */
    public Matrix elementProduct(Vector v) {
        Matrix result;
        result = new Matrix(row, col);
        if (row == 1 && col == v.size()){
            for (int i = 0; i < col; i++) {
                result.values[0][i] = values[0][i] * v.getValue(i);
            }
        } else {
            if (col == 1 && row == v.size()){
                for (int i = 0; i < row; i++) {
                    result.values[i][0] = values[i][0] * v.getValue(i);
                }
            }
        }
        return result;
    }

    /**
     * The sumOfElements method accumulates all the items in values {@link java.lang.reflect.Array} and
     * returns this summation.
     *
     * @return sum of the items of values {@link java.lang.reflect.Array}.
     */
    public double sumOfElements() {
        int i, j;
        double sum = 0.0;
        for (i = 0; i < row; i++) {
            for (j = 0; j < col; j++) {
                sum += values[i][j];
            }
        }
        return sum;
    }

    /**
     * The trace method accumulates items of values {@link java.lang.reflect.Array} at the diagonal.
     *
     * @return sum of items at diagonal.
     * @throws MatrixNotSquare if row and column sizes do not match.
     */
    public double trace() throws MatrixNotSquare{
        if (row != col){
            throw new MatrixNotSquare();
        }
        int i;
        double sum = 0.0;
        for (i = 0; i < row; i++) {
            sum += values[i][i];
        }
        return sum;
    }

    /**
     * The transpose method creates a new {@link Matrix}, then takes the transpose of values {@link java.lang.reflect.Array}
     * and puts transposition to the {@link Matrix}.
     *
     * @return Matrix type output.
     */
    public Matrix transpose() {
        int i, j;
        Matrix result = new Matrix(col, row);
        for (i = 0; i < row; i++) {
            for (j = 0; j < col; j++) {
                result.values[j][i] = values[i][j];
            }
        }
        return result;
    }

    /**
     * The partial method takes 4 integer inputs; rowStart, rowEnd, colStart, colEnd and creates a {@link Matrix} size of
     * rowEnd - rowStart + 1 x colEnd - colStart + 1. Then, puts corresponding items of values {@link java.lang.reflect.Array}
     * to the new result {@link Matrix}.
     *
     * @param rowStart integer input for defining starting index of row.
     * @param rowEnd   integer input for defining ending index of row.
     * @param colStart integer input for defining starting index of column.
     * @param colEnd   integer input for defining ending index of column.
     * @return result Matrix.
     */
    public Matrix partial(int rowStart, int rowEnd, int colStart, int colEnd) {
        int i, j;
        Matrix result = new Matrix(rowEnd - rowStart + 1, colEnd - colStart + 1);
        for (i = rowStart; i <= rowEnd; i++)
            for (j = colStart; j <= colEnd; j++)
                result.values[i - rowStart][j - colStart] = values[i][j];
        return result;
    }

    /**
     * The isSymmetric method compares each item of values {@link java.lang.reflect.Array} at positions (i, j) with (j, i)
     * and returns true if they are equal, false otherwise.
     *
     * @return true if items are equal, false otherwise.
     * @throws MatrixNotSquare if row and column sizes do not match.
     */
    public boolean isSymmetric() throws MatrixNotSquare {
        if (row != col){
            throw new MatrixNotSquare();
        }
        for (int i = 0; i < row - 1; i++) {
            for (int j = i + 1; j < row; j++) {
                if (values[i][j] != values[j][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * The determinant method first creates a new {@link java.lang.reflect.Array}, and copies the items of  values
     * {@link java.lang.reflect.Array} into new {@link java.lang.reflect.Array}. Then, calculates the determinant of this
     * new {@link java.lang.reflect.Array}.
     *
     * @return determinant of values {@link java.lang.reflect.Array}.
     * @throws MatrixNotSquare if row and column sizes do not match.
     */
    public double determinant() throws MatrixNotSquare {
        if (row != col){
            throw new MatrixNotSquare();
        }
        int i, j, k;
        double ratio, det = 1.0;
        double[][] copy = new double[row][col];
        for (i = 0; i < row; i++)
            for (j = 0; j < col; j++)
                copy[i][j] = values[i][j];
        for (i = 0; i < row; i++) {
            det *= copy[i][i];
            if (det == 0.0)
                break;
            for (j = i + 1; j < row; j++) {
                ratio = copy[j][i] / copy[i][i];
                for (k = i; k < col; k++)
                    copy[j][k] = copy[j][k] - copy[i][k] * ratio;
            }
        }
        return det;
    }

    /**
     * The inverse method finds the inverse of values {@link java.lang.reflect.Array}.
     *
     * @throws DeterminantZero exception.
     * @throws MatrixNotSquare if row and column sizes do not match.
     */
    public void inverse() throws DeterminantZero, MatrixNotSquare {
        if (row != col){
            throw new MatrixNotSquare();
        }
        double big;
        double dum, pivinv;
        int i, icol, irow, j, k, l, ll;
        Matrix b;
        int indxc[], indxr[], ipiv[];
        b = new Matrix(row);
        indxc = new int[row];
        indxr = new int[row];
        ipiv = new int[row];
        for (j = 0; j < row; j++)
            ipiv[j] = 0;
        for (i = 1; i <= row; i++) {
            big = 0.0;
            irow = -1;
            icol = -1;
            for (j = 1; j <= row; j++)
                if (ipiv[j - 1] != 1)
                    for (k = 1; k <= row; k++)
                        if (ipiv[k - 1] == 0)
                            if (Math.abs(values[j - 1][k - 1]) >= big) {
                                big = Math.abs(values[j - 1][k - 1]);
                                irow = j;
                                icol = k;
                            }
            if (irow == -1 || icol == -1)
                throw new DeterminantZero();
            ipiv[icol - 1] = ipiv[icol - 1] + 1;
            if (irow != icol) {
                for (l = 1; l <= row; l++) {
                    dum = values[irow - 1][l - 1];
                    values[irow - 1][l - 1] = values[icol - 1][l - 1];
                    values[icol - 1][l - 1] = dum;
                }
                for (l = 1; l <= row; l++) {
                    dum = b.values[irow - 1][l - 1];
                    b.values[irow - 1][l - 1] = b.values[icol - 1][l - 1];
                    b.values[icol - 1][l - 1] = dum;
                }
            }
            indxr[i - 1] = irow;
            indxc[i - 1] = icol;
            if (values[icol - 1][icol - 1] == 0)
                throw new DeterminantZero();
            pivinv = (1.0) / (values[icol - 1][icol - 1]);
            values[icol - 1][icol - 1] = 1.0;
            for (l = 1; l <= row; l++)
                values[icol - 1][l - 1] = values[icol - 1][l - 1] * pivinv;
            for (l = 1; l <= row; l++)
                b.values[icol - 1][l - 1] = b.values[icol - 1][l - 1] * pivinv;
            for (ll = 1; ll <= row; ll++)
                if (ll != icol) {
                    dum = values[ll - 1][icol - 1];
                    values[ll - 1][icol - 1] = 0.0;
                    for (l = 1; l <= row; l++)
                        values[ll - 1][l - 1] = values[ll - 1][l - 1] - values[icol - 1][l - 1] * dum;
                    for (l = 1; l <= row; l++)
                        b.values[ll - 1][l - 1] = b.values[ll - 1][l - 1] - b.values[icol - 1][l - 1] * dum;
                }
        }
        for (l = row; l >= 1; l--)
            if (indxr[l - 1] != indxc[l - 1])
                for (k = 1; k <= row; k++) {
                    dum = values[k - 1][indxr[l - 1] - 1];
                    values[k - 1][indxr[l - 1] - 1] = values[k - 1][indxc[l - 1] - 1];
                    values[k - 1][indxc[l - 1] - 1] = dum;
                }
    }

    /**
     * The choleskyDecomposition method creates a new {@link Matrix} and puts the Cholesky Decomposition of values Array
     * into this {@link Matrix}. Also, it throws {@link MatrixNotSymmetric} exception if it is not symmetric and
     * {@link MatrixNotPositiveDefinite} exception if the summation is negative.
     *
     * @return Matrix type output.
     * @throws MatrixNotSymmetric        if values {@link ArrayList} is not symmetric
     * @throws MatrixNotPositiveDefinite if the summation is negative.
     */
    public Matrix choleskyDecomposition() throws MatrixNotSymmetric, MatrixNotPositiveDefinite, MatrixNotSquare {
        int i, j, k;
        double sum;
        if (!isSymmetric()) {
            throw new MatrixNotSymmetric();
        }
        Matrix b = new Matrix(row, col);
        for (i = 0; i < row; i++) {
            for (j = i; j < row; j++) {
                sum = values[i][j];
                for (k = i - 1; k >= 0; k--)
                    sum -= values[i][k] * values[j][k];
                if (i == j) {
                    if (sum <= 0.0)
                        throw new MatrixNotPositiveDefinite();
                    b.values[i][i] = Math.sqrt(sum);
                } else
                    b.values[j][i] = sum / b.values[i][i];
            }
        }
        return b;
    }

    /**
     * The rotate method rotates values {@link java.lang.reflect.Array} according to given inputs.
     *
     * @param s   double input.
     * @param tau double input.
     * @param i   integer input.
     * @param j   integer input.
     * @param k   integer input.
     * @param l   integer input.
     */
    private void rotate(double s, double tau, int i, int j, int k, int l) {
        double g = values[i][j];
        double h = values[k][l];
        values[i][j] = g - s * (h + g * tau);
        values[k][l] = h + s * (g - h * tau);
    }

    /**
     * The characteristics method finds and returns a sorted {@link ArrayList} of {@link Eigenvector}s. And it throws
     * {@link MatrixNotSymmetric} exception if it is not symmetric.
     *
     * @return a sorted {@link ArrayList} of {@link Eigenvector}s.
     * @throws MatrixNotSymmetric exception if it is not symmetric.
     */
    public ArrayList<Eigenvector> characteristics() throws MatrixNotSymmetric, MatrixNotSquare {
        int j, iq, ip, i;
        double threshold, theta, tau, t, sm, s, h, g, c;
        if (!isSymmetric()) {
            throw new MatrixNotSymmetric();
        }
        Matrix matrix1 = clone();
        Matrix v = new Matrix(row, row);
        double[] d = new double[row];
        double[] b = new double[row];
        double[] z = new double[row];
        double EPS = 0.000000000000000001;
        for (ip = 0; ip < row; ip++) {
            for (iq = 0; iq < row; iq++) {
                v.values[ip][iq] = 0.0;
            }
            v.values[ip][ip] = 1.0;
        }
        for (ip = 0; ip < row; ip++) {
            b[ip] = d[ip] = matrix1.values[ip][ip];
            z[ip] = 0.0;
        }
        for (i = 1; i <= 50; i++) {
            sm = 0.0;
            for (ip = 0; ip < row - 1; ip++)
                for (iq = ip + 1; iq < row; iq++)
                    sm += Math.abs(matrix1.values[ip][iq]);
            if (sm == 0.0) {
                break;
            }
            if (i < 4)
                threshold = 0.2 * sm / Math.pow(row, 2);
            else
                threshold = 0.0;
            for (ip = 0; ip < row - 1; ip++) {
                for (iq = ip + 1; iq < row; iq++) {
                    g = 100.0 * Math.abs(matrix1.values[ip][iq]);
                    if (i > 4 && g <= EPS * Math.abs(d[ip]) && g <= EPS * Math.abs(d[iq])) {
                        matrix1.values[ip][iq] = 0.0;
                    } else {
                        if (Math.abs(matrix1.values[ip][iq]) > threshold) {
                            h = d[iq] - d[ip];
                            if (g <= EPS * Math.abs(h)) {
                                t = matrix1.values[ip][iq] / h;
                            } else {
                                theta = 0.5 * h / matrix1.values[ip][iq];
                                t = 1.0 / (Math.abs(theta) + Math.sqrt(1.0 + Math.pow(theta, 2)));
                                if (theta < 0.0) {
                                    t = -t;
                                }
                            }
                            c = 1.0 / Math.sqrt(1 + Math.pow(t, 2));
                            s = t * c;
                            tau = s / (1.0 + c);
                            h = t * matrix1.values[ip][iq];
                            z[ip] -= h;
                            z[iq] += h;
                            d[ip] -= h;
                            d[iq] += h;
                            matrix1.values[ip][iq] = 0.0;
                            for (j = 0; j < ip; j++) {
                                matrix1.rotate(s, tau, j, ip, j, iq);
                            }
                            for (j = ip + 1; j < iq; j++) {
                                matrix1.rotate(s, tau, ip, j, j, iq);
                            }
                            for (j = iq + 1; j < row; j++) {
                                matrix1.rotate(s, tau, ip, j, iq, j);
                            }
                            for (j = 0; j < row; j++) {
                                v.rotate(s, tau, j, ip, j, iq);
                            }
                        }
                    }
                }
            }
            for (ip = 0; ip < row; ip++) {
                b[ip] = b[ip] + z[ip];
                d[ip] = b[ip];
                z[ip] = 0.0;
            }
        }
        ArrayList<Eigenvector> result = new ArrayList<>();
        for (i = 0; i < row; i++) {
            if (d[i] > 0) {
                result.add(new Eigenvector(d[i], v.getColumn(i)));
            }
        }
        Collections.sort(result);
        return result;
    }
}
