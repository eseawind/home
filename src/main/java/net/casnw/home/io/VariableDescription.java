//@DECLARE@
package net.casnw.home.io;

/**
 * 模型中的变量描述
 * @author 罗立辉
 * @since 2013-04-09
 * @version 1.0
 */
public class VariableDescription extends Description{
    
    private String attributeName;
    private String variableName;
    private String variableValue;
    private String variableType;
    private String variableContext;
    private String variableSize;
    
    /**
     * 空构造函数
     */
    public VariableDescription(){
        
    }
    
    /**
     * 变量构造函数
     * @param attributeName
     * @param variableName
     * @param variableValue
     * @param variableType
     * @param variableContext
     * @param variableSize
     * @throws Exception 
     */
    public VariableDescription(String attributeName, String variableName, String variableValue, String variableType, String variableContext, String variableSize) { 
       this.attributeName = attributeName;
       this.variableName = variableName;
       this.variableValue = variableValue;
       this.variableType = variableType;
       this.variableContext = variableContext;
       this.variableSize = variableSize;
    }
    
    /**
     * 设置变量属性名
     * @param attributeName 
     */
    private void setAttributeName(String attributeName){
        this.attributeName = attributeName;
    }
    
    /**
     * 获得变量属性名
     * @return 
     */
    public String getAttributeName(){
        return attributeName;
    }
    
    /**
     * 设置变量名
     * @param variableName 
     */
    private void setVariableName(String variableName){
        this.variableName = variableName;
    }
    
    /**
     * 获得变量名
     * @return 
     */
    public String getVariableName(){
        return variableName;
    }
    
    /**
     * 设置变量值
     * @param variableValue 
     */
    private void setVariableValue(String variableValue){
        this.variableValue = variableValue;
    }
    
    /**
     * 获得变量值
     * @return 
     */
    public String getVariableValue(){
        return variableValue;
    }
    
    /**
     * 设置变量类型
     * @param variableType 
     */
    private void setVariableType(String variableType){
        this.variableType = variableType;
    }
    
    /**
     * 获得变量类型
     * @return 
     */
    public String getVariableType(){
        return variableType;
    }
    
    /**
     * 设置变量所属容器
     * @param variableContext 
     */
    private void setVariableContext(String variableContext){
        this.variableContext = variableContext;
    }
    
    /**
     * 获得变量所属容器
     * @return 
     */
    public String getVariableContext(){
        return variableContext;
    }
    
    /**
     * 设置变量尺寸
     * @param variableSize 
     */
    private void setVariableSize(String variableSize){
        this.variableSize = variableSize;
    }
    
    /**
     * 获得变量尺寸
     * @return 
     */
    public String getVariableSize(){
        return variableSize;
    }
}
