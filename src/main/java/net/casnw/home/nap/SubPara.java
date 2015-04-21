//@DECLARE@
package net.casnw.home.nap;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * subroutine对象，对应fortran代码中一个subroutine对象
 *
 * @author myf@lzb.ac.cn
 * @since 2013-08-13
 * @version 1.0
 *
 */
public class SubPara {

    private String subName;//subroutine name
    // private List<Para> para ;
    private Map<String, Para> paraMap;//参数map

    /**
     * 构造函数
     */
    public SubPara() {
        subName = "";
        //  List para = new ArrayList<Para>();
        paraMap = new LinkedHashMap<String, Para>();
    }

    /**
     * 参数类
     */
    public class Para {

        //String paraName;//参数的
        String poolType;//数据池中的数据类型
        String subType;//jna interface中变量的数据类型
        int dimensionNum;//数据的维数，0：不是数组；1：一维数组，以此类推
        String poolName;//在数据池中变量的名称,//用r_/i_/c_作为参数的前缀

        Para(String poolType, String subType, int dimensionNum, String poolName) {
            this.poolName = poolName.toLowerCase();
            this.poolType = poolType;
            this.dimensionNum = dimensionNum;
            this.subType = subType;
        }
    }

    /**
     *
     * @return
     */
    public String getSubName() {
        return subName;
    }

    /**
     *
     * @param subName
     */
    public void setSubName(String subName) {
        this.subName = subName;
    }

    /**
     *
     * @return
     */
    public Map<String, Para> getParaMap() {
        return paraMap;
    }

    /**
     *
     * @param paraName
     */
    public void addParaMapName(String paraName) {
        Para para = new Para("", "", 0, "");
        this.paraMap.put(paraName, para);
    }

    /**
     *将一行字符串，转换成一个或一组subPara对象加到paraMap中
     * @param strLine
     * @param subName
     */
    public void addPara(String strLine, String subName) throws Exception {
        //real(kind=4),value :: snow,pre
        //过滤空格
        strLine = strLine.replaceAll(" ", "");
        String type = getType(strLine);
        if (strLine.contains("::")) {
            String preStr = strLine.split("::")[0];
            //分析数据类型长度
            int len = 0;
            int dimension = 0;
            if (preStr.contains("kind")) {
                len = Integer.parseInt(preStr.substring(preStr.indexOf("kind=") + 5, preStr.indexOf(")")));
            }
            //判断是否是数组
            // INTEGER, DIMENSION(m, n) :: arr1
            if (preStr.contains("dimension")) {
                preStr = preStr.substring(preStr.indexOf("dimension("));
                preStr = preStr.substring(preStr.indexOf("(") + 1, preStr.indexOf(")"));
                dimension = preStr.split(",").length;
            }
            String posStr = strLine.split("::")[1];
            String[] subParas = posStr.split(",");
            for (String paraName : subParas) {
                if (paraMap.get(paraName) != null) {
                    String paraType = getJavaParaType(type, len, dimension);
                    String poolType = getPoolDataType(paraType, dimension);
                    String subType = getReferenceType(paraType);
                    String poolName = getPreOfPara(subName) + paraName;
                    Para para = new Para(poolType, subType, dimension, poolName);
                    paraMap.put(paraName, para);
                }
            }
        }
    }

    /**
     *
     * @param strLine
     * @return
     */
    public String getType(String strLine) {
        String type = "";
        if (strLine == null) {
            return "";
        }
        if (strLine.startsWith("real")) {
            type = "real";
        } else if (strLine.startsWith("character")) {
            type = "character";
        } else if (strLine.startsWith("integer")) {
            type = "integer";
        } else if (strLine.startsWith("logical")) {
            type = "logical";
        }
        return type;
    }

