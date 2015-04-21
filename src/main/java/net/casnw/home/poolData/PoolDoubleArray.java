package net.casnw.home.poolData;

//@DECLARE@
import java.util.ArrayList;
import java.util.List;

/**
 * 封装一维double数组（double[]）对象
 *
 * @author myf@lzb.ac.cn
 * @since 2013-05-06
 * @version 1.0
 *
 */
public class PoolDoubleArray implements Attributeable.DoubleArrayable {

    private List value;
    public int length = 0;

    public PoolDoubleArray() {
        value = new ArrayList();
    }

    public PoolDoubleArray(double[] value) {
        setValue(value);
    }

    public PoolDoubleArray(String value) {
        if (value != null & !value.equalsIgnoreCase("")) {
            setValue(value);
        }
    }

    @Override
    public double[] getValue() {
        if (this.value != null) {
            double[] value = new double[this.value.size()];
            for (int i = 0; i < this.value.size(); i++) {
                value[i] = Double.parseDouble(this.value.get(i).toString());
            }
            return value;
        }
        return null;

    }

    @Override
    public double getValue(int index) {
        if (index < value.size() && index > -1) {
            return Double.parseDouble(this.value.get(index).toString());
        } else {
            throw new ArrayIndexOutOfBoundsException("index:" + index + " is out of bounds!");
        }
    }

    @Override
    public void setValue(double[] value) {
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
                this.value.add(Double.parseDouble(values[i].trim()));
            }
            this.length = values.length;

        } else {
            this.value = null;
        }

    }

    @Override
    public void setValue(int index, double value) {
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
