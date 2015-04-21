//@DECLARE@

package net.casnw.home.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 读取属性，定义公用getParameter()方法
 * @author 罗立辉
 * @since 2013-03-08
 * @version 1.0
 */
public class PropertiesReader {
    
    private File _path_file;
    protected final Log _logger = LogFactory.getLog(getClass());
    //定义属性
    public static String MODEL_CLASS = "model.class";
    public static String CONTEXT_TYPE = "context.type";
    public static String CONTEXT_ITERATE_NUM = "context.iterate.num";
    public static String INPUT_PATH = "input.path";
    public static String OUTPUT_PATH = "output.path";
    
    /**
     * 构造函数，传递工作目录path
     * @param path
     * @return
     */          
    public PropertiesReader(String path) throws Exception {
        //super(path);
        //判断目录是否为空
         if (path == null ){
              _logger.info( path + " 目录不存在或者文件不存在！" );
              throw new IOException( path + " 目录不存在或者文件不存在！" );
         }
         else {
             _path_file = new File(path).getAbsoluteFile();
             if (!_path_file.isFile()) {
                 _logger.info( _path_file + " 不是文件！" );
                 throw new IOException( _path_file + " 不是文件！" );
             }
         }
    }
    
    /**
     * 通过输入的静态常量来获得model.properties文件设置的参数值
     * 并对不符合的参数值进行判断
     * @param name
     * @return String
     */    
    public String getParameter(String name){
        //String path_dir = path.concat("/model.properties");
        //System.out.println(path_dir);
        Properties prop = new Properties();
        FileInputStream fis = null;
        String pro_value;
        
        try {
            fis = new FileInputStream(_path_file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PropertiesReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            prop.load(fis);
        } catch (IOException ex) {
            Logger.getLogger(PropertiesReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        //prop.list(System.out);  
        //System.out.println("\nThe  property: " +  prop.getProperty(name));  
        pro_value = prop.getProperty(name);
        
        //判断MODEL_CLASS是否是有效的包名
        if (name.equals(MODEL_CLASS)){
            if (isValidJavaFullClassName(pro_value)){
                return pro_value;
            }
            else {
                return null;
            }
        }
        
        //判断CONTEXT_TYPE容器类型是否在normal和temporal两者之间，如果不是返回normal
        if (name.equals(CONTEXT_TYPE)){
            if ("normal".equals(pro_value) || "temporal".equals(pro_value)){
                return pro_value;
            }
            else {
                pro_value = "normal";
                return pro_value;
            }
        }
        
        //判断CONTEXT_ITERATE_NUM迭代次数是否为整数，而且要大于0，如果不是设置为1
        if (name.equals(CONTEXT_ITERATE_NUM)){
            try {
                //先判断是否为数字
                int num = Integer.parseInt(pro_value);
                //再判断是否是大于0的整数
                if(num>0&&num%1==0){
                    return pro_value;
                }
                else{
                    pro_value = "1";
                    return pro_value;
                }
            }
            catch (NumberFormatException e) {
                pro_value = "1";
                return pro_value;
                }
        }
        
        //判断INPUT_PATH和OUTPUT_PATH的路径是否存在
        if (name.equals(INPUT_PATH) || name.equals(OUTPUT_PATH)){
            //判断INPUT和OUTPUT两个文件是否存在
            //当属性文件文件和input和output这两个文价夹在同一个目录时成立，否则...
            String io_path = _path_file.getParent().concat("/"+pro_value);
            File io_file = new File(io_path);
            System.out.println(io_path);
            if (io_file.exists()){
                return pro_value;
            }
            else {
                return null;
            }
        }
        
        //判断name不在这个静态变量之中，则返回NULL
        else{
            _logger.info( name + " 不是属性名" );
            //return null;
        }
        return pro_value;
    }
    
    /** 
     * 对单独的Package Name进行校验（MODEL_CLASS） 
     * @param packageName 
     * @return 
     */  
    public boolean isValidJavaIdentifier(String packageName) {  
        //确定是否允许将指定字符作为 Java 标识符中的首字符。  
        if (packageName.length() == 0  
                || !Character.isJavaIdentifierStart(packageName.charAt(0))) {
            return false;
        }  
  
        String name = packageName.substring(1);  
        for (int i = 0; i < name.length(); i++) {
            if (!Character.isJavaIdentifierPart(name.charAt(i))) {
                return false;
            }
        }  
  
        return true;  
    }  
    
     /** 
     *  对 package name进行校验（MODEL_CLASS）
     * @param fullName 
     * @return 
     */  
    public boolean isValidJavaFullClassName(String fullName){  
        if(fullName.equals("")){  
            return false;  
        }  
        boolean flag = true;  
        try{  
            if(!fullName.endsWith(".")){  
                int index = fullName.indexOf(".");  
                if(index!=-1){  
                    String[] str = fullName.split("\\.");  
                    for(String name : str){  
                        if(name.equals("")){  
                            flag = false;  
                            break;  
                        }else if(!isValidJavaIdentifier(name)){  
                            flag = false;  
                            break;  
                        }  
                    }  
                }else if(!isValidJavaIdentifier(fullName)){  
                    flag = false;  
                }  
  
            }else {  
                flag = false;  
            }  
              
        }catch(Exception ex){  
            flag = false;  
        }  
        return flag;  
    }  
    
   /*
    public static void main(String[] args) throws IOException {
        try {
            System.out.println(new PropertiesReader("G:/基于建模框架的生态-水文模型构建与参数模拟/HOME/HOME develop/config/allocation/model.properties").getParameter(INPUT_PATH));
        } catch (Exception ex) {
            Logger.getLogger(PropertiesReader.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
     */
}
