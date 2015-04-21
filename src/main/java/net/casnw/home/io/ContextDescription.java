//@DECLARE@
package net.casnw.home.io;

import java.util.ArrayList;
import java.util.List;

/**
 * 模型中的容器描述
 *
 * @author 罗立辉
 * @since 2013-04-09
 * @version 1.0
 */
public class ContextDescription extends ModuleDescription {

    private List<ModuleDescription> mouDes = new ArrayList<ModuleDescription>();
   // private List<ContextDescription> contextDes = new ArrayList<>();

    /**
     * 设置模块列表
     *
     * @param mouDes
     */
    public void setModuleList(List<ModuleDescription> mouDes) {
        this.mouDes = mouDes;
    }

    /**
     * 获得变量列表
     *
     * @return
     */
    public List<ModuleDescription> getModuleList() {
        return mouDes;
    }

    /**
     * 添加ModuleDescription对象
     *
     * @return
     */
    public void addModule(ModuleDescription var) {
        mouDes.add(var);
    }

    
}
