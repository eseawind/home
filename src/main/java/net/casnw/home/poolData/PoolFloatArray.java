package net.casnw.home.poolData;

//@DECLARE@
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 封装一维float数组（float[]）对象
 *
 * @author myf@lzb.ac.cn
 * @since 2013-05-06
 * @version 1.0
 *
 */
public class PoolFloatArray implements Attributeable.FloatArrayable {

    private List value;
    public int length = 0;

    public PoolFloatArray() {
        value = new ArrayList();
    }

    public PoolFloatArray(float[] value) {
        setValue(value);
    }

    public PoolFloatArray(String value) {
        if (value != null & !value.equalsIgnoreCase("")) {
            setValue(value);
        }
    }

    @Override
    public float[] getValue() {
        if (this.value != null && this.value.size() > 0) {
            float[] value = new float[this.value.size()];
            for (int i = 0; i < this.value.size(); i++) {
                value[i] = Float.parseFloat(this.value.get(i).toString());
            }
            return value;
        } else if (this.value != null && this.value.size() == 0 && this.length > 0) {
            float[] value = new float[this.length];
            return value;
        }
        return null;

    }

    @Override
    public float getValue(int index) {
        if (index < value.size() && index > -1) {
            return Float.parseFloat(this.value.get(index).toString());
        } else {
            throw new ArrayIndexOutOfBoundsException("index:" + index + " is out of bounds!");
        }
    }

    @Override
    public void setValue(float[] value) {
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
                this.value.add(Float.parseFloat(values[i].trim()));
            }
            this.length = values.length;
        } else {
            this.value = null;
        }
    }

    @Override
    public void setValue(int index, float value) {
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
