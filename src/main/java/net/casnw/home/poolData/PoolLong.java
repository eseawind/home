package net.casnw.home.poolData;

//@DECLARE@


/**
 * 封装long型对象
 *
 * @author myf@lzb.ac.cn
 * @since 2013-04-09
 * @version 1.0
 *
 */
public class PoolLong implements Attributeable.Longable{
    private long _value;

    public PoolLong() {
    }
    public PoolLong(long value){
        this._value = value;
    }

    @Override
    public long getValue() {
        return _value;
    }

    @Override
    public void setValue(long value) {
        this._value = value;
    }

    @Override
    public void setValue(String value) {
        this._value = Long.parseLong(value);
    }
    @Override
    public String toString()
    {
        return Long.toString(_value);
    }
}
