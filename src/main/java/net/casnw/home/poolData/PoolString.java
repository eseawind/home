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
public class PoolString implements Attributeable.Stringable {

    private String _value;
    public int length;

    public PoolString() {
    }

    public PoolString(String value) {
        this._value = value;
        this.length = value.length();
    }

    @Override
    public String getValue() {
        return _value;
    }

    @Override
    public void setValue(String value) {
        this._value = value;
        this.length = value.length();
    }

    @Override
    public String toString() {
        return this._value;
    }
}
