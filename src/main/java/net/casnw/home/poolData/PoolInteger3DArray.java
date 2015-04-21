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
public class PoolInteger3DArray implements Attributeable.Integer3DArrayable {

    private int[][][] value;

    public PoolInteger3DArray() {
    }

    public PoolInteger3DArray(int[][][] value) {
        if (value != null) {
            setValue(value);
        }
    }

    @Override
    public int[][][] getValue() {
        return value;
    }

    @Override
    public void setValue(int[][][] value) {
        this.value = value;
    }

    @Override
    public int getCellValue(int i, int j, int k) {
        return this.value[i][j][k];
    }

    @Override
    public void setCellValue(int i, int j, int k, int cellValue) {
        this.value[i][j][k] = cellValue;
    }

    @Override
    public void setValue(int i, int j, int[] value) {
        this.value[i][j] = value;
    }

    @Override
    public int[] getValue(int i, int j) {
        return this.value[i][j];
    }

    @Override
    public void setValue(String value) {
    }
}
