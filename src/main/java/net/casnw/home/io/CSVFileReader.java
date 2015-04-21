package net.casnw.home.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 读取数据，定义公用getNext()
 * 
 * @author 罗立辉
 * @since 2013-03-08
 * @version 1.0
 */
public class CSVFileReader {
	private static final Log logger = LogFactory.getLog(CSVFileReader.class);
	private BufferedReader reader;
	private String seperator;
        private String nextLine = "";

	public CSVFileReader(File file) throws IOException {
		// 缺省的分隔符
		seperator = " +";

		// 判断目录是否为空
		if (file == null) {
			logger.info(file + " 目录不存在或者文件不存在！");
			throw new IOException(file + " 目录不存在或者文件不存在！");
		} else {
			// 得到绝对路径，指定的目录可是是相对路径或绝对路径
			if (!file.isFile()) {
				logger.info(file + " 不是文件！");
				throw new IOException(file + " 不是文件！");
			}
			reader = new BufferedReader(new FileReader(file));
		}
	}

	public CSVFileReader(String path) throws IOException {
		this(new File(path));
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

	/**
	 * 转换字符串成为数字
	 * 
	 * @param parts
	 *            分割好的字符串的各个部分
	 * @return 分析好的数据
	 */
	private List<Double> stringToDouble(String[] parts) {
		List<Double> finalResult = new ArrayList<Double>();
		for (String part : parts) {
			finalResult.add(Double.valueOf(part));
		}
		return finalResult;
	}

	/**
	 * 读取下一行数据
	 * 
	 * @return 如果有数据返回一个List<Double>对象，否则返回null。
	 */
	public List<Double> getNext() {

		try {
			
			while ((nextLine = reader.readLine()) != null) {
				String[] strings = nextLine.split(seperator);
				// 如果第一列是数字，表明该列是数据
				if (isNumeric(strings[0])) {
					return stringToDouble(strings);
				}
			}
			// 这个时候,文件已经读完了
			reader.close();
		} catch (IOException e) {
			logger.error("", e);
		}
		return null;
	}

      /**
       * 判断是否有值
       * @return Boolean 
       */
      public Boolean hasNext() {
         if (nextLine != null) {
             return true;
         }
         else {
             return false;
         }
      }
     
	/**
	 * 设置分隔符
	 * 
	 * @param sep
	 *            被设置的分隔符,采用的是Java正则表达式的格式
	 */
	public void setSeperator(String regexp) {
		this.seperator = regexp;
	}
        
        public static void main(String[] args) throws IOException {
        CSVFileReader test;
             
         try {
            test = new CSVFileReader("test/net/casnw/home/io/input/input_old.dat");
             if (test.hasNext()) {
                 System.out.println("TRUE TRUE TRUE");
                 System.out.println(test.getNext());
                 System.out.println(test.getNext());
              }
                 System.out.println(test.hasNext());
                 System.out.println(test.getNext());
                 System.out.println(test.getNext());
                 System.out.println(test.getNext());
                 System.out.println(test.getNext());
                 System.out.println(test.getNext());
                 System.out.println(test.getNext());
                 System.out.println(test.hasNext());
         } catch (Exception ex) {
             Logger.getLogger(CSVFileReader.class.getName()).log(Level.SEVERE, null, ex);
         }
     }
}
