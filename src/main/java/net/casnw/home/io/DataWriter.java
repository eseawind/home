//@DECLARE@
package net.casnw.home.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 数据写入（一行），定义公用writeLine()方法
 *
 * @author 罗立辉
 * @since 2013-03-08
 * @version 1.0
 */
public class DataWriter {

    protected final Log _logger = LogFactory.getLog(getClass());
    private File _path_file;
    private FileWriter fw;

    /**
     * 构造函数，传递工作目录path
     *
     * @param path return
     */
    public DataWriter(String path) {
        try {
            //super(path);
            //判断目录是否为空
            if (path == null) {
                _logger.info(path + " 目录不存在或者文件不存在！");

            } else {
                //得到绝对路径，指定的目录可是是相对路径或绝对路径
                _path_file = new File(path).getAbsoluteFile();
                if (!_path_file.isFile()) {
                    try {
                        //如果指定的文件不存在，就新建一个文件
                        File file = new File(path);
                        file.createNewFile();
                        _logger.info(_path_file + " 不是文件！但是已经新建一个！");
                    } catch (IOException ex) {
                        Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
            fw = new FileWriter(_path_file, false);
        } catch (IOException ex) {
            Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * 写入文件
     *
     * @param str
     * @return void
     */
    public void writeLine(String str) {

        BufferedWriter out = null;

        try {
            //在已有的output文件中的行下面添加新的行
            fw.write(str + "\n");
            fw.flush();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }

    }

    /**
     * 关闭文件写入
     *
     * @return void
     */
    public void close() {
        
        try {
            //关闭文件写入
            fw.close();
            
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }
}
