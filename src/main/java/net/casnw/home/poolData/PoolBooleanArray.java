package net.casnw.home.poolData;

//@DECLARE@
import java.util.ArrayList;
import java.util.List;

/**
 * 封装一维booean数组（boolean[]）对象
 *
 * @author myf@lzb.ac.cn
 * @since 2013-05-06
 * @version 1.0
 *
 */
public class PoolBooleanArray implements Attributeable.BooleanArrayable {

    private List value;
    public int length = 0;

    public PoolBooleanArray() {
        value = new ArrayList();
    }

    public PoolBooleanArray(boolean[] value) {
        setValue(value);
    }

    public PoolBooleanArray(String value) {
        if (value != null & !value.equalsIgnoreCase("")) {
            setValue(value);
        }
    }

    @Override
    public boolean[] getValue() {
        if (this.value != null) {
            boolean[] value = new boolean[this.value.size()];
            for (int i = 0; i < this.value.size(); i++) {
                value[i] = Boolean.parseBoolean(this.value.get(i).toString());
            }
            return value;
        }
        return null;

    }

    @Override
    public boolean getValue(int index) {
        if (index < value.size() && index > -1) {
            return Boolean.parseBoolean(this.value.get(index).toString());
        } else {
            throw new ArrayIndexOutOfBoundsException("index:" + index + " is out of bounds!");
        }
    }

    @Override
    public void setValue(boolean[] value) {
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
                this.value.add(Boolean.parseBoolean(values[i].trim()));
            }
            this.length = values.length;
        } else {
            this.value = null;
        }
    }

    @Override
    public void setValue(int index, boolean value) {
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