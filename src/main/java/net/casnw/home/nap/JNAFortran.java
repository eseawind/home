//@DECLARE@
package net.casnw.home.nap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author myf@lzb.ac.cn
 * @since 2013-08-13
 * @version 1.0
 *
 */
public class JNAFortran {

    private File srcFile;//fortran 源文件
    private String destDir;//产生java文件的路径
    // private File genFile;//产生的java文件
    private String libname; //dll的名字
    private String packageName;
    private List<SubPara> subparaList;
    static private Pattern linePattern = Pattern.compile("(\\s*\\(([^\\)]*)\\))?[^\\n]*\\n", Pattern.MULTILINE);

    /**
     *
     * @param srcFile
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void execute() throws FileNotFoundException, IOException, Exception {
        FileInputStream fis = new FileInputStream(srcFile);
        FileChannel fc = fis.getChannel();

        // Get a CharBuffer from the source file
        ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        CharsetDecoder cd = Charset.forName("8859_1").newDecoder();
        CharBuffer cb = cd.decode(bb);
        handle(cb.toString());
        fis.close();
        done();
    }

    /**
     *
     * @param srcString
     */
    public void handle(String srcString) throws Exception {
        //按"!@RUN"划分fortran代码
        String annStr = "!@RUN";
        Matcher m = linePattern.matcher(srcString);
        subparaList = new ArrayList<SubPara>();
        System.out.println("srcString="+srcString);
        while (m.find()) {
            if (m.group().trim().startsWith(annStr)) {
                int isPara = 1;//下一行是否是subroutine的参数，1：是；0：不是
                String paraStr = "";
                SubPara subpara = new SubPara();
                String subName = "";
                String headStr = "";
                while (m.find()) {
                    String strLine = m.group().trim().toLowerCase();
                    //取得方法名
                    if (!"".equalsIgnoreCase(strLine) && !strLine.startsWith("!")) {
                        if (strLine.startsWith("subroutine") || strLine.startsWith("function")) {
                            if (strLine.startsWith("subroutine")) {
                                headStr = "subroutine";
                            } else if (strLine.startsWith("function")) {
                                headStr = "function";
                            }
                            if (!"".equalsIgnoreCase(headStr)) {
                                if (strLine.indexOf("(") > -1) {
                                    subName = strLine.substring(strLine.indexOf(headStr) + headStr.length(), strLine.indexOf("("));
                                } else {
                                    subName = strLine.substring(strLine.indexOf(headStr) + headStr.length());
                                }
                                subName = subName.trim().toLowerCase();
                                subpara.setSubName(subName);
                            }
                        }

                        //取得方法的参数定义
                        if (strLine.startsWith("subroutine") || strLine.startsWith("function") || isPara == 1) {
                            //if indexOf(")")==-1,没有结束，需要读取下一行
                            if (strLine.indexOf("(") > -1 && strLine.indexOf(")") > -1) {
                                paraStr = strLine.substring(strLine.indexOf("(") + 1, strLine.indexOf(")"));
                                isPara = 0;
                            } else if (strLine.indexOf("(") > -1 && strLine.indexOf(")") == -1) {
                                paraStr = strLine.substring(strLine.indexOf("(") + 1);
                                isPara = 1;
                            } else if (strLine.indexOf("(") == -1 && strLine.indexOf(")") == -1) {
                                //过滤掉[!$&]
                                paraStr = paraStr + strLine.substring(1).trim();
                                isPara = 1;
                            } else if (strLine.indexOf("(") == -1 && strLine.indexOf(")") > -1) {
                                paraStr = paraStr + strLine.substring(1, strLine.indexOf(")")).trim();
                                isPara = 0;
                            }
                            if (isPara == 0) {
                                String[] paras = paraStr.split(",");

                                for (String para : paras) {
                                    subpara.addParaMapName(para.trim());
                                }
                            }

                        } else if (isPara(strLine)) {
                            if (strLine.contains("!")) {
                                strLine = strLine.substring(0, strLine.indexOf("!")).trim();
                            }
                            subpara.addPara(strLine, subName);
                        } else if (strLine.startsWith("end") && (strLine.trim().length() == 3
                                || strLine.substring(3).trim().startsWith(headStr)
                                || strLine.substring(3).trim().startsWith("!"))) {
                            subparaList.add(subpara);
                            break;
                        }
                    }
                }
            }
        }
    }

    public boolean isPara(String strLine) {
        return strLine.startsWith("real") || strLine.startsWith("character") || strLine.startsWith("integer")
                || strLine.startsWith("logical");
    }

    /**
     *
     * @throws FileNotFoundException
     */
    public void done() throws FileNotFoundException {
        //一个方法产生一个.java文件
        for (int i = 0; i < this.subparaList.size(); i++) {
            File genFile;
            SubPara subpara = this.subparaList.get(i);
            genFile = getGenFile(subpara.getSubName());
            genFile.getParentFile().mkdirs();
            String className = genFile.getName().substring(0, genFile.getName().indexOf('.'));
            String fclassName = className + "_";

            PrintStream w = new PrintStream(genFile);

            w.println("// HOME Native proxy from '" + srcFile.getPath() + "'");
            w.println("// Generated at " + new Date());
            w.println("package " + packageName + ";");
            w.println();
            w.println("import com.sun.jna.ptr.*;");
            w.println("import com.sun.jna.Native;");

            w.println("import java.util.logging.Level;");
            w.println("import java.util.logging.Logger;");
            w.println("import net.casnw.home.model.AbsComponent;");
            w.println("import net.casnw.home.poolData.*;");
            w.println();

            w.println("public class " + className + " extends AbsComponent {");
            w.println();

            //申明交换变量
            String[] paras;

            paras = subpara.getJavaParaDefine();
            for (String para : paras) {
                w.println(para);
            }

            w.println();

            //定义jna接口       
            w.println("    interface " + fclassName + " extends com.sun.jna.Library {");
            w.println("     " + fclassName + " lib=(" + fclassName + ")Native.loadLibrary(\"" + libname + "\"," + fclassName + ".class);");
            //定义接口方法

            String subParaStr = subpara.getJavafunPara();
            subParaStr = "      void " + subpara.getSubName() + "(" + subParaStr + ");";
            w.println(subParaStr);


            w.println(" }");
            w.println();

            //定义模块的方法
            //init()


            String[] str = subpara.getJavafunStr();
            w.println(" @Override");
            //  w.println(" public void " + subpara.getSubName().toLowerCase() + "() throws Exception {");
            w.println(" public void run() throws Exception {");
            w.println(str[0]);
            w.println("        " + fclassName + ".lib." + subpara.getSubName() + "(" + str[1] + ");");
            w.println(str[2]);
            w.println(" }");
            w.println(" }");

            w.println();


        }
    }

    public void setGenDestDir(String destDir) {
        this.destDir = destDir;
    }

    public void setPackageName(String srcFilePath) {
        this.packageName = new File(srcFilePath).getParent().toString();
        this.packageName = this.packageName.replaceAll("////", ".");
    }

    /**
     *
     * @param genFile
     */
    public File getGenFile(String subName) {
        String srcFileName = srcFile.getName();
        File genFile = new File(System.getProperty("user.dir") + "/" + destDir + "/", srcFileName.substring(0, srcFileName.lastIndexOf('.')) + "_" + subName + ".java");
        return genFile;
    }

    /**
     *
     * @param srcFile
     */
    public void setSrcFile(File srcFile) {
        this.srcFile = srcFile;
    }

    /**
     *
     * @param libname
     */
    public void setLibname(String libname) {
        this.libname = libname;
    }
}
