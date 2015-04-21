//@DECLARE@
package net.casnw.home.io;

import java.io.*;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 写入Raster对象，并生成ASCII grid数据格式
 *
 * @author 罗立辉
 * @since 2013-05-02
 * @version 1.0
 */
public class RasterWriter {

    private NumberFormat cellFormat = null;
    private String nodataString = Raster.DEFAULT_NODATA;
    protected final Log _logger = LogFactory.getLog(getClass());
    private PrintStream reader;

    /**
     * RasterWriter构造函数
     */
    public RasterWriter(String filename) {
        try {
            File f = new File(filename);

            if (f.exists()) {
                f.delete();
            }

            if (!f.createNewFile()) {
                _logger.info(filename + " 创建栅格文件不成功!！");
                //throw new RuntimeException( "创建栅格文件不成功!");
            }

            reader = new PrintStream(f);

        } catch (IOException ex) {
            Logger.getLogger(RasterWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 写入Ratser对象到给定的文件名
     *
     * @param filename
     * @param r
     * @throws IOException
     */
    public void writeRaster(Raster r) throws IOException {

        //写入栅格文件的头文件信息
        reader.println("ncols " + r.getCols());
        reader.println("nrows " + r.getRows());
        reader.println("xllcorner " + r.getXll());
        reader.println("yllcorner " + r.getYll());
        reader.println("cellsize " + r.getCellsize());
        reader.println("NODATA_value " + r.getNDATA());
        reader.println(" ");

        for (double[] row : r.getData()) {
            StringBuffer b = new StringBuffer();
            for (int i = 0; i < row.length; i++) {
                if (Double.isNaN(row[i])) {
                    b.append(r.getNDATA());
                } else if (cellFormat != null) {
                    b.append(cellFormat.format(row[i]));
                } else {
                    b.append(row[i]);
                }

                if (i < row.length - 1) {
                    b.append(" ");
                }
            }
            reader.println(b);
            b.setLength(0);
        }
        reader.close();
    }

    /**
     * 快捷函数：直接从Raster各种变量中输入（作为Raster，但未生成Ratser对象）
     *
     * @param filename
     * @param data
     * @param xll
     * @param yll
     * @param size
     * @param ndata
     * @throws IOException
     */
    private void writeRaster(double[][] data, double xll, double yll, double size, String ndata) throws IOException {
        writeRaster(Raster.getTempRaster(data, xll, yll, size, ndata));
    }

    /**
     * 写入ASCII grid头文件
     *
     * @param filename
     * @param r
     * @throws IOException
     */
    private void writeRasterHeader(String filename, Raster r) throws IOException {
        File f = new File(filename);

        PrintStream o = new PrintStream(f);
        o.println("ncols " + r.getCols());
        o.println("nrows " + r.getRows());
        o.println("xllcorner " + r.getXll());
        o.println("yllcorner " + r.getYll());
        o.println("cellsize " + r.getCellsize());
        o.println("NODATA_value " + r.getNDATA());
        o.println(" ");
    }

    /**
     * 给已带头文件的Raster文件在最后一行填充数据，当数据超过列数的时候，在下一行填充
     */
    private void writeRasterWithHeader(String filename, Double data, int cols) throws IOException {
        File f = new File(filename);

        FileWriter fw = new FileWriter(f, true);
        BufferedWriter bw = new BufferedWriter(fw);

        //读取文本
        FileReader fr = new FileReader(f);
        BufferedReader breader = new BufferedReader(fr);
        String s;

        //判断是否是最后一行，如果是取值
        int lineNumber = 0;
        String lastNumberValue = "";
        while ((s = breader.readLine()) != null) {
            lineNumber = lineNumber + 1;
            lastNumberValue = s;
        }

        String[] strings = lastNumberValue.split(" +");
        int dataLength = strings.length;

        //判断最后一行的字符串的个数，如果超过列数，则从下一行进行填充
        if (dataLength < cols) {
            bw.append(data.toString());
            bw.append(" ");
            bw.flush();
            bw.close();
        } else {
            bw.newLine();
            bw.append(data.toString());
            bw.append(" ");
            bw.flush();
            bw.close();
        }
    }
}
