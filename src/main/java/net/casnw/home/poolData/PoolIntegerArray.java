package net.casnw.home.poolData;

//@DECLARE@
import java.util.ArrayList;
import java.util.List;

/**
 * 封装一维整形数组（int[]）对象
 *
 * @author myf@lzb.ac.cn
 * @since 2013-04-27
 * @version 1.0
 *
 */
public class PoolIntegerArray implements Attributeable.IntegerArrayable {

    private List value;
    public int length = 0;

    public PoolIntegerArray() {
        value = new ArrayList();
    }

    public PoolIntegerArray(int[] value) {
        setValue(value);
    }

    public PoolIntegerArray(String value) {
        if (value != null & !value.equalsIgnoreCase("")) {
            setValue(value);
        }
    }

    @Override
    public int[] getValue() {
        if (this.value != null) {
            int[] value = new int[this.value.size()];
            for (int i = 0; i < this.value.size(); i++) {
                value[i] = Integer.parseInt(this.value.get(i).toString());
            }
            return value;
        }
        return null;

    }

    @Override
    public int getValue(int index) {
        if (index < value.size() && index > -1) {
            return Integer.parseInt(this.value.get(index).toString());
        } else {
            throw new ArrayIndexOutOfBoundsException("index:" + index + " is out of bounds!");
        }
    }

    @Override
    public void setValue(int[] value) {
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
                this.value.add(Integer.parseInt(values[i].trim()));
            }
             this.length = values.length;

          
        } else {
            this.value = null;
        }

    }

    @Override
    public void setValue(int index, int value) {
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
            throw new NullPointerException("PoolIntegerArray is null!");
        }
    }

}
