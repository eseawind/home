//@DECLARE@
package net.casnw.home.io;

import java.io.*;
import java.text.NumberFormat;
import java.util.regex.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 读取ASCII grid数据格式，并生成Raster对象
 *
 * @author 罗立辉
 * @since 2013-05-02
 * @version 1.0
 */
public class RasterReader {

    private String noData = Raster.DEFAULT_NODATA;
    private Pattern header = Pattern.compile("^(\\w+)\\s+(-?\\d+(.\\d+)?)");
    private File _path_file;
    private BufferedReader reader;
    protected final Log _logger = LogFactory.getLog(getClass());

    /**
     * 构造函数，传递工作目录和文件path
     *
     * @throws java.lang.Exception
     */
    public RasterReader(String path) {
        FileReader input_file_path = null;
        try {
            //判断目录是否为空
            if (path == null) {
                _logger.info(path + " 目录不存在或者文件不存在！");

            } else {
                //得到绝对路径，指定的目录可是是相对路径或绝对路径
                _path_file = new File(path).getAbsoluteFile();
                if (!_path_file.isFile()) {
                    _logger.info(_path_file + " 不是文件！");

                }
            }
            input_file_path = new FileReader(_path_file);
            reader = new BufferedReader(input_file_path);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RasterReader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * 读取ASCII grid文件，然后返回Raster对象
     *
     * @return Raster
     */
    public Raster readRaster() {

        Raster raster = new Raster();
        try {
            while (reader.ready()) {
                String line = reader.readLine();
                Matcher headMatch = header.matcher(line);

                //头文件匹配
                if (headMatch.matches()) {
                    String head = headMatch.group(1);
                    String value = headMatch.group(2);
                    if (head.equalsIgnoreCase("nrows")) {
                        raster.rows = Integer.parseInt(value);
                    } else if (head.equalsIgnoreCase("ncols")) {
                        raster.cols = Integer.parseInt(value);
                    } else if (head.equalsIgnoreCase("xllcorner")) {
                        raster.xll = Double.parseDouble(value);
                    } else if (head.equalsIgnoreCase("yllcorner")) {
                        raster.yll = Double.parseDouble(value);
                    } else if (head.equalsIgnoreCase("NODATA_value")) {
                        raster.NDATA = value;
                    } else if (head.equals("cellsize")) {
                        raster.cellsize = Double.parseDouble(value);
                    } else {
                        System.out.println("Unknown setting: " + line);
                    }
                } else if (line.matches("^-?\\d+.*")) {

                    //从第一行开始处理数据
                    int row = 0;
                    double[][] data = new double[raster.rows][];
                    while (true) {

                        String[] inData = line.split("\\s+");
                        double[] numData = new double[raster.cols];

                        if (inData.length != numData.length) {
                            throw new RuntimeException("列数错误: 期望 "
                                    + raster.cols + " 实际获得 " + inData.length + " 列 \n" + line);
                        }
                        for (int col = 0; col < raster.cols; col++) {
                            if (inData[col].equals(noData)) {
                                numData[col] = Double.NaN;
                            } else {
                                numData[col] = Double.parseDouble(inData[col]);
                            }
                        }
                        data[row] = numData;

                        if (reader.ready()) {
                            line = reader.readLine();
                        } else {
                            break;
                        }
                        row++;
                    }
                    if (row != raster.rows - 1) {
                        _logger.info("错误行数: 期望 " + raster.rows + " 实际获得的 " + (row + 1));
                        //throw new RuntimeException("Wrong number of rows: expected " + raster.rows + " got " + (row + 1));
                    }
                    raster.data = data;
                } else {
                    if (line.length() >= 0 && !line.matches("^\\s*$")) {
                        System.out.println("行数错误: " + line);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(RasterReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return raster;
    }
}
