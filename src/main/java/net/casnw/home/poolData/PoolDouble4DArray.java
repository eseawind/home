/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.casnw.home.poolData;

/**
 * 封装四维double数组（double[][][][]）对象
 *
 * @author myf@lzb.ac.cn
 * @since 2013-07-25
 * @version 1.0
 *
 */
public class PoolDouble4DArray implements Attributeable.Double4DArrayable {

    private double[][][][] value;

    public PoolDouble4DArray() {
    }

    public PoolDouble4DArray(double[][][][] value) {
        if (value != null) {
            setValue(value);
        }
    }

    @Override
    public double[][][][] getValue() {
        return value;
    }

    @Override
    public void setValue(double[][][][] value) {
        this.value = value;
    }

    @Override
    public double getCellValue(int i, int j, int k, int l) {
        return this.value[i][j][k][l];
    }

    @Override
    public void setCellValue(int i, int j, int k, int l, double cellValue) {
        this.value[i][j][k][l] = cellValue;
    }

    @Override
    public void setValue(int i, int j, int k, double[] value) {
        this.value[i][j][k] = value;
    }

    @Override
    public double[] getValue(int i, int j, int k) {
        return this.value[i][j][k];
    }

    @Override
    public void setValue(String value) {
    }
}
