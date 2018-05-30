package Math;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Matrix implements Serializable{

    private int row;
    private int col;
    private double[][] values;
    
    public Matrix(String filename){
        Scanner sc;
        int i, j;
        try{
            sc = new Scanner(new File(filename));
            row = sc.nextInt();
            col = sc.nextInt();
            values = new double[row][col];
            for (i = 0; i < row; i++){
                for (j = 0; j < col; j++){
                    values[i][j] = sc.nextDouble();
                }
            }
        }
        catch (FileNotFoundException fileNotFoundException){
            System.out.println("File " + filename + " not found");
        }
    }

    public Matrix(int row, int col){
        values = new double[row][col];
        this.row = row;
        this.col = col;
    }

    public Matrix(int row, int col, double min, double max){
        values = new double[row][col];
        this.row = row;
        this.col = col;
        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                values[i][j] = min + (max - min) * Math.random();
            }
        }
    }

    public Matrix clone(){
        Matrix result = new Matrix(row, col);
        for (int i = 0; i < row; i++)
            System.arraycopy(values[i], 0, result.values[i], 0, col);
        return result;
    }

    public Matrix(int size){
        int i;
        values = new double[size][size];
        row = size;
        col = size;
        for (i = 0; i < size; i++){
            values[i][i] = 1;
        }
    }

    public void printToFile(String fileName){
        PrintWriter output;
        try {
            output = new PrintWriter(new File(fileName));
            for (int i = 0; i < row; i++){
                output.print(String.format("%.5f", values[i][0]));
                for (int j = 1; j < col; j++){
                    output.print(" " + String.format("%.5f", values[i][j]));
                }
                output.println();
            }
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public double getValue(int rowNo, int colNo){
        return values[rowNo][colNo];
    }

    public void setValue(int rowNo, int colNo, double value){
        values[rowNo][colNo] = value;
    }

    public void addValue(int rowNo, int colNo, double value){
        values[rowNo][colNo] += value;
    }

    public void increment(int rowNo, int colNo){
        values[rowNo][colNo] += 1;
    }

    public int getRow(){
        return row;
    }

    public Vector getRow(int row){
        return new Vector(values[row]);
    }

    public ArrayList<Double> getColumn(int column){
        ArrayList<Double> vector = new ArrayList<>();
        for (int i = 0; i < row; i++){
            vector.add(values[i][column]);
        }
        return vector;
    }

    public int getColumn(){
        return col;
    }

    public void columnWiseNormalize(){
        for (int i = 0; i < row; i++){
            double sum = 0.0;
            for (int j = 0; j < col; j++){
                sum += values[i][j];
            }
            for (int j = 0; j < col; j++){
                values[i][j] /= sum;
            }
        }
    }

    public void multiplyWithConstant(double constant){
        int i, j;
        for (i = 0; i < row; i++){
            for (j = 0; j < col; j++){
                values[i][j] *= constant;
            }
        }
    }

    public void divideByConstant(double constant){
        int i, j;
        for (i = 0; i < row; i++){
            for (j = 0; j < col; j++){
                values[i][j] /= constant;
            }
        }
    }

    public void add(Matrix m) throws MatrixDimensionMismatch {
        int i, j;
        if (row != m.row || col != m.col){
            throw new MatrixDimensionMismatch();
        }
        for (i = 0; i < row; i++){
            for (j = 0; j < col; j++){
                values[i][j] += m.values[i][j];
            }
        }
    }

    public void add(int rowNo, Vector v) throws MatrixColumnMismatch {
        if (col != v.size()){
            throw new MatrixColumnMismatch();
        }
        for (int i = 0; i < col; i++){
            values[rowNo][i] += v.getValue(i);
        }
    }

    public void subtract(Matrix m) throws MatrixDimensionMismatch {
        int i, j;
        if (row != m.row || col != m.col){
            throw new MatrixDimensionMismatch();
        }
        for (i = 0; i < row; i++){
            for (j = 0; j < col; j++){
                values[i][j] -= m.values[i][j];
            }
        }
    }

    public Vector multiplyWithVectorFromLeft(Vector v) throws MatrixRowMismatch {
        if (row != v.size()){
            throw new MatrixRowMismatch();
        }
        double[] result = new double[col];
        for (int i = 0; i < col; i++){
            result[i] = 0.0;
            for (int j = 0; j < row; j++){
                result[i] += v.getValue(j) * values[j][i];
            }
        }
        return new Vector(result);
    }

    public Vector multiplyWithVectorFromRight(Vector v) throws MatrixColumnMismatch {
        if (col != v.size()){
            throw new MatrixColumnMismatch();
        }
        double[] result = new double[row];
        for (int i = 0; i < row; i++){
            result[i] = 0.0;
            for (int j = 0; j < col; j++){
                result[i] += v.getValue(j) * values[i][j];
            }
        }
        return new Vector(result);
    }

    public double columnSum(int columnNo){
        double sum = 0;
        for (int i = 0; i < row; i++){
            sum += values[i][columnNo];
        }
        return sum;
    }

    public Vector sumOfRows(){
        Vector result = new Vector(0, 0.0);
        for (int i = 0; i < col; i++){
            result.add(columnSum(i));
        }
        return result;
    }

    public double rowSum(int rowNo){
        double sum = 0;
        for (int i = 0; i < col; i++){
            sum += values[rowNo][i];
        }
        return sum;
    }

    public Matrix multiply(Matrix m) throws MatrixRowColumnMismatch {
        int i, j, k;
        double sum;
        Matrix result;
        if (col != m.row){
            throw new MatrixRowColumnMismatch();
        }
        result = new Matrix(row, m.col);
        for (i = 0; i < row; i++){
            for (j = 0; j < m.col; j++){
                sum = 0.0;
                for (k = 0; k < col; k++){
                    sum += values[i][k] * m.values[k][j];
                }
                result.values[i][j] = sum;
            }
        }
        return result;
    }

    public Matrix elementProduct(Matrix m) throws MatrixDimensionMismatch {
        int i, j;
        if (row != m.row || col != m.col){
            throw new MatrixDimensionMismatch();
        }
        Matrix result;
        result = new Matrix(row, m.col);
        for (i = 0; i < row; i++){
            for (j = 0; j < col; j++){
                result.values[i][j] = values[i][j] * m.values[i][j];
            }
        }
        return result;
    }

    public double sumOfElements(){
        int i, j;
        double sum = 0.0;
        for (i = 0; i < row; i++){
            for (j = 0; j < col; j++){
                sum += values[i][j];
            }
        }
        return sum;
    }

    public double trace(){
        int i;
        double sum = 0.0;
        for (i = 0; i < row; i++){
            sum += values[i][i];
        }
        return sum;
    }

    public Matrix transpose(){
        int i, j;
        Matrix result = new Matrix(col, row);
        for (i = 0; i < row; i++){
            for (j = 0; j < col; j++){
                result.values[j][i] = values[i][j];
            }
        }
        return result;
    }

    public Matrix partial(int rowstart, int rowend, int colstart, int colend){
        int i, j;
        Matrix result = new Matrix(rowend - rowstart + 1, colend - colstart + 1);
        for (i = rowstart; i <= rowend; i++)
          for (j = colstart; j <= colend; j++)
            result.values[i - rowstart][j - colstart] = values[i][j];
        return result;
    }

    public boolean isSymmetric(){
        for (int i = 0; i < row - 1; i++){
            for (int j = i + 1; j < row; j++){
                if (values[i][j] != values[j][i]){
                    return false;
                }
            }
        }
        return true;
    }

    public double determinant(){
        int i, j, k;
        double ratio, det = 1.0;
        double[][] copy = new double[row][col];
        for (i = 0; i < row; i++)
            for (j = 0; j < col; j++)
                copy[i][j] = values[i][j];
        for (i = 0; i < row; i++){
            det *= copy[i][i];
            if (det == 0.0)
                break;
            for (j = i + 1; j < row; j++){
                ratio = copy[j][i] / copy[i][i];
                for (k = i; k < col; k++)
                    copy[j][k] = copy[j][k] - copy[i][k] * ratio;
            }
        }
        return det;
    }

    public void inverse() throws DeterminantZero{
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
        for (i = 1; i <= row; i++){
            big = 0.0;
            irow = -1;
            icol = -1;
            for (j = 1; j <= row; j++)
                if (ipiv[j - 1] != 1)
                    for (k = 1; k <= row; k++)
                        if (ipiv[k - 1] == 0)
                            if (Math.abs(values[j - 1][k - 1]) >= big){
                                big = Math.abs(values[j - 1][k - 1]);
                                irow = j;
                                icol = k;
                            }
            if (irow == -1 || icol == -1)
                throw new DeterminantZero();
            ipiv[icol - 1] = ipiv[icol - 1] + 1;
            if (irow != icol){
                for (l = 1; l <= row; l++){
                    dum = values[irow - 1][l - 1];
                    values[irow - 1][l - 1] = values[icol - 1][l - 1];
                    values[icol - 1][l - 1] = dum;
                }
                for (l = 1; l <= row; l++){
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
                if (ll != icol){
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
                for (k = 1; k <= row; k++){
                    dum = values[k - 1][indxr[l - 1] - 1];
                    values[k - 1][indxr[l - 1] - 1] = values[k - 1][indxc[l - 1] - 1];
                    values[k - 1][indxc[l - 1] - 1] = dum;
                }
    }

    public Matrix choleskyDecomposition() throws MatrixNotSymmetric, MatrixNotPositiveDefinite {
        int i, j, k;
        double sum;
        if (!isSymmetric()){
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


    private void rotate(double s, double tau, int i, int j, int k, int l){
        double g = values[i][j];
        double h = values[k][l];
        values[i][j] = g - s * (h + g * tau);
        values[k][l] = h + s * (g - h * tau);
    }

    public ArrayList<Eigenvector> characteristics() throws MatrixNotSymmetric {
        int j, iq, ip, i;
        double threshold, theta, tau, t, sm, s, h, g, c;
        if (!isSymmetric()){
            throw new MatrixNotSymmetric();
        }
        Matrix matrix1 = clone();
        Matrix v = new Matrix(row, row);
        double[] d = new double[row];
        double[] b = new double[row];
        double[] z = new double[row];
        double EPS = 0.000000000000000001;
        for (ip = 0; ip < row; ip++){
            for (iq = 0; iq < row; iq++){
                v.values[ip][iq] = 0.0;
            }
            v.values[ip][ip] = 1.0;
        }
        for (ip = 0; ip < row; ip++){
            b[ip] = d[ip] = matrix1.values[ip][ip];
            z[ip] = 0.0;
        }
        for (i = 1; i <= 50; i++){
            sm = 0.0;
            for (ip = 0; ip < row - 1; ip++)
                for (iq = ip + 1; iq < row; iq++)
                    sm += Math.abs(matrix1.values[ip][iq]);
            if (sm == 0.0){
                break;
            }
            if (i < 4)
                threshold = 0.2 * sm / Math.pow(row, 2);
            else
                threshold = 0.0;
            for (ip = 0; ip < row - 1; ip++){
                for (iq = ip + 1; iq < row; iq++){
                    g = 100.0 * Math.abs(matrix1.values[ip][iq]);
                    if (i > 4 && g <= EPS * Math.abs(d[ip]) && g <= EPS * Math.abs(d[iq])){
                        matrix1.values[ip][iq] = 0.0;
                    } else {
                        if (Math.abs(matrix1.values[ip][iq]) > threshold){
                            h = d[iq] - d[ip];
                            if (g <= EPS * Math.abs(h)){
                                t = matrix1.values[ip][iq] / h;
                            } else {
                                theta = 0.5 * h / matrix1.values[ip][iq];
                                t = 1.0 / (Math.abs(theta) + Math.sqrt(1.0 + Math.pow(theta, 2)));
                                if (theta < 0.0){
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
                            for (j = 0; j < ip; j++){
                                matrix1.rotate(s, tau, j, ip, j, iq);
                            }
                            for (j = ip + 1; j < iq; j++){
                                matrix1.rotate(s, tau, ip, j, j, iq);
                            }
                            for (j = iq + 1; j < row; j++){
                                matrix1.rotate(s, tau, ip, j, iq, j);
                            }
                            for (j = 0; j < row; j++){
                                v.rotate(s, tau, j, ip, j, iq);
                            }
                        }
                    }
                }
            }
            for (ip = 0; ip < row; ip++){
                b[ip] = b[ip] + z[ip];
                d[ip] = b[ip];
                z[ip] = 0.0;
            }
        }
        ArrayList<Eigenvector> result = new ArrayList<>();
        for (i = 0; i < row; i++){
            if (d[i] > 0){
                result.add(new Eigenvector(d[i], v.getColumn(i)));
            }
        }
        Collections.sort(result);
        return result;
    }
}