    /**
     *
     * @param type 数据类型
     * @param len 数据类型的长度
     * @param dimension 数组维数，=0不是数组
     * @return
     */
    public String getJavaParaType(String type, int len, int dimension) {
        String paraType = "";
        if (type == null) {
            return "";
        }
        if (type.equalsIgnoreCase("integer")) {
            if (len == 0) {
                paraType = "int";
            } else if (len == 4) {
                paraType = "int";
            } else if (len == 8) {
                paraType = "long";
            }
        } else if (type.equalsIgnoreCase("real")) {
            if (len == 0) {
                paraType = "float";
            } else if (len == 4) {
                paraType = "float";
            } else if (len == 8) {
                paraType = "double";
            }
        } else if (type.equalsIgnoreCase("character")) {
            paraType = "String";
        }


        /*else if (type.equalsIgnoreCase("logical")) {
         paraType = "boolean";
         }*///暂不支持logical类型的值传递

        if (dimension > 0 && !"".equalsIgnoreCase(paraType)) {
            paraType = paraType + "[]";
        }
        /*for (int i = 0; i < dimension; i++) {
         paraType = paraType + "[]";
         }*/

        return paraType;
    }

    /**
     *
     * @param type
     * @return
     */
    public String getReferenceType(String type) {
        if (type == null) {
            return "";
        } else if (type.equals("float")) {
            return "FloatByReference";
        } else if (type.equals("double")) {
            return "DoubleByReference";
        } else if (type.equals("int")) {
            return "IntByReference";
        } else {
            return type;
        }


    }

    /**
     *
     * @param subName
     * @return
     */
    public String getPreOfPara(String subName) {
        String pre = "";
        if (subName == null) {
            return "";
        }
        switch (subName.toLowerCase()) {
            case "init":
                pre = "i_";
                break;
            case "run":
                pre = "r_";
                break;
            case "clear":
                pre = "c_";
                break;
        }
        return pre;
    }

    /**
     * 获取模块中所有变量的定义的字符串
     *
     * @return
     */
    public String[] getJavaParaDefine() {
        Iterator subIterator = paraMap.entrySet().iterator();
        int i = 0;
        String[] paraStrs = new String[paraMap.size()];
        while (subIterator.hasNext()) {
            Entry entry = (Entry) subIterator.next();
            Para para = (Para) entry.getValue();
            paraStrs[i++] = "   public " + para.poolType + " " + para.poolName + ";";
        }
        return paraStrs;
    }

    /**
     *java模块中方法中的字符串
     * @return
     */
    public String[] getJavafunStr() {
        String[] str = {"", "", ""};//返回结果字符串
        //str[0]:获取参数值的字符串
        //str[1]：方法调用部分的字符串
        //str[2]：参数赋值部分的字符串
        String tab = "        ";
        Iterator subIterator = paraMap.entrySet().iterator();
        while (subIterator.hasNext()) {
            Entry entry = (Entry) subIterator.next();
            Para para = (Para) entry.getValue();
            String paraName = entry.getKey().toString();

            if (para.subType.endsWith("ByReference")) {
                str[0] = str[0] + tab + para.subType + " " + paraName + "= new " + para.subType + "(this." + para.poolName + ".getValue());\n";
                str[2] = str[2] + tab + "this." + para.poolName + ".setValue( " + paraName + ".getValue());\n";
            } else {
                String getValueStr = ".getValue();\n";
                String setValueStr = ".setValue(";
                if (para.dimensionNum > 1) {
                    getValueStr = ".convert2DTo1DCol();\n";
                    setValueStr = ".set1DTo2DArr(";

                }
                str[0] = str[0] + tab + para.subType + " " + paraName + "=  this." + para.poolName + getValueStr;
                str[2] = str[2] + tab + "this." + para.poolName + setValueStr + paraName + ");\n";
            }
            if ("".equalsIgnoreCase(str[1])) {
                str[1] = paraName;
            } else {
                str[1] = str[1] + "," + paraName;
            }
        }
        return str;
    }

    /**
     *模块中方法中所有参数的串
     * @return
     */
    public String getJavafunPara() {
        Iterator subIterator = paraMap.entrySet().iterator();
        int i = 0;
        String paraStr = "";
        while (subIterator.hasNext()) {
            Entry entry = (Entry) subIterator.next();
            String paraName = entry.getKey().toString();
            Para para = (Para) entry.getValue();
            String str = "";
            if (i == 0) {
                str = para.subType + " " + paraName;
            } else {
                str = ",\n             " + para.subType + " " + paraName;
            }
            paraStr = paraStr + str;
            i++;
        }
        return paraStr;
    }

