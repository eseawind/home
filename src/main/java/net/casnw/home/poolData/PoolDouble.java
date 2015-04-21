package net.casnw.home.poolData;

//@DECLARE@


/**
 * 封装double型对象
 *
 * @author myf@lzb.ac.cn
 * @since 2013-04-09
 * @version 1.0
 *
 */
public class PoolDouble implements Attributeable.Doubleable {

    private double _value=0;

    public PoolDouble() {
    }
    public PoolDouble(double value){
        this._value = value;
    }

    @Override
    public double getValue() {
        return _value;
    }

    @Override
    public void setValue(double value) {
        this._value = value;
    }

    @Override
    public void setValue(String value) {
        this._value = Double.parseDouble(value);
    }
    @Override
    public String toString()
    {
        return Double.toString(_value);
    }
}
