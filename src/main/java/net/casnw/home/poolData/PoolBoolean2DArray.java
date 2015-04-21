//@DECLARE@
package net.casnw.home.poolData;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装二维boolean数组（boolean[][]）对象
 *
 * @author myf@lzb.ac.cn
 * @since 2013-05-17
 * @version 1.0
 *
 */
public class PoolBoolean2DArray implements Attributeable.Boolean2DArrayable {

    boolean[][] value = null;
    public int colsNum = 0;
    public int rowsNum = 0;

    public PoolBoolean2DArray() {
    }

    public PoolBoolean2DArray(String value) {
        if (value != null && !"".equalsIgnoreCase(value)) {
            setValue(value);
        }
    }

    public PoolBoolean2DArray(boolean[][] value) {
        setValue(value);
    }

    @Override
    public boolean[][] getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    @Override
    public void setValue(boolean[][] value) {
        if (value != null) {
            this.rowsNum = value.length;
            this.colsNum = value[0].length;
            this.value = new boolean[this.rowsNum][];
            for (int i = 0; i < this.rowsNum; i++) {
                int col = value[i].length;
                this.value[i] = new boolean[col];
                for (int j = 0; j < col; j++) {
                    this.value[i][j] = value[i][j];
                }
            }
        }
    }

    @Override
    public boolean[] getRowValue(int index) {
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
    public boolean[] getColValue(int index) {
        boolean[] colValue;
        if (value != null) {
            if (index >= 0 && index < this.colsNum) {
                colValue = new boolean[this.rowsNum];
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
    public boolean getCellValue(int row, int col) {
        boolean cellValue = true;
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
            throw new NullPointerException("PoolBoolean2DArray is null!");
        }
        return cellValue;
    }

    @Override
    public void setCellValue(int row, int col, boolean cellValue) {
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
            List<boolean[]> valueList = new ArrayList();

            for (int i = 0; i < this.rowsNum; i++) {
                String[] cellString = rowString[i].split(",");
                int currentColsNum = cellString.length;
                //将最大的一组数字的长度赋给二维数组的列数
                this.colsNum = (currentColsNum > this.colsNum) ? currentColsNum : this.colsNum;
                boolean[] rowValue = new boolean[currentColsNum];
                for (int j = 0; j < currentColsNum; j++) {
                    rowValue[j] = Boolean.parseBoolean(cellString[j].trim());
                }
                valueList.add(rowValue);
            }
            //将List<boolean[]>组装成二维数组。
            boolean[][] value2D = new boolean[this.rowsNum][this.colsNum];
            for (int i = 0; i < valueList.size(); i++) {
                boolean[] temp = valueList.get(i);
                if (temp.length == this.colsNum) {
                    value2D[i] = temp;
                } else if (temp.length < this.colsNum) {
                    boolean[] temp2 = new boolean[this.colsNum];
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
}
