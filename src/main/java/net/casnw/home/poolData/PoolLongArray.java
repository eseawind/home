//@DECLARE@
package net.casnw.home.poolData;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装一维long数组（long[]）对象
 *
 * @author myf@lzb.ac.cn
 * @since 2013-05-16
 * @version 1.0
 *
 */
public class PoolLongArray implements Attributeable.LongArrayable {

    private List value;
    public int length = 0;

    public PoolLongArray() {
        value = new ArrayList();
    }

    public PoolLongArray(long[] value) {
        setValue(value);
    }

    public PoolLongArray(String value) {
        if (value != null & !value.equalsIgnoreCase("")) {
            setValue(value);
        }
    }

    @Override
    public long[] getValue() {
        if (this.value != null) {
            long[] value = new long[this.value.size()];
            for (int i = 0; i < this.value.size(); i++) {
                value[i] = Long.parseLong(this.value.get(i).toString());
            }
            return value;
        }
        return null;

    }

    @Override
    public long getValue(int index) {
        if (index < value.size() && index > -1) {
            return Long.parseLong(this.value.get(index).toString());
        } else {
            throw new ArrayIndexOutOfBoundsException("index:" + index + " is out of bounds!");
        }
    }

    @Override
    public void setValue(long[] value) {
        this.value = new ArrayList();
        if (value != null) {
            for (int i = 0; i < value.length; i++) {
                this.value.add(value[i]);
            }
            this.length = value.length;

        }
    }

    @Override
    public void setValue(String value) {
        this.value = new ArrayList();
        if (value != null && !value.equalsIgnoreCase("")) {

            String[] values = value.split(",");

            for (int i = 0; i < values.length; i++) {
                this.value.add(Long.parseLong(values[i].trim()));
            }
            this.length = values.length;


        } else {
            this.value = null;
        }

    }

    @Override
    public void setValue(int index, long value) {
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
}