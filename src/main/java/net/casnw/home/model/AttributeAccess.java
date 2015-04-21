//@DECLARE@
package net.casnw.home.model;

import net.casnw.home.poolData.Datable;

/**
 * 属性对象
 *
 * @author myf@lzb.ac.cn
 * @since 2013-04-10
 * @version 1.0
 *
 */
public class AttributeAccess {

    private Componentable component;//模块
    private String attributeName;//数据池中变量的名字
    private String compName;//模块中变量的名称
    private String value;//变量的值
    private String dataType;//变量的数据类型
    private String size;//变量的大小：if 数据类型是数组，值为数组大小；if数据类型是字符串，值为字符串长度
    private Contextable context; //容器

    public AttributeAccess(Componentable comp, String attributeName, String compName, String value, String dataType, String size, Contextable context) {
        this.component = comp;
        this.attributeName = attributeName;
        this.compName = compName;
        this.value = value;
        this.dataType = dataType;
        this.size = size;
        this.context = context;
    }

    public AttributeAccess() {
    }

    /**
     * @return the component
     */
    public Componentable getComponent() {
        return component;
    }

    /**
     * @param component the component to set
     */
    public void setComponent(Componentable comp) {
        this.component = comp;
    }

    /**
     * @return the attributeName
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * @param attributeName the attributeName to set
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * @return the compName
     */
    public String getCompName() {
        return compName;
    }

    /**
     * @param compName the compName to set
     */
    public void setCompName(String compName) {
        this.compName = compName;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the dataType
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * @return the context
     */
    public Contextable getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(Contextable context) {
        this.context = context;
    }

    /**
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(String size) {
        this.size = size;
    }
}