    /**
     *
     * @param javaDataType
     * @param dimension
     * @return
     */
    public String getPoolDataType(String javaDataType, int dimension) throws Exception {
        String poolDataType = "";
        if (dimension > 1) {
            for (int i = 0; i < dimension - 1; i++) {
                javaDataType = javaDataType + "[]";
            }
        }
        switch (javaDataType) {
            case "int":
                if (dimension != 0) {
                    throw new Exception("dimension is not conrect!");
                } else {
                    poolDataType = "PoolInteger";
                    break;
                }
            case "int[]":
                if (dimension != 1) {
                    throw new Exception("dimension is not conrrect!");
                } else {
                    poolDataType = "PoolIntegerArray";
                    break;
                }
            case "int[][]":
                if (dimension != 2) {
                    throw new Exception("dimension is not conrrect!");
                } else {
                    poolDataType = "PoolInteger2DArray";
                    break;
                }
            case "int[][][]":
                poolDataType = "PoolInteger3DArray";
                break;
            case "long":
                if (dimension != 0) {
                    throw new Exception("dimension is not conrect!");
                } else {
                    poolDataType = "PoolLong";
                    break;
                }
            case "long[]":
                if (dimension != 1) {
                    throw new Exception("dimension is not conrrect!");
                } else {
                    poolDataType = "PoolLongArray";
                    break;
                }
            case "long[][]":
                if (dimension != 2) {
                    throw new Exception("dimension is not conrrect!");
                } else {
                    poolDataType = "PoolLong2DArray";
                    break;
                }
            case "long[][][]":
                if (dimension != 3) {
                    throw new Exception("dimension is not conrrect!");
                } else {
                    poolDataType = "PoolLong3DArray";
                    break;
                }
            case "float":
                if (dimension != 0) {
                    throw new Exception("dimension is not conrrect!");
                } else {
                    poolDataType = "PoolFloat";
                    break;
                }
            case "float[]":
                if (dimension != 1) {
                    throw new Exception("dimension is not conrrect!");
                } else {
                    poolDataType = "PoolFloatArray";
                    break;
                }
            case "float[][]":
                if (dimension != 2) {
                    throw new Exception("dimension is not conrrect!");
                } else {
                    poolDataType = "PoolFloat2DArray";
                    break;
                }
            case "float[][][]":
                if (dimension != 3) {
                    throw new Exception("dimension is not conrrect!");
                } else {
                    poolDataType = "PoolFloat3DArray";
                    break;
                }
            case "double":
                if (dimension != 0) {
                    throw new Exception("dimension is not conrrect!");
                } else {
                    poolDataType = "PoolDouble";
                    break;
                }
            case "double[]":
                if (dimension != 1) {
                    throw new Exception("dimension is not conrrect!");
                } else {
                    poolDataType = "PoolDoubleArray";
                    break;
                }
            case "double[][]":
                if (dimension != 2) {
                    throw new Exception("dimension is not conrrect!");
                } else {
                    poolDataType = "PoolDouble2DArray";
                    break;
                }
            case "double[][][]":
                if (dimension != 3) {
                    throw new Exception("dimension is not conrrect!");
                } else {
                    poolDataType = "PoolFDouble3DArray";
                    break;
                }
            case "double[][][][]":
                if (dimension != 4) {
                    throw new Exception("dimension is not conrrect!");
                } else {
                    poolDataType = "PoolFDouble4DArray";
                    break;
                }
            case "String":
                poolDataType = "PoolString";
                break;
            case "String[]":
                poolDataType = "PoolStringArray";
                break;
            case "String[][]":
                poolDataType = "PoolString2DArray";
                break;
            case "boolean":
                poolDataType = "PoolBoolean";
                break;
            case "boolean[]":
                poolDataType = "PoolBooleanArray";
                break;
            case "boolean[][]":
                poolDataType = "PoolBoolean2DArray";
                break;
        }
        return poolDataType;
    }
}
