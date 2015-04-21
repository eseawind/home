/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.casnw.home.poolData;

/**
 * 封装三维double数组（double[][][]）对象
 *
 * @author myf@lzb.ac.cn
 * @since 2013-06-24
 * @version 1.0
 *
 */
public class PoolDouble3DArray implements Attributeable.Double3DArrayable {

    private double[][][] value;

    public PoolDouble3DArray() {
    }

    public PoolDouble3DArray(double[][][] value) {
        if (value != null) {
            setValue(value);
        }
    }

    @Override
    public double[][][] getValue() {
        return value;
    }

    @Override
    public void setValue(double[][][] value) {
        this.value = value;
    }

    @Override
    public double getCellValue(int i, int j, int k) {
        return this.value[i][j][k];
    }

    @Override
    public void setCellValue(int i, int j, int k, double cellValue) {
        this.value[i][j][k] = cellValue;
    }

    @Override
    public void setValue(int i, int j, double[] value) {
        this.value[i][j] = value;
    }

    @Override
    public double[] getValue(int i, int j) {
        return this.value[i][j];
    }

    @Override
    public void setValue(String value) {
    }
}
