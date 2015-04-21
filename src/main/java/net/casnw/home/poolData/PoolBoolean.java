package net.casnw.home.poolData;

//@DECLARE@


/**
 * 封装double型对象
 *
 * @author myf@lzb.ac.cn
 * @since 2013-04-19
 * @version 1.0
 *
 */
public class PoolBoolean implements Attributeable.Booleanable {

    private boolean _value;

    public PoolBoolean() {
    }
    public PoolBoolean(boolean value){
        this._value = value;
    }

    @Override
    public boolean getValue() {
        return _value;
    }

    @Override
    public void setValue(boolean value) {
        this._value = value;
    }

    @Override
    public void setValue(String value) {
        this._value = Boolean.parseBoolean(value);
    }
    @Override
    public String toString()
    {
        return Boolean.toString(_value);
    }
}
