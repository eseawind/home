//@DECLARE@
package net.casnw.home.poolData;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装二维double数组（double[][]）对象
 *
 * @author myf@lzb.ac.cn
 * @since 2013-05-20
 * @version 1.0
 *
 */
public class PoolDouble2DArray implements Attributeable.Double2DArrayable {

    double[][] value = null;
    public int colsNum = 0;
    public int rowsNum = 0;

    public PoolDouble2DArray() {
    }

    public PoolDouble2DArray(String value) {
        if (value != null && !"".equalsIgnoreCase(value)) {
            setValue(value);
        }
    }

    public PoolDouble2DArray(double[][] value) {
        setValue(value);
    }

    @Override
    public double[][] getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    @Override
    public void setValue(double[][] value) {
        if (value != null) {
            this.rowsNum = value.length;
            this.colsNum = value[0].length;
            this.value = new double[this.rowsNum][];
            for (int i = 0; i < this.rowsNum; i++) {
                int col = value[i].length;
                this.value[i] = new double[col];
                for (int j = 0; j < col; j++) {
                    this.value[i][j] = value[i][j];
                }
            }
        }
    }

    @Override
    public double[] getRowValue(int index) {
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
    public double[] getColValue(int index) {
        double[] colValue;
        if (value != null) {
            if (index >= 0 && index < this.colsNum) {
                colValue = new double[this.rowsNum];
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
    public double getCellValue(int row, int col) {
        double cellValue = 0;
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
            throw new NullPointerException("PoolDouble2DArray is null!");
        }
        return cellValue;
    }

    @Override
    public void setCellValue(int row, int col, double cellValue) {
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
            throw new NullPointerException("PoolInt2DArray is null!");
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
            List<double[]> valueList = new ArrayList();

            for (int i = 0; i < this.rowsNum; i++) {
                String[] cellString = rowString[i].split(",");
                int currentColsNum = cellString.length;
                //将最大的一组数字的长度赋给二维数组的列数
                this.colsNum = (currentColsNum > this.colsNum) ? currentColsNum : this.colsNum;
                double[] rowValue = new double[currentColsNum];
                for (int j = 0; j < currentColsNum; j++) {
                    rowValue[j] = Double.parseDouble(cellString[j].trim());
                }
                valueList.add(rowValue);
            }
            //将List<double[]>组装成二维数组。
            double[][] value2D = new double[this.rowsNum][this.colsNum];
            for (int i = 0; i < valueList.size(); i++) {
                double[] temp = valueList.get(i);
                if (temp.length == this.colsNum) {
                    value2D[i] = temp;
                } else if (temp.length < this.colsNum) {
                    double[] temp2 = new double[this.colsNum];
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
        double a;
        
        return colsNum;
       
        
    }

    public double[] convert2DTo1DCol() {
        double[] a = new double[rowsNum * colsNum];
        if (value != null) {
            for (int i = 0; i < colsNum; i++) {
                for (int j = 0; j < rowsNum; j++) {
                    a[rowsNum * i + j] = value[j][i];
                }
            }
        }
        return a;
    }

    public void set1DTo2DArr(double[] value) {
        if (this.value == null) {
            this.value = new double[rowsNum][colsNum];
        }
        for (int i = 0; i < colsNum; i++) {
            for (int j = 0; j < rowsNum; j++) {
                this.value[j][i] = value[rowsNum * i + j];
            }
        }

    }
}
