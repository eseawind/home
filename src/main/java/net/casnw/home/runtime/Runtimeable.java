//@DECLARE@
package net.casnw.home.runtime;

import net.casnw.home.model.Model;

/**
 * 运行环境接口 *
 * @author myf@lzb.ac.cn
 * @since 2013-03-08
 * @version 1.0
 *
 */
public interface Runtimeable {

    /**
     * 加载模型     *
     * @param 属性定义对象
     */
    void loadModel() throws Exception;
    /**
     * 用于运行模型
     */
    void runModel();
    /**
     * 模型清理
     */
    void clearModel();

}
