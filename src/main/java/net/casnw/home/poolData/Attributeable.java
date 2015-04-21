//@DECLARE@
package net.casnw.home.poolData;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 数据对象接口
 *
 * @author myf@lzb.ac.cn
 * @since 2013-04-09
 * @version 1.0
 *
 */
public interface Attributeable {

    public interface Integerable extends Datable {

        int getValue();

        void setValue(int value);
    }

    public interface Doubleable extends Datable {

        double getValue();

        void setValue(double value);
    }

    public interface Longable extends Datable {

        long getValue();

        void setValue(long value);
    }

    public interface Floatable extends Datable {

        float getValue();

        void setValue(float value);
    }

    public interface Stringable extends Datable {

        String getValue();

        void setValue(String value);
    }

    public interface Booleanable extends Datable {

        boolean getValue();

        void setValue(boolean value);
    }

    public interface Objectable extends Datable {

        public java.lang.Object getValue();

        public void setValue(java.lang.Object value);
    }

    public interface IntegerArrayable extends Datable {

        int[] getValue();

        int getValue(int index);

        void setValue(int[] value);

        void setValue(int index, int value);
    }

    public interface LongArrayable extends Datable {

        long[] getValue();

        long getValue(int index);

        void setValue(long[] value);

        void setValue(int index, long value);
    }

    public interface FloatArrayable extends Datable {

        float[] getValue();

        float getValue(int index);

        void setValue(float[] value);

        void setValue(int index, float value);
    }

    public interface DoubleArrayable extends Datable {

        double[] getValue();

        double getValue(int index);

        void setValue(double[] value);

        void setValue(int index, double value);
    }

    public interface StringArrayable extends Datable {

        String[] getValue();

        String getValue(int index);

        void setValue(String[] value);

        void setValue(int index, String value);
    }

    public interface BooleanArrayable extends Datable {

        boolean[] getValue();

        boolean getValue(int index);

        void setValue(boolean[] value);

        void setValue(int index, boolean value);
    }

    public interface ObjectArrayable extends Datable {

        Object[] getValue();

        Object getValue(int index);

        void setValue(Object[] value);

        void setValue(int index, Object value);
    }

    public interface Dateable extends Datable {

        Date getValue();

        void setValue(Date value);

        void setValue(long value);

        void setTime(long value);

        boolean after(Date value);

        boolean before(Date value);

        int compareTo(Date value);
    }

    public interface Calendarable extends Datable {

        Attributeable.Calendarable getValue();

        void setValue(long value);

        void setValue(Date value);

        void setValue(String value, String format) throws ParseException;

        void setValue(Calendar cal);

        int compareTo(PoolCalendar cal);

        boolean after(PoolCalendar when);

        boolean before(PoolCalendar when);

        public long getTimeInMillis();
    }

    public interface Integer2DArrayable extends Datable {

        int[][] getValue();

        void setValue(int[][] value);

        int[] getRowValue(int index);

        int[] getColValue(int index);

        int getCellValue(int row, int col);

        void setCellValue(int row, int col, int cellValue);

        int getRowsNum();

        int getColsNum();
    }

    public interface Double2DArrayable extends Datable {

        double[][] getValue();

        void setValue(double[][] value);

        double[] getRowValue(int index);

        double[] getColValue(int index);

        double getCellValue(int row, int col);

        void setCellValue(int row, int col, double cellValue);

        int getRowsNum();

        int getColsNum();
    }

    public interface Float2DArrayable extends Datable {

        float[][] getValue();

        void setValue(float[][] value);

        float[] getRowValue(int index);

        float[] getColValue(int index);

        float getCellValue(int row, int col);

        void setCellValue(int row, int col, float cellValue);

        int getRowsNum();

        int getColsNum();
    }

    public interface Long2DArrayable extends Datable {

        long[][] getValue();

        void setValue(long[][] value);

        long[] getRowValue(int index);

        long[] getColValue(int index);

        long getCellValue(int row, int col);

        void setCellValue(int row, int col, long cellValue);

        int getRowsNum();

        int getColsNum();
    }

    public interface Boolean2DArrayable extends Datable {

        boolean[][] getValue();

        void setValue(boolean[][] value);

        boolean[] getRowValue(int index);

        boolean[] getColValue(int index);

        boolean getCellValue(int row, int col);

        void setCellValue(int row, int col, boolean cellValue);

        int getRowsNum();

        int getColsNum();
    }

    public interface String2DArrayable extends Datable {

        String[][] getValue();

        void setValue(String[][] value);

        String[] getRowValue(int index);

        String[] getColValue(int index);

        String getCellValue(int row, int col);

        void setCellValue(int row, int col, String cellValue);

        int getRowsNum();

        int getColsNum();
    }

    public interface Double3DArrayable extends Datable {

        double[][][] getValue();

        void setValue(double[][][] value);

        double getCellValue(int i, int j, int k);

        void setCellValue(int i, int j, int k, double cellValue);

        void setValue(int i, int j, double[] value);

        double[] getValue(int i, int j);
    }

    public interface Integer3DArrayable extends Datable {

        int[][][] getValue();

        void setValue(int[][][] value);

        int getCellValue(int i, int j, int k);

        void setCellValue(int i, int j, int k, int cellValue);

        void setValue(int i, int j, int[] value);

        int[] getValue(int i, int j);
    }

    public interface Double4DArrayable extends Datable {

        double[][][][] getValue();

        void setValue(double[][][][] value);

        double getCellValue(int i, int j, int k, int l);

        void setCellValue(int i, int j, int k, int l, double cellValue);

        void setValue(int i, int j, int k, double[] value);

        double[] getValue(int i, int j, int k);
    }

    public interface DoubleNDArrayable extends Datable {

        double[] getValue();

        void setValue(double[] value);

        String getCellValue(int[] indexArray);

        void setCellValue(int[] indexArray, String cellValue);

        void setDimension();

        int getDimension();
    }
}
