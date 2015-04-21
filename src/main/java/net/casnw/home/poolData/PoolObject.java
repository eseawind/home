/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.casnw.home.poolData;

/**
 * 封装Object对象
 *
 * @author myf@lzb.ac.cn
 * @since 2013-06-24
 * @version 1.0
 *
 */
public class PoolObject implements Attributeable.Objectable {

    private Object value;

    public PoolObject() {
    }

    public PoolObject(Object value) {
        this.setValue(value);
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public void setValue(String value) {
    }
}
