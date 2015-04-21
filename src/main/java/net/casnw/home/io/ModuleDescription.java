//@DECLARE@
package net.casnw.home.io;

import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;


/**
 * 模型中的模块描述
 * @author 罗立辉
 * @since 2013-04-09
 * @version 1.0
 */
public class ModuleDescription extends Description {
    private List<VariableDescription> varDes;
    private String instanceClass;
    private String instanceName;
    private Element ele;
    
    /**
     * 模块构造函数
     * @param className
     * @param variable
     * @throws Exception 
     */
    public ModuleDescription(String instanceClass, String instanceName, List<VariableDescription> varDes){
        this.instanceClass = instanceClass;
        this.instanceName = instanceName;
        this.varDes = varDes;
    }
    
     public ModuleDescription() {
        this.varDes = new ArrayList<VariableDescription>();
    }

    /**
     * 设置模块类名
     * @param instanceClass 
     */
    public void setInstanceClass(String instanceClass){
        
        this.instanceClass = instanceClass; 
    }
    
    /**
     * 获得模块类名
     * @return 
     */
    public String getInstanceClass(){
        return instanceClass;
    }
    
    /**
     * 设置模块名
     * @param instanceName 
     */
    public void setInstanceName(String instanceName){
        
        this.instanceName = instanceName; 
    }
    
    /**
     * 获得模块名
     * @return 
     */
    public String getInstanceName(){
        return instanceName;
    }
    
    /**
     * 设置变量列表
     * @param variable 
     */
    public void setVariableList(List<VariableDescription> varDes){
        this.varDes = varDes; 
    }
    
    /**
     * 获得变量列表
     * @return 
     */
    public List<VariableDescription> getVariableList(){
        return varDes;
    }
    
    /**
     * 添加VariableDescription对象
     * @return 
     */
    public void addVariable(VariableDescription var){
        varDes.add(var);
    }

    /**
     * @return the ele
     */
    public Element getEle() {
        return ele;
    }

    /**
     * @param ele the ele to set
     */
    public void setEle(Element ele) {
        this.ele = ele;
    }
}
