//@DECLARE@
package net.casnw.home.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 空间参数的读取，定义公用getParameter()方法
 *
 * @author 罗立辉
 * @since 2013-03-08
 * @version 1.0
 */
public class SpatialParameterReader {

    //private String path;
    private static final Log logger = LogFactory.getLog(CSVFileReader.class);
    private File _path_file;
    private BufferedReader reader;
    private String seperator;
    private static Double[][] parameters;
    private int cols;//列数
    private int rows;//行数

    public SpatialParameterReader(String file) throws IOException {
        // 缺省的分隔符
        seperator = "[ ,;]+";

        // 判断目录是否为空
        if (file == null) {
            logger.info(file + " 目录不存在或者文件不存在！");
            throw new IOException(file + " 目录不存在或者文件不存在！");
        } else {
            // 得到绝对路径，指定的目录可是是相对路径或绝对路径
            _path_file = new File(file).getAbsoluteFile();
            if (!_path_file.isFile()) {
                logger.info(file + " 不是文件！");
                throw new IOException(file + " 不是文件！");
            }
            InputStream in = new FileInputStream(file);
            InputStreamReader inReader = new InputStreamReader(in);
            reader = new BufferedReader(inReader);
        }
    }

    /**
     * 设置分隔符
     *
     * @param sep 被设置的分隔符,采用的是Java正则表达式的格式
     */
    public void setSeperator(String regexp) {
        this.seperator = regexp;
    }

    /**
     * 获得参数文件的行数和列数
     *
     * @return
     */
    private List<Integer> getColsRows() {

        List<Integer> colsRows = new ArrayList();
        try {
            String line = null;
            try {
                //先定义2维数组的行和列 while ((line = reader.readLine()) != null) {

                String[] numbers = line.trim().split(seperator);
                System.out.println(numbers);
                if (isNumeric(numbers[0])) {
                    cols = numbers.length;
                    rows++;
                }
                colsRows.add(cols);
                colsRows.add(rows);
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(SpatialParameterReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IllegalStateException e) {
            logger.error(reader + "数据读取失败！", e);
        }
        return colsRows;
    }

    /**
     * 获得全部参数
     *
     * @return
     */
    public Double[][] getParameter() throws IOException {

        try {
            String line;

            //在首行做标记
            reader.mark((int) _path_file.length() + 1);
            //先定义2维数组的行和列
            while ((line = reader.readLine()) != null) {
              
                String[] numbers = line.trim().split(seperator);
                if (isNumeric(numbers[0])) {
                    cols = numbers.length;
                    rows++;
                }
            }
            parameters = new Double[rows][cols];

            rows = 0;
            //从头开始读取
            reader.reset();
            //再把数据赋值给2维数组
            while ((line = reader.readLine()) != null) {
                String[] numbers = line.trim().split(seperator);
                if (isNumeric(numbers[0])) {
                    //System.out.println("numbers " + numbers.length);
                    for (int i = 0; i < numbers.length; i++) {
                        //System.out.println("numbers: " + numbers[0]);
                        //parameters大小没有定义，必须先定义
                        parameters[rows][i] = Double.parseDouble(numbers[i]);

                    }
                    rows++;
                }
            }
            //reader.close();
        } catch (IllegalStateException e) {
            logger.error(reader + " 数据读取失败！", e);
        }
        return parameters;
    }

    /**
     * 通过ID获得其所有参数值
     *
     * @return
     */
    public List<Double> getParaFromID(int ID, Double[][] paras) {

        List<Double> oneRowParas = new ArrayList();
        try {
            List<Integer> IDs = new ArrayList();

            //Double[][] paras = getParameter();

            for (int i = 0; i < paras.length; i++) {
                Double para = paras[i][0];

                int Id = para.intValue();
                //System.out.println(Id);
                if (ID == Id) {

                    for (int j = 0; j < paras[0].length; j++) {

                        Double onePara = paras[i][j];
                        oneRowParas.add(onePara);



                    }

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(SpatialParameterReader.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return oneRowParas;
    }

    /**
     * 判断是否为数字
     *
     * @return Boolean
     */
    private boolean isNumeric(String str) {
        try {
            Double.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*
     public static void main(String[] args) throws IOException {
     SpatialParameterReader test;

     try {
     test = new SpatialParameterReader("test/net/casnw/home/io/parameter/soils.par");
     Double[][] haha = test.getParameter();
     //System.out.println(haha[0][0]);
     //System.out.println(haha[5][5]);
     test.getParaFromID(2, haha);


     } catch (Exception ex) {
     Logger.getLogger(DataReader.class
     .getName()).log(Level.SEVERE, null, ex);
     }
     }
     */
}
