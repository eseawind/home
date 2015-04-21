/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.casnw.home.util;

import net.casnw.home.model.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 提供一些公共类
 *
 * @author myf@lzb.ac.cn
 * @since 2013-05-23
 * @version 1.0
 */
public class OftenTools {

    /**
     * 将一定格式的字符串转换为Date类型的数据
     *
     * @param date
     * @param format
     * @return Date
     */
    public static Date toDate(String date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        try {
            dateFormat.applyPattern(format);
            java.util.Date vDate = dateFormat.parse(date);
            return new Date(vDate.getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将一定格式的字符串转换为Calendar类型的数据
     *
     * @param date
     * @param format
     * @return Calendar
     */
    public static Calendar toCanlendar(String date, String format) {
        Date adate = toDate(date,format);
        Calendar cl = Calendar.getInstance();
        cl.setTime(adate);
        return cl;
    }
}
