//@DECLARE@

package net.casnw.home.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 数据写入（一行），定义公用writeLine()方法
 * 
 * @author 罗立辉
 * @since 2013-03-08
 * @version 1.0
 */
public class CSVFileWriter {
	private static final Log _logger = LogFactory.getLog(CSVFileWriter.class);
	private static final String NEW_LINE = "\n";
	private boolean firstLine;
	private FileWriter fw;
	private String seperator;

	/**
	 * 构造函数
	 * 
	 * @param path
	 *            输出文件的路径
	 * @throws IOException
	 *             文件打开错误时会抛出IO异常
	 */
	public CSVFileWriter(String path) throws IOException {
		seperator = " ";
		firstLine = true;
		// 判断目录是否为空
		if (path == null) {
			_logger.info("送入的文件参数为空!");
			throw new FileNotFoundException("送入的文件参数为空!");
		} else {
			// 得到绝对路径，指定的目录可是是相对路径或绝对路径
			File file = new File(path);
			if (!file.exists()) {
				// 如果指定的文件不存在，就新建一个文件
				file.createNewFile();
				_logger.info(file + " 不是文件！但是已经新建一个！");
			}
			fw = new FileWriter(file, false);
		}
	}

	private String format(List<?> values) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (Object obj : values) {
			if (!first) {
				builder.append(seperator);
				builder.append(obj.toString());
			} else {
				first = false;
				builder.append(obj.toString());
			}
		}
		builder.append(NEW_LINE);
		return builder.toString();
	}

	private String format(Object[] values) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (Object obj : values) {
			if (!first) {
				builder.append(seperator);
				builder.append(obj.toString());
			} else {
				first = false;
				builder.append(obj.toString());
			}
		}
		return builder.toString();
	}

	private void writeLine(String str) throws IOException {
		if (!firstLine) {
			fw.write(NEW_LINE);
		}else{
			firstLine=false;
		}
		fw.write(str);
	}

	public void close() throws IOException {
		fw.close();
	}

	/**
	 * 设置字段分隔符
	 * 
	 * @param sep
	 *            字段分隔符
	 */
	public void setSeperator(String sep) {
		this.seperator = sep;
	}

	public void write(List<Object> line) throws IOException {
		String str = format(line);
		writeLine(str);
	}

	public void write(Object[] line) throws IOException {
		String str = format(line);
		writeLine(str);
	}
}
