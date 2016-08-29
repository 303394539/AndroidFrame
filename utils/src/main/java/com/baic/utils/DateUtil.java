package com.baic.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by baic on 16/5/3.
 */
public class DateUtil {

    private final static int MINUTE = 60 * 1000;
    private final static int HOUR = 60 * 60 * 1000;
    private final static int DAY = 24 * 60 * 60 * 1000;

    private enum Suffix {
        NOW("刚刚"),
        MINUTE("分"),
        HOUR("小时"),
        DAY("天"),
        WEEK("周"),
        MONTH("月"),
        YEAR("年");
        String value;

        Suffix(String value) {
            this.value = value;
        }

        String getValue() {
            return this.value;
        }
    }

    public enum Format {
        FULL("yyyy-MM-dd HH:mm:ss"),
        FULL1("yyyy/MM/dd HH/mm/ss"),
        FULL2("yyyy年MM月dd日 HH时mm分ss秒"),
        yMd("yyyy-MM-dd"),
        yMd1("yyyy/MM/dd"),
        yMd2("yyyy年MM月dd日"),
        yMd3("yyyy:MM:dd"),
        Hms("HH:mm:ss"),
        Hms1("HH/mm/ss"),
        Hms2("HH时mm分ss秒"),
        Hms3("HH-mm-ss");
        SimpleDateFormat sdf;

        Format(String str) {
            this.sdf = new SimpleDateFormat(str);
        }

        SimpleDateFormat getSDF() {
            return this.sdf;
        }
    }

    private DateUtil() {

    }

    public static String getPeriodString(long time) {
        Calendar calendar = Calendar.getInstance();
        long nowDate = new Date().getTime();
        long diffDate = nowDate - time;
        calendar.setTime(new Date(diffDate));
        StringBuilder sb = new StringBuilder();
        if (diffDate >= 0 && diffDate < MINUTE) {
            sb.append(Suffix.NOW.getValue());
        } else if (diffDate >= MINUTE && diffDate < HOUR) {
            sb.append(calendar.get(Calendar.MINUTE));
            sb.append(Suffix.MINUTE.getValue());
        } else if (diffDate >= HOUR && diffDate < DAY) {
            sb.append(calendar.get(Calendar.HOUR));
            sb.append(Suffix.HOUR.getValue());
        } else {
            sb.append(Format.FULL.getSDF().format(time));
        }
        return sb.toString();
    }

    public static String getFormatStr(Format format) {
        return getFormatStr(format, new Date());
    }

    public static String getFormatStr(Format format, Date date) {
        return getFormatStr(format, date.getTime());
    }

    public static String getFormatStr(Format format, long time) {
        return format.getSDF().format(time);
    }
}
