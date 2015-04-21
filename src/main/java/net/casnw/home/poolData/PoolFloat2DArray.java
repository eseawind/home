//@DECLARE@
package net.casnw.home.poolData;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装二维float数组（float[][]）对象
 *
 * @author myf@lzb.ac.cn
 * @since 2013-05-20
 * @version 1.0
 *
 */
public class PoolFloat2DArray implements Attributeable.Float2DArrayable {

    float[][] value = null;
    public int colsNum = 0;
    public int rowsNum = 0;

    public PoolFloat2DArray() {
    }

    public PoolFloat2DArray(String value) {
        if (value != null && !"".equalsIgnoreCase(value)) {
            setValue(value);
        }
    }

    public PoolFloat2DArray(float[][] value) {
        setValue(value);
    }

    @Override
    public float[][] getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    @Override
    public void setValue(float[][] value) {
        if (value != null) {
            this.rowsNum = value.length;
            this.colsNum = value[0].length;
            this.value = new float[this.rowsNum][];
            for (int i = 0; i < this.rowsNum; i++) {
                int col = value[i].length;
                this.value[i] = new float[col];
                for (int j = 0; j < col; j++) {
                    this.value[i][j] = value[i][j];
                }
            }
        }
    }

    @Override
    public float[] getRowValue(int index) {
        if (value != null) {
            if (index >= 0 && index < this.rowsNum) {
                return value[index];
            } else {
                throw new ArrayIndexOutOfBoundsException("index:" + index + " is out of bounds!");
            }
        } else {
            return null;
        }
    }

    @Override
    public float[] getColValue(int index) {
        float[] colValue;
        if (value != null) {
            if (index >= 0 && index < this.colsNum) {
                colValue = new float[this.rowsNum];
                for (int i = 0; i < this.rowsNum; i++) {
                    colValue[i] = value[i][index];
                }
                return colValue;
            } else {
                throw new ArrayIndexOutOfBoundsException("index:" + index + " is out of bounds!");
            }
        } else {
            return null;
        }
    }

    @Override
    public float getCellValue(int row, int col) {
        float cellValue = 0;
        if (value != null) {

            boolean rowable = (row >= 0 && row < this.rowsNum);
            boolean colable = (col >= 0 && col < value[row].length);
            if (rowable && colable) {
                cellValue = value[row][col];
            } else {
                if (!rowable) {
                    throw new ArrayIndexOutOfBoundsException("rowIndex:" + row + " is out of bounds!");
                }
                if (!colable) {
                    throw new ArrayIndexOutOfBoundsException("colIndex:" + col + " is out of bounds!");
                }
            }
        } else {
            throw new NullPointerException("PoolInt2DArray is null!");
        }
        return cellValue;
    }

    @Override
    public void setCellValue(int row, int col, float cellValue) {
        if (value != null) {

            boolean rowable = (row >= 0 && row < this.rowsNum);
            boolean colable = (col >= 0 && col < value[row].length);
            if (rowable && colable) {
                value[row][col] = cellValue;
            } else {
                if (!rowable) {
                    throw new ArrayIndexOutOfBoundsException("rowIndex:" + row + " is out of bounds!");
                }
                if (!colable) {
                    throw new ArrayIndexOutOfBoundsException("colIndex:" + col + " is out of bounds!");
                }
            }
        } else {
            throw new NullPointerException("PoolFloat2DArray is null!");
        }
    }

    /**
     * 给二维数组赋值。字符串的格式必须是"1,2,3;4,5,6". 一维数组间用";"分号分隔,数组内用"，"逗号分隔。
     * 数组的列数取最大的一维数组的长度。没有赋值的由cell的值自动填充为一维数组前一个cell的值
     *
     * @param value
     */
    @Override
    public void setValue(String value) {
        if (value != null && !value.equalsIgnoreCase("")) {

            String[] rowString = value.split(";");
            this.rowsNum = rowString.length;
            //定义一个list存放所有的一维数组
            List<float[]> valueList = new ArrayList();

            for (int i = 0; i < this.rowsNum; i++) {
                String[] cellString = rowString[i].split(",");
                int currentColsNum = cellString.length;
                //将最大的一组数字的长度赋给二维数组的列数
                this.colsNum = (currentColsNum > this.colsNum) ? currentColsNum : this.colsNum;
                float[] rowValue = new float[currentColsNum];
                for (int j = 0; j < currentColsNum; j++) {
                    rowValue[j] = Float.parseFloat(cellString[j].trim());
                }
                valueList.add(rowValue);
            }
            //将List<float[]>组装成二维数组。
            float[][] value2D = new float[this.rowsNum][this.colsNum];
            for (int i = 0; i < valueList.size(); i++) {
                float[] temp = valueList.get(i);
                if (temp.length == this.colsNum) {
                    value2D[i] = temp;
                } else if (temp.length < this.colsNum) {
                    float[] temp2 = new float[this.colsNum];
                    for (int j = 0; j < this.colsNum; j++) {
                        if (j < temp.length) {
                            temp2[j] = temp[j];
                        } else {
                            temp2[j] = temp[temp.length - 1];
                        }
                    }
                    value2D[i] = temp2;
                }
            }
            this.value = value2D;
        } else {
            this.value = null;
        }
    }

    @Override
    public int getRowsNum() {
        return rowsNum;
    }

    @Override
    public int getColsNum() {
        return colsNum;
    }

    public float[] convert2DTo1DCol() {
        float[] a = new float[rowsNum * colsNum];
        if (value != null) {
            for (int i = 0; i < colsNum; i++) {
                for (int j = 0; j < rowsNum; j++) {
                    a[rowsNum * i + j] = value[j][i];
                }
            }
        }
        return a;
    }

    public void set1DTo2DArr(float[] value) {
        if (this.value == null) {
            this.value = new float[rowsNum][colsNum];
        }
        for (int i = 0; i < colsNum; i++) {
            for (int j = 0; j < rowsNum; j++) {
                this.value[j][i] = value[rowsNum * i + j];
            }
        }

    }
}
