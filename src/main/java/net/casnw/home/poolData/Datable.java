//@DECLARE@
package net.casnw.home.poolData;

/**
 * 数据对象接口
 *
 *
 * @author myf@lzb.ac.cn
 * @since 2013-04-09
 * @version 1.0
 *
 */
public interface Datable {

    /**
     * 用于给数据对象赋值
     */
    void setValue(String value);

    /**
     * 获取数据对象的值
     * @return 数据对象的字符串值
     */
    @Override
    String toString();
}
