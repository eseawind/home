/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.casnw.home.poolData;

import java.util.ArrayList;
import java.util.List;
import net.casnw.home.poolData.Attributeable.Objectable;

/**
 * 封装一维对象数组（Object[]）
 *
 * @author myf@lzb.ac.cn
 * @since 2013-06-24
 * @version 1.0
 *
 */
public class PoolObjectArray implements Attributeable.ObjectArrayable {

    private List value;
    public int length = 0;

    public PoolObjectArray() {
        value = new ArrayList();
    }

    public PoolObjectArray(Object[] obj) {
        setValue(obj);
    }

    @Override
    public Object[] getValue() {
        if (this.value != null) {
            Object[] value = new Object[this.value.size()];
            for (int i = 0; i < this.value.size(); i++) {
                value[i] = this.value.get(i);
            }
            return value;
        }
        return null;
    }

    @Override
    public Object getValue(int index) {
         if (index < value.size() && index > -1) {
            return this.value.get(index);
        } else {
            throw new ArrayIndexOutOfBoundsException("index:" + index + " is out of bounds!");
        }
    }

    @Override
    public void setValue(Object[] value) {
         this.value = new ArrayList();
        if (value != null) {
            for (int i = 0; i < value.length; i++) {
                this.value.add(value[i]);
            }
            this.length = value.length;
        }
    }

    @Override
    public void setValue(int index, Object value) {
         if (this.value != null) {
            if (index >= 0) {
                if (index >= this.value.size()) {
                    this.value.add(index, value);
                    this.length = this.value.size();
                } else {
                    this.value.set(index, value);
                }
            } else {
                throw new ArrayIndexOutOfBoundsException("Index:" + index + " is out of bounds!");
            }
        } else {
            throw new NullPointerException(this.getClass().getName() + " is null!");
        }
    }

    @Override
    public void setValue(String value) {
    }
}
