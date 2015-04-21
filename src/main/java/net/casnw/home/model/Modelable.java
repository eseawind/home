//@DECLARE@

package net.casnw.home.model;

import net.casnw.home.runtime.Runtimeable;

/**
 * 模型接口，定义取得工作环境方法(getRuntime())
 * @author 邵勇
 *
 */
public interface Modelable extends Contextable {

	/**
	 * 取得工作环境方法
	 * @return 工作环境
	 */
	Runtimeable getRuntime();
	
	/**
	 * 设置工作环境方法
	 */
	void setRuntime(Runtimeable runtime);
	
}


