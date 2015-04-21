//@DECLARE@
package net.casnw.home.io;

import java.util.Arrays;

/**
 * 栅格Raster对象的描述
 *
 * @author 罗立辉
 * @since 2013-05-02
 * @version 1.0
 */
public class Raster {

    protected double[][] data;
    protected double xll;
    protected double yll;
    protected double cellsize;
    protected int cols;
    protected int rows;
    protected String NDATA;
    public static final String DEFAULT_NODATA = "-9999";

    /**
     * 空构造函数
     */
    public Raster() {
    }

    /**
     * 构造函数（从给与的数据中创建栅格对象）
     *
     * @param cellsize
     * @param xll
     * @param yll
     */
    public Raster(double cellsize, double xll, double yll) {
        this();
        setCellsize(cellsize);
        setXll(xll);
        setYll(yll);
    }

    /**
     * 构造函数（从给与的数据中创建栅格对象）
     *
     * @param data
     * @param cellsize
     * @param xll
     * @param yll
     */
    public Raster(double[][] data, double cellsize, double xll, double yll) {
        this(cellsize, xll, yll);
        setData(data);
    }

    /**
     * 构造函数（从给与的数据中创建栅格对象）
     *
     * @param data
     * @param cellsize
     * @param xll
     * @param yll
     */
    public Raster(int[][] data, double cellsize, double xll, double yll) {
        this(cellsize, xll, yll);
        setData(data);
    }

    /**
     * 获得栅格Raster对象
     *
     * @param data
     * @param xll
     * @param yll
     * @param size
     * @return Raster对象
     */
    public static Raster getTempRaster(double[][] data, double xll, double yll, double size) {
        return getTempRaster(data, xll, yll, size, DEFAULT_NODATA);
    }

    /**
     * 获得栅格Raster对象
     *
     * @param data
     * @param xll
     * @param yll
     * @param size
     * @param ndata
     * @return Raster对象
     */
    public static Raster getTempRaster(double[][] data, double xll, double yll, double size, String ndata) {
        Raster a = new Raster();
        a.data = data;
        a.xll = xll;
        a.yll = yll;
        a.cellsize = size;
        a.NDATA = ndata;
        a.rows = data.length;
        a.cols = data[0].length;
        return a;
    }

    /**
     * 设置栅格Raster参数 (rows, columns, corner, cellsize, NDATA)
     *
     * @param other
     */
    public void init(Raster other) {
        xll = other.xll;
        yll = other.yll;
        cellsize = other.cellsize;
        NDATA = other.NDATA;
        setSize(other.getRows(), other.getCols());
    }

    /**
     * 初始化栅格Raster为Double.NaN (i.e. NDATA)
     */
    public void initData() {
        initData(Double.NaN);
    }

    /**
     * 初始化栅格Raster使得数据阵列包含value
     *
     * @param value
     */
    public void initData(double value) {
        data = new double[rows][];
        for (int i = 0; i < rows; i++) {
            data[i] = new double[cols];
            //把value值填充到data数组中
            Arrays.fill(data[i], value);
        }
    }

    /**
     * 返回数据阵列
     *
     * @return the data array
     */
    public double[][] getData() {
        return data;
    }

    /**
     * 根据行列数设置数据阵列的值
     *
     * @param row
     * @param column
     * @param value
     */
    public void setValue(int row, int column, double value) {
        if (row < rows && column < cols) {
            data[row][column] = value;
        }
    }

    /**
     * 获得数据阵列的值
     *
     * @param row
     * @param column
     * @return
     */
    public double getValue(int row, int column) {
        if (row < rows && column < cols) {
            return data[row][column];
        }
        return Double.NaN;
    }

    /**
     * 拷贝给与的数据到数据阵列，并更新数据阵列的行和列（double[][]）
     *
     * @param data
     */
    public void setData(double[][] data) {
        rows = data.length;
        cols = data[0].length;
        initData();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.data[i][j] = data[i][j];
            }
        }
    }

    /**
     * 拷贝给与的数据到数据阵列，并更新数据阵列的行和列(int[][])
     *
     * @param data
     */
    public void setData(int[][] data) {
        rows = data.length;
        cols = data[0].length;
        initData();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.data[i][j] = data[i][j];
            }
        }
    }

    /**
     * 获得最左下角X坐标Xll
     *
     * @return
     */
    public double getXll() {
        return xll;
    }

    /**
     * 设置最左下角X坐标Xll
     *
     * @param xll
     */
    public void setXll(double xll) {
        this.xll = xll;
    }

    /**
     * 获得最左下角Y坐标Yll
     *
     * @return
     */
    public double getYll() {
        return yll;
    }

    /**
     * 设置最左下角Y坐标Yll
     *
     * @param yll
     */
    public void setYll(double yll) {
        this.yll = yll;
    }

    /**
     * 获得网格单元格大小cellsize
     *
     * @return
     */
    public double getCellsize() {
        return cellsize;
    }

    /**
     * 设置网格单元格大小cellsize
     *
     * @param cellsize
     */
    public void setCellsize(double cellsize) {
        this.cellsize = cellsize;
    }

    /**
     * 获得列数
     *
     * @return
     */
    public int getCols() {
        return cols;
    }

    /**
     * 设置列数
     *
     * @return
     */
    public void setCols(int cols) {
        this.cols = cols;
    }

    /**
     * 获得行数
     *
     * @return
     */
    public int getRows() {
        return rows;
    }

    /**
     * 设置行数
     *
     * @return
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * 设置栅格Raster的尺寸（列数和行数），并初始化数据阵列
     *
     * @param nrows
     * @param columns
     */
    public void setSize(int nrows, int columns) {
        this.rows = nrows;
        this.cols = columns;
        initData();
    }

    /**
     * 获得空值NA
     *
     * @return
     */
    public String getNDATA() {
        return NDATA;
    }

    /**
     * 设置控制NA
     *
     * @param nDATA
     */
    public void setNDATA(String nDATA) {
        NDATA = nDATA;
    }

    /**
     * 打印Raster数据
     */
    public void print() {
        System.out.println("Rows: " + rows + " cols: " + cols + " cellsize " + cellsize);
        //按照行列打印二维数组
        for (double[] row : data) {
            //把一行的数值打印出来
            for (double val : row) {
                System.out.print(val + " ");
            }
            //一行打印完后，换行
            System.out.println("");
        }

    }
}
