package net.casnw.home.poolData;

//@DECLARE@


/**
 * 封装float型对象
 *
 * @author myf@lzb.ac.cn
 * @since 2013-04-09
 * @version 1.0
 *
 */
public class PoolFloat implements Attributeable.Floatable {

    private float _value;

    public PoolFloat() {
    }
    public PoolFloat(float value){
        this._value = value;
    }

    @Override
    public float getValue() {
        return _value;
    }

    @Override
    public void setValue(float value) {
        this._value = value;
    }

    @Override
    public void setValue(String value) {
        this._value = Float.parseFloat(value);
    }
    @Override
    public String toString()
    {
        return Float.toString(_value);
    }
}
