package Math;

import java.io.Serializable;
import java.util.ArrayList;

public class Vector implements Serializable{

    private int size;
    private ArrayList<Double> values;

    public Vector(ArrayList<Double> values){
        this.values = values;
        size = values.size();
    }

    public Vector(int size, double x){
        this.size = size;
        values = new ArrayList<Double>();
        for (int i = 0; i < size; i++){
            values.add(x);
        }
    }

    public Vector(int size, int index, double x){
        this.size = size;
        values = new ArrayList<Double>();
        for (int i = 0; i < size; i++){
            values.add(0.0);
        }
        values.set(index, x);
    }

    public Vector(double[] values){
        this.values = new ArrayList<Double>();
        for (double value : values) {
            this.values.add(value);
        }
        size = values.length;
    }

    public Vector biased(){
        Vector result = new Vector(0, 0);
        for (Double value:values){
            result.add(value);
        }
        result.insert(0, 1.0);
        return result;
    }

    public void add(double x){
        values.add(x);
        size++;
    }

    public void insert(int pos, double x){
        values.add(pos, x);
        size++;
    }

    public void remove(int pos){
        values.remove(pos);
        size--;
    }

    public void clear(){
        for (int i = 0; i < values.size(); i++){
            values.set(i, 0.0);
        }
    }

    public int maxIndex(){
        int index = 0;
        double max = values.get(0);
        for (int i = 1; i < size; i++){
            if (values.get(i)> max){
                max = values.get(i);
                index = i;
            }
        }
        return index;
    }

    public void sigmoid(){
        for (int i = 0; i < size; i++){
            values.set(i, 1 / (1 + Math.exp(-values.get(i))));
        }
    }

    public Vector skipVector(int mod, int value){
        Vector result = new Vector(0, 0);
        int i = value;
        while (i < size){
            result.add(values.get(i));
            i += mod;
        }
        return result;
    }

    public void add(Vector v) throws VectorSizeMismatch{
        if (size() != v.size()){
            throw new VectorSizeMismatch();
        }
        for (int i = 0; i < size; i++){
            values.set(i, values.get(i) + v.values.get(i));
        }
    }

    public void subtract(Vector v) throws VectorSizeMismatch{
        if (size() != v.size()){
            throw new VectorSizeMismatch();
        }
        for (int i = 0; i < size; i++){
            values.set(i, values.get(i) - v.values.get(i));
        }
    }

    public Vector difference(Vector v) throws VectorSizeMismatch{
        if (size() != v.size()){
            throw new VectorSizeMismatch();
        }
        double[] result = new double[v.size()];
        for (int i = 0; i < size; i++){
            result[i] = values.get(i) - v.values.get(i);
        }
        return new Vector(result);
    }

    public double dotProduct(Vector v) throws VectorSizeMismatch{
        if (size() != v.size()){
            throw new VectorSizeMismatch();
        }
        double result = 0;
        for (int i = 0; i < size; i++){
            result += values.get(i)* v.values.get(i);
        }
        return result;
    }

    public double dotProduct(){
        double result = 0;
        for (int i = 0; i < size; i++){
            result += values.get(i)* values.get(i);
        }
        return result;
    }

    public Vector elementProduct(Vector v) throws VectorSizeMismatch{
        if (size() != v.size()){
            throw new VectorSizeMismatch();
        }
        double[] result = new double[v.size()];
        for (int i = 0; i < size; i++){
            result[i] = values.get(i) * v.values.get(i);
        }
        return new Vector(result);
    }

    public Matrix multiply(Vector v){
        Matrix m = new Matrix(size, v.size);
        for (int i = 0; i < size; i++){
            for (int j = 0; j < v.size; j++){
                m.setValue(i, j, values.get(i) * v.values.get(j));
            }
        }
        return m;
    }

    public void divide(double value){
        for (int i = 0; i < size; i++){
            values.set(i, values.get(i) / value);
        }
    }

    public void multiply(double value){
        for (int i = 0; i < size; i++){
            values.set(i, values.get(i) * value);
        }
    }

    public Vector product(double value){
        Vector result = new Vector(0, 0);
        for (int i = 0; i < size; i++){
            result.add(values.get(i) * value);
        }
        return result;
    }

    public void l1Normalize(){
        double sum = 0;
        for (int i = 0; i < size; i++){
            sum += values.get(i);
        }
        for (int i = 0; i < size; i++){
            values.set(i, values.get(i) / sum);
        }
    }

    public double l2Norm() {
        double sum = 0;
        for (int i = 0; i < size; i++) {
            sum += Math.pow(values.get(i), 2);
        }
        return Math.sqrt(sum);
    }

    public double cosineSimilarity(Vector v) throws VectorSizeMismatch {
        if (size() != v.size) {
            throw new VectorSizeMismatch();
        }
        return dotProduct(v) / l2Norm() / v.l2Norm();
    }

    public int size(){
        return values.size();
    }

    public double getValue(int index){
        return values.get(index);
    }

    public void setValue(int index, double value){
        values.set(index, value);
    }

    public void addValue(int index, double value){
        values.set(index, values.get(index) + value);
    }

}
