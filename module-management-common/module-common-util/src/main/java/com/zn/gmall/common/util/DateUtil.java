package com.zn.gmall.common.util;

import org.apache.commons.lang.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * 日期操作工具类
 * </p>
 */
public class DateUtil {

    private static final String dateFormat = "yyyy-MM-dd";

    /**
     * 获取两个时间差 单位：秒
     * @param date1
     * @param date2
     * @return
     */
    public Long getTimeSubtract(Date date1, Date date2) {
        return (date1.getTime() - date2.getTime()) / 1000;
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);

    }

    /**
     * 截取比较断两个日期对象的field处的值 。
     * 如果第一个日期小于、等于、大于第二个，则对应返回负整数、0、正整数
     *
     * @param date1 第一个日期对象，非null
     * @param date2 第二个日期对象，非null
     * @param field Calendar中的阈值
     *              <p>
     *              date1 > date2  返回：1
     *              date1 = date2  返回：0
     *              date1 < date2  返回：-1
     */
    public static int truncatedCompareTo(final Date date1, final Date date2, final int field) {
        return DateUtils.truncatedCompareTo(date1, date2, field);
    }

    /**
     * 比对时间大小
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static boolean dateCompare(Date beginDate, Date endDate) {
        return DateUtil.truncatedCompareTo(beginDate, endDate, Calendar.SECOND) != 1;
    }
}
