package net.casnw.home.poolData;

//@DECLARE@


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 封装Date型对象
 *
 * @author myf@lzb.ac.cn
 * @since 2013-05-07
 * @version 1.0
 *
 */
public class PoolDate implements Attributeable.Dateable {

    private Date _value;
    private DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

    public PoolDate() {
        _value = new Date();
    }

    public PoolDate(Date value) {
        _value = value;
    }

    public PoolDate(long value) {
        _value.setTime(value);
    }

    @Override
    public Date getValue() {
        return _value;
    }

    @Override
    public void setValue(Date value) {
        _value = value;
    }

    @Override
    public void setValue(String value) {
        try {
            _value = format1.parse(value);
        } catch (ParseException ex) {
            Logger.getLogger(PoolDate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*
     * 格式化日期转义形式 yyyy-mm-dd 的日期
     */
    @Override
    public String toString() {
        return format1.format(_value);
    }

    @Override
    public void setValue(long value) {
        _value.setTime(value);
    }

    @Override
    public void setTime(long value) {
        _value.setTime(value);
        
    }

    @Override
    public boolean after(Date value) {
        return _value.after(value);
    }

    @Override
    public boolean before(Date value) {
        return _value.before(value);
    }

    @Override
    public int compareTo(Date value) {
        return _value.compareTo(value);
    }
    
}
