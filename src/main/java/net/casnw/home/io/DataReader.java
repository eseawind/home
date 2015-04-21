//@DECLARE@
package net.casnw.home.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 读取数据，定义公用getNext(), hasNext()方法
 *
 * @author 罗立辉
 * @since 2013-03-08
 * @version 1.0
 */
public class DataReader {

    //private String path;
    protected final Log _logger = LogFactory.getLog(getClass());
    private File _path_file;
    private BufferedReader reader;
    private String nextString = "";
    private String seperator = "[\\s,;]+";

    /**
     * 构造函数，传递工作目录path
     *
     * @throws java.lang.Exception
     */
    public DataReader(String path) {

        // 缺省的分隔符
        //seperator = "[ ,;]+";
        FileReader input_file_path;
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
            Logger.getLogger(DataReader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * 读取输入文件,获得一行，返回Double一维数组
     *
     * @return List<Double>
     */
    public List<Double> getNext() {

        List result = new ArrayList();
        List<Double> finalResult = new ArrayList();
        String[] strings;
        result.clear();

        try {

            while ((nextString = reader.readLine()) != null) {

                //判断第一行是否有变量名
                strings = nextString.split(seperator);
                result = Arrays.asList(strings);
                //System.out.println(result.size());

                //如果第一行是数字，取值不变，否则从第二行开始
                if (isNumeric(result.get(0).toString())) {
                    //System.out.println("OK");

                    for (int i = 0; i < result.size(); i++) {

                        finalResult.add(Double.valueOf((String) result.get(i)));

                    }

                } else {
                    //否则从下一行开始读取
                    nextString = reader.readLine();
                    strings = nextString.split(seperator);
                    result = Arrays.asList(strings);

                    //System.out.println("BAD");
                    for (int i = 0; i < result.size(); i++) {
                        finalResult.add(Double.valueOf((String) result.get(i)));
                    }
                }

                return finalResult;
            }
        } catch (IOException ex) {
            Logger.getLogger(DataReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return finalResult;
    }

    /**
     * 读取输入文件,获得一行，返回String一维数组
     *
     * @return List<String>
     */
    public List<String> getNext2String() {

        List result = new ArrayList();
        List<String> finalResult = new ArrayList();
        String[] strings;
        result.clear();

        try {
            while ((nextString = reader.readLine()) != null) {

                //判断第一行是否有变量名
                strings = nextString.split(seperator);
                result = Arrays.asList(strings);
                //System.out.println(result.size());

                //如果第一行是数字，取值不变，否则从第二行开始
                if (isNumeric(result.get(0).toString())) {
                    for (int i = 0; i < result.size(); i++) {
                        finalResult.add((String) result.get(i));
                        //System.out.println(finalResult);
                    }

                } else {
                    //否则从下一行开始读取
                    nextString = reader.readLine();
                    strings = nextString.split(seperator);
                    result = Arrays.asList(strings);

                    for (int i = 0; i < result.size(); i++) {

                        finalResult.add((String) result.get(i));
                        //System.out.println(finalResult.subList(0, 2));
                    }
                }
                return finalResult;
            }
        } catch (IOException ex) {
            Logger.getLogger(DataReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return finalResult;
    }

    /**
     * 读取输入文件,获得一行，返回String一维数组 获得所有数据
     *
     * @return List<String>
     */
    public List<String> getAllNext2String() {

        List result = new ArrayList();
        List<String> finalResult = new ArrayList();
        String[] strings;
        String strTrim = "";
        result.clear();
        try {
            while ((nextString = reader.readLine()) != null) {
                //判断第一行是否有变量名
                strTrim = nextString.trim();
                strings = strTrim.split(seperator);

                result = Arrays.asList(strings);

                for (int i = 0; i < result.size(); i++) {
                    finalResult.add((String) result.get(i));
                }
                return finalResult;
            }
        } catch (IOException ex) {
            Logger.getLogger(DataReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return finalResult;
    }

    /**
     * 读取输入文件，获得一行，返回Object一维数组
     *
     * @return List<Double>
     */
    public List<Object> getNext2Object() {

        List result = new ArrayList();
        List<Object> finalResult = new ArrayList();
        String[] strings;
        result.clear();

        try {

            while ((nextString = reader.readLine()) != null) {

                //判断第一行是否有变量名
                strings = nextString.split(seperator);
                result = Arrays.asList(strings);

                //如果第一行是数字，取值不变，否则从第二行开始
                if (isNumeric(result.get(0).toString())) {
                    //System.out.println("OK");

                    for (int i = 0; i < result.size(); i++) {
                        finalResult.add((String) result.get(i));
                        //System.out.println(finalResult);
                    }

                } else {
                    //否则从下一行开始读取
                    nextString = reader.readLine();
                    strings = nextString.split(seperator);
                    result = Arrays.asList(strings);

                    //System.out.println("BAD");
                    for (int i = 0; i < result.size(); i++) {
                        finalResult.add((String) result.get(i));
                    }

                }

                return finalResult;
            }
        } catch (IOException ex) {
            Logger.getLogger(DataReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return finalResult;
    }

    /**
     * 判断是否有下一行
     *
     * @return Boolean
     */
    public Boolean hasNext() {
        /*
         boolean empty = true;
         
         for (Object item : getNext()) {
         if (item != null) {
         empty = false;
         break;
         }
         }
         return !empty;
         */
        if (nextString != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 把获得的数据转换成日期格式 此函数仅仅只是有日期存在的情况，还支持年和月份
     * 天时间尺度，如输入的日期格式为：2013-07-23，格式字符串为“2013-07-23”
     * 月时间尺度，如输入的日期格式为：2013-07，格式字符串为“2013-07” 年时间尺度，如输入的日期格式为：2013，格式字符串为“2013”
     * 字符 意义	例子 y	年	yyyy 1996,1997,1998 M	第几月 MM 01,09,12 d	第几天 dd 01,15,30 H
     * 第几小时 (0-23) HH 00,11,23 k	第几小时 (1-24) kk 01,12,24 m	第几分钟	mm 01,30,60
     * 如果输入文件的日期为"2013-10-01",则格式format为"yyyy-MM-dd"
     * 如果输入文件的日期为"2013.10.01",则格式format为"yyyy.MM.dd"
     * 如果输入文件的日期为"2013.10.01",则格式format为"yyyy.MM.dd"
     * 如果输入文件的日期为"01/10/2013",则格式format为"dd/MM/yyyy"
     *
     * @return Date
     */
    public Date DateFormat(String date_obj, String format) {
        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(date_obj);
        } catch (ParseException ex) {
            Logger.getLogger(DataReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    /**
     * 把获得的数据转换成日期格式 如果有输入数据有时间，则忽略秒，模拟计算目前最小分辨率为分钟 则需要把日期和时间的字符以空格连接在一起
     * 如日期为：2013-07-23 时间为：23:00，格式连接起来就是“2013-07-23
     * 23:00”,则格式format为"yyyy-MM-dd HH:mm" 如果输入文件的时间为"23:00",
     * 当从0开始计数的话，则格式format为"HH:mm" 当从1开始计数的话，则格式format为"kk:mm"
     *
     * @return Date
     */
    public Date DateTimeFormat(String Date_obj, String Time_obj, String format) {
        String Date_Time_obj;
        Date date = null;

        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            Date_Time_obj = Date_obj.concat(" " + Time_obj);
            date = dateFormat.parse(Date_Time_obj);
        } catch (ParseException ex) {
            Logger.getLogger(DataReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    /**
     * 判断是否为数字
     *
     * @return Boolean
     */
    public boolean isNumeric(String str) {
        try {
            //Integer.parseInt(str);
            Double.parseDouble(str);
            //Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block 
            return false;
        }
    }

    /*
     public static void main(String[] args) throws IOException {
     DataReader DR = new DataReader("test/net/casnw/home/io/input/ahum.dat");
     DataReader DR1 = new DataReader("test/net/casnw/home/io/input/input.dat");
     List<Double> result = DR1.getNext();
     List<String> result1 = DR.getNext2String();
     List<Object> result2 = DR.getNext2Object();
     System.out.println(result1.size());
     System.out.println(result1.get(0));
     System.out.println(DR.DateFormat("2013-07-24", "yyyy-MM-dd"));
     System.out.println(DR.DateFormat(result1.get(0), "dd.MM.yyyy"));
     System.out.println(DR.DateTimeFormat(result1.get(0), result1.get(1), "dd.MM.yyyy HH:mm"));
     }
     */
}
