package net.casnw.home.poolData;

//@DECLARE@


/**
 * 封装int型对象
 *
 * @author myf@lzb.ac.cn
 * @since 2013-04-09
 * @version 1.0
 *
 */
public class PoolInteger implements Attributeable.Integerable {

    private int _value;

    public PoolInteger() {
    }
    public PoolInteger(int value){
        this._value = value;
    }

    @Override
    public int getValue() {
        return _value;
    }

    @Override
    public void setValue(int value) {
        this._value = value;
    }

    @Override
    public void setValue(String value) {
        this._value = Integer.parseInt(value);
    }
    @Override
    public String toString()
    {
        return Integer.toString(_value);
    }
}
