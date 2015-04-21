//@DECLARE@
package net.casnw.home.model;

/**
 * 模块接口，定义init() run() clear()方法
 *
 * @author 邵勇
 * @since 2013-03-08
 * @version 1.0
 *
 */
public interface Componentable {

    /**
     * 初始化方法，在组件第一次被调用是初始化。
     *
     * @throws java.lang.Exception
     */
    void init() throws Exception;

    /**
     * 执行方法，在组件被调度到执行时被调用。
     *
     * @throws java.lang.Exception
     */
    void run() throws Exception;

    /**
     * 清理方法，在组件被销毁时调用。
     *
     * @throws java.lang.Exception
     */
    void clear() throws Exception;

    /**
     * 取得包含这个模块的模型
     *
     * @return 包含这个模块的模型
     */
    Modelable getModel();

    /**
     * 取得包含这个模块的容器
     *
     * @return 包含这个模块的容器
     *
     */
    Contextable getContext();

    /**
     * 设置包含这个模块的模型
     *
     * @param包含这个模块的模型
     */
    void setModel(Modelable model);

    /**
     * 设置包含这个模块的容器
     *
     * @param 包含这个模块的容器
     */
    void setContext(Contextable context);

    /**
     * 设置模块的实例名
     *
     * @param 实例名称
     */
    void setInstanceName(String name);

    /**
     * 获取模型的实例名称
     */
    String getInstanceName();
}
