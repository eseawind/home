package net.casnw.home.poolData;

//@DECLARE@
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 封装Calender型对象
 *
 * @author myf@lzb.ac.cn
 * @since 2013-05-06
 * @version 1.0
 *
 */
public class PoolCalendar extends GregorianCalendar implements Attributeable.Calendarable {

    transient public final static String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    transient public final static TimeZone STANDARD_TIME_ZONE = new SimpleTimeZone(0, "GMT");
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private long milliSeconds;

    public PoolCalendar() {
        super();
        this.setTimeInMillis(0);
        initTZ();
    }

    public PoolCalendar(int year, int month, int dayOfMonth) {
        super(year, month, dayOfMonth);
        set(Calendar.MILLISECOND, 0);
        initTZ();
    }

    public PoolCalendar(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
        super(year, month, dayOfMonth, hourOfDay, minute);
        set(Calendar.MILLISECOND, 0);
        initTZ();
    }

    public PoolCalendar(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second) {
        super(year, month, dayOfMonth, hourOfDay, minute, second);
        set(Calendar.MILLISECOND, 0);
        initTZ();
    }

    private void initTZ() {
        this.setTimeZone(STANDARD_TIME_ZONE);
        dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN);
        dateFormat.setTimeZone(STANDARD_TIME_ZONE);
    }

    /**
     *
     * @return
     */
    @Override
    public PoolCalendar getValue() {

        return new PoolCalendar(get(YEAR), get(MONTH), get(DAY_OF_MONTH), get(HOUR_OF_DAY), get(MINUTE), get(SECOND));
    }

    /**
     * 给PoolCalendar数据类型赋值，参数类型 long
     *
     * @param value
     */
    @Override
    public void setValue(long value) {
        this.setTimeInMillis(value);
    }

    /**
     * 给PoolCalendar数据类型赋值，参数类型 String
     *
     * @param String 时间值
     * @param String 时间格式
     */
    @Override
    public void setValue(String value, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        date = sdf.parse(value);
        setTime(date);
        sdf.setTimeZone(this.getTimeZone());
        setTimeInMillis(date.getTime());
        set(Calendar.MILLISECOND, 0);
    }

    /**
     * 给PoolCalendar数据类型赋值，参数类型 Calender
     *
     * @param Calender
     */
    @Override
    public void setValue(Calendar cal) {
        setTimeInMillis(cal.getTimeInMillis());
        set(Calendar.MILLISECOND, 0);
    }

    /**
     * 给PoolCalendar数据类型赋值，参数类型 Date
     *
     * @param Date
     */
    @Override
    public void setValue(Date date) {
        setTime(date);
        setTimeInMillis(date.getTime());
        set(Calendar.MILLISECOND, 0);
    }

    public void setDateFormat(String formatString) {
        dateFormat = new SimpleDateFormat(formatString);
        dateFormat.setTimeZone(this.getTimeZone());
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    @Override
    public boolean after(PoolCalendar calendar) {
        return super.after(calendar);
    }

    @Override
    public boolean before(PoolCalendar calendar) {
        return super.before(calendar);
    }

    @Override
    public int compareTo(PoolCalendar cal) {
        long thisTime = this.getTimeInMillis();
        long calTime = cal.getTimeInMillis();
        return (thisTime > calTime) ? 1 : (thisTime == calTime) ? 0 : -1;
    }

    @Override
    public void setValue(String value) {

        Date date = null;
        try {
            date = dateFormat.parse(value);
        } catch (ParseException ex) {
            Logger.getLogger(PoolCalendar.class.getName()).log(Level.SEVERE, null, ex);
        }

        setTime(date);
        dateFormat.setTimeZone(this.getTimeZone());
        setTimeInMillis(date.getTime());
        set(Calendar.MILLISECOND, 0);
    }

    @Override
    public String toString() {
        return this.toString();
    }
}
