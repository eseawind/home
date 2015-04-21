//@DECLARE@
package net.casnw.home.model;

import java.util.List;
import java.util.Map;
import net.casnw.home.poolData.Datable;

/**
 * 容器接口，定义设置模块方法(setComponent())
 *
 * @author 邵勇
 * @since 2013-03-08
 * @version 1.0
 *
 */
public interface Contextable extends Componentable {

    /**
     * 设置放入容器的模块
     *
     * @param 放入容器的模块
     */
    void setComponents(List<Componentable> components);

    /**
     * 取得容器中的所有模块
     *
     * @return 容器中的所有模块
     */
    List<Componentable> getComponents();

    /**
     * 取得容器中的一个模块
     *
     * @param 模块名称
     * @return 容器中的一个模块
     */
    Componentable getComponent(String name);

    /**
     * 取得容器中的所有的属性对象
     *
     * @return 容器中的所有属性对象
     */
    List<AttributeAccess> getAttributeAccess();

    /**
     * 给容器中添加模块
     *
     * @param 放入容器中的模块
     */
    void addComponent(Componentable component);

    /**
     * 给容器中添加属性对象
     *
     * @param 放入容器的属性对象
     */
    void addAccess(AttributeAccess attributeAccess);

    /**
     * 给容器中添加静态属性对象
     *
     * @param 放入容器的属性对象 attributeAccess = AttributeAccess(attributeName,context)
     */
    void addStaticAccess(AttributeAccess attributeAccess);

    /**
     * 初始化容器中数据池与模块参数的对应关系
     *
     */
    void initAccessors();

    /**
     * 获取模块的数据池
     *
     */
    Map<String, Datable> getDataPool();

    /**
     * 取得容器中的所有的属性对象的Map
     *
     * @return 容器中的所有属性对象
     */
    Map<String, AttributeAccess> getAttributeAccessMap();
}
