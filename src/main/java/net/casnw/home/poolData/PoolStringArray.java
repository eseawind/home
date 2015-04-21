package net.casnw.home.poolData;

//@DECLARE@
/**
 * 封装一维String数组（String[]）对象
 *
 * @author myf@lzb.ac.cn
 * @since 2013-05-06
 * @version 1.0
 *
 */
public class PoolStringArray implements Attributeable.StringArrayable {

    private String[] value;
    public static int length;

    public PoolStringArray() {
    }

    public PoolStringArray(String[] value) {
        this.value = value;
        this.length = value.length;
    }

    public PoolStringArray(String value) {
        if (value != null & !value.equalsIgnoreCase("")) {
            setValue(value);
        }
    }

    @Override
    public String[] getValue() {
        return value;

    }

    @Override
    public String getValue(int index) {
        if (index < value.length && index > -1) {
            return value[index];
        } else {
            throw new ArrayIndexOutOfBoundsException("index:" + index + " is out of bounds!");
        }
    }

    @Override
    public void setValue(String[] value) {
        this.value = value;
    }

    @Override
    public void setValue(String value) {
        if (value != null && !value.equalsIgnoreCase("")) {

            String[] values = value.split(",");
            String[] stringValues = new String[values.length];

            for (int i = 0; i < values.length; i++) {
                stringValues[i] = values[i].trim();
            }
            this.value = stringValues;
            this.length = stringValues.length;
        } else {
            this.value = null;
        }

    }

    @Override
    public void setValue(int index, String value) {
        if (this.value != null) {
            if (index >= 0 && index < this.value.length) {
                this.value[index] = value;
            } else {
                throw new ArrayIndexOutOfBoundsException("Index:" + index + " is out of bounds!");
            }
        } else {
            throw new NullPointerException("PoolStringArray is null!");
        }
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
