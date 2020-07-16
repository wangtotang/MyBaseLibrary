package com.tang.util.convert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具类
 *
 * @author HWJ
 */
public class DateUtils {

    public final static String FORMAT_YEAR = "yyyy";
    public final static String FORMAT_MONTH_DAY = "MM月dd日";

    public final static String FORMAT_DATE = "yyyy-MM-dd";
    public final static String FORMAT_TIME = "HH:mm";
    public final static String FORMAT_MONTH_DAY_TIME = "MM月dd日  hh:mm";

    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_DATE1_TIME = "yyyy/MM/dd HH:mm:ss";
    public final static String FORMAT_DATE_TIME_SECOND = "yyyy/MM/dd HH:mm:ss";

    private static final SimpleDateFormat sdf = new SimpleDateFormat();
    private static final int YEAR = 365 * 24 * 60 * 60;// 年
    private static final int MONTH = 30 * 24 * 60 * 60;// 月
    private static final int DAY = 24 * 60 * 60;// 天
    private static final int HOUR = 60 * 60;// 小时
    private static final int MINUTE = 60;// 分钟

    /**
     * 根据时间戳获取描述性时间，如3分钟前，1天前
     *
     * @param timestamp 时间戳 单位为毫秒
     * @return 时间字符串
     */
    public static String getDescriptionTimeFromTimestamp(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
        System.out.println("timeGap: " + timeGap);
        String timeStr;
        if (timeGap > YEAR) {
            timeStr = timeGap / YEAR + "年前";
        } else if (timeGap > MONTH) {
            timeStr = timeGap / MONTH + "个月前";
        } else if (timeGap > DAY) {// 1天以上
            timeStr = timeGap / DAY + "天前";
        } else if (timeGap > HOUR) {// 1小时-24小时
            timeStr = timeGap / HOUR + "小时前";
        } else if (timeGap > MINUTE) {// 1分钟-59分钟
            timeStr = timeGap / MINUTE + "分钟前";
        } else {// 1秒钟-59秒钟
            timeStr = "刚刚";
        }
        return timeStr;
    }


    /**
     * 根据时间戳获取描述性时间，如刚刚，x分钟前，昨天，月-日，月-日-年
     *
     * @param timestamp
     * @return
     */
    public static String getDescriptionTimeFromTimestamp2(long timestamp) {

        Date date = timestampToDate(timestamp, FORMAT_DATE_TIME);
        int ret = judgeCurrentDate(date);
        if (ret == 2) {
            return "昨天";
        }
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
        System.out.println("timeGap: " + timeGap);
        String timeStr;
        if (timeGap > YEAR) {
            //timeStr = timeGap / YEAR + "年前";
            timeStr = timestampToDateStr(timestamp, "MM-dd-yyyy");
        } else if (timeGap > MONTH) {
            //timeStr = timeGap / MONTH + "个月前";
            timeStr = timestampToDateStr(timestamp, "MM-dd");
        } else if (timeGap > DAY) {// 1天以上,显示日期
            //timeStr = timeGap / DAY + "天前";
            timeStr = timestampToDateStr(timestamp, "MM-dd");
        } else if (timeGap > HOUR) {// 1小时-24小时,显示格式为HH:mm
            //timeStr = timeGap / HOUR + "小时前";
            timeStr = timestampToDateStr(timestamp, FORMAT_TIME);
        } else if (timeGap > MINUTE) {// 1分钟-59分钟
            timeStr = timeGap / MINUTE + "分钟前";
        } else {// 1秒钟-59秒钟
            timeStr = "刚刚";
        }
        return timeStr;
    }

    /**
     * 根据时间戳获取描述性时间，如刚刚，x分钟前，昨天，月-日，月-日-年
     *
     * @param date
     * @return
     */
    public static String getDescriptionTimeFromTimestamp2(Date date) {
        long timestamp = date.getTime();
        return getDescriptionTimeFromTimestamp2(timestamp);
    }

    /**
     * 根据时间戳获取描述性时间，如刚刚，x分钟前，昨天，月-日，月-日-年
     *
     * @param dateStr yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getDescriptionTimeFromTimestamp2(String dateStr) {
        Date date = stringToDate(dateStr, FORMAT_DATE_TIME);
        if (date != null) {
            long timestamp = date.getTime();
            return getDescriptionTimeFromTimestamp2(timestamp);
        }
        return "";

    }

    /**
     * 时间戳转换为字符串(yyyy-MM-dd HH:mm:ss)
     *
     * @param timestamp
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String timestampToDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }

   /* public static String timestampToDateStr(long timestamp,String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd = sdf.format(new Date(timestamp));
        return sd;
    }*/

    /**
     * 时间戳转换自定义格式日期
     *
     * @param timestamp
     * @param pattern
     * @return
     */
    public static String timestampToDateStr(long timestamp, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(timestamp));
    }

    /**
     * 时间戳转换自定义格式日期
     *
     * @param timestamp 时间戳
     * @param pattern
     * @return
     */
    public static Date timestampToDate(long timestamp, String pattern) {
        Date dateOld = new Date(timestamp); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, pattern); // 把date类型的时间转换为string
        return stringToDate(sDateTime, pattern);
    }

    /**
     * 将字符串转为时间戳
     *
     * @param date "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static long dateStrToTimestamp(String date) {
        long re_time = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try {
            d = sdf.parse(date);
            re_time = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }

    /**
     * 将字符串转为时间戳
     *
     * @param date "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static long dateToTimestamp(Date date) {
        return date.getTime();
    }


    /**
     * date类型转换为定义格式日期
     *
     * @param data
     * @param pattern
     * @return
     */
    public static String dateToString(Date data, String pattern) {
        return new SimpleDateFormat(pattern).format(data);
    }

    /**
     * string类型转换为date类型
     *
     * @param strTime strTime的时间格式必须要与formatType的时间格式相同
     * @param pattern
     * @return
     */
    public static Date stringToDate(String strTime, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * string类型转换为时间戳
     *
     * @param strTime strTime的时间格式和formatType的时间格式必须相同
     * @param pattern
     * @return
     */
    public static long stringToTimestamp(String strTime, String pattern) {
        Date date = stringToDate(strTime, pattern); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            return dateToTimestamp(date);
        }
    }

    /**
     * 获取当前日期的指定格式的字符串
     *
     * @param pattern 指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM:ss"
     * @return
     */
    public static String getCurrentTime(String pattern) {
        if (pattern == null || pattern.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE_TIME);
        } else {
            sdf.applyPattern(pattern);
        }
        return sdf.format(new Date());
    }


    /**
     * 将时间戳转为“yy-MM-dd HH:mm”格式字符串
     *
     * @param time
     * @return
     */
    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
        return format.format(new Date(time));
    }

    /**
     * 将时间戳转为“HH:mm”格式字符串
     *
     * @param time
     * @return
     */
    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    /**
     * 获取时间的相对性（今天，昨天，前天）
     *
     * @param timeStamp 毫秒
     * @return
     */
    public static String getDetailTime(long timeStamp) {
        String result;
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(timeStamp);
        int temp = Integer.parseInt(sdf.format(today))
                - Integer.parseInt(sdf.format(otherDay));

        switch (temp) {
            case 0:
                result = "今天 " + getHourAndMin(timeStamp);
                break;
            case 1:
                result = "昨天 " + getHourAndMin(timeStamp);
                break;
            case 2:
                result = "前天 " + getHourAndMin(timeStamp);
                break;
            default:
                result = getTime(timeStamp);
                break;
        }

        return result;
    }

    /**
     * 判断日期是今天还是昨天
     *
     * @param time yyyy-MM-dd HH:mm:ss
     * @return 0-格式错误 1-今天 2-昨天 3-日期
     */
    public static int judgeCurrentDate(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (time == null || "".equals(time)) {
            return 0;
        }
        Date date;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;

        }

        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance(); // 今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        // Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance(); // 昨天

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH,
                current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);

        if (current.after(today)) {
            return 1;
        } else if (current.before(today) && current.after(yesterday)) {
            return 2;
        } else {
            return 3;
        }
    }

    /**
     * 判断日期是今天还是昨天
     *
     * @param date
     * @return 0-格式错误 1-今天 2-昨天 3-日期
     */
    public static int judgeCurrentDate(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance(); // 今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        // Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance(); // 昨天

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH,
                current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);

        if (current.after(today)) {
            return 1;
        } else if (current.before(today) && current.after(yesterday)) {
            return 2;
        } else {
            return 3;
        }
    }

    /**
     * 获得日期的下一天
     *
     * @param date
     * @return
     */
    public static Date getNextDay(Date date) {// 接收输入的日期inputDate
        Calendar nextDate = Calendar.getInstance();
        nextDate.setTime(date);
        nextDate.add(Calendar.DAY_OF_MONTH, 1);
        return nextDate.getTime();
    }


    /**
     * 获得日期的前一天
     *
     * @param date
     * @return
     */
    public static Date getForeDay(Date date) {// 接收输入的日期inputDate
        Calendar nextDate = Calendar.getInstance();
        nextDate.setTime(date);
        nextDate.add(Calendar.DAY_OF_MONTH, -1);
        return nextDate.getTime();
    }

    /**
     * 根据日期获得星期
     *
     * @param date
     * @return
     */
    public static String getWeek(Date date) {
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (index < 0) {
            index = 0;
        }
        return weeks[index];
    }

    /**
     * 获得指定日期与当前日期相差的天数
     *
     * @param day1
     * @return
     * @throws ParseException
     */
    public static int getBetweenDays(Date day1) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        day1 = sf.parse(sf.format(day1));

        Calendar c = Calendar.getInstance();
        Date day2 = sf.parse(sf.format(c.getTime()));

        c.setTime(day1);
        long d1 = c.getTimeInMillis();

        c.setTime(day2);
        long d2 = c.getTimeInMillis();

        long between = (d2 - d1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between));
    }

    /**
     * 判断日期是否在当天
     *
     * @param dateStr yyyy-MM-dd
     * @return
     * @throws ParseException
     */
    public static boolean isCurrentDay(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sdf.parse(dateStr);
            Date current = sdf.parse(sdf.format(new Date()));
            int flag = date.compareTo(current);
            if (flag == 0) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 获得日期所在的月份
     *
     * @param date
     * @return 月份
     */
    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 判断是否在当月
     *
     * @param date
     * @return
     */
    public static boolean isCurrentMonth(Date date) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        int m1 = c1.get(Calendar.MONTH);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        int m2 = c2.get(Calendar.MONTH);
        return m1 == m2;
    }

    /**
     * 判断是否在当月
     *
     * @param dateStr
     * @return 月份
     */
    public static boolean isCurrentMonth(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c1 = Calendar.getInstance();
            c1.setTime(sdf.parse(dateStr));
            int m1 = c1.get(Calendar.MONTH);
            Calendar c2 = Calendar.getInstance();
            c2.setTime(new Date());
            int m2 = c2.get(Calendar.MONTH);
            if (m1 == m2) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 将数字转化为大写
     *
     * @param num  数字
     * @param reverseType 转换类型
     * @return
     */
    public static String numToUpper(int num, int reverseType) {
        String u1[] = { "〇", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
        String u2[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        char[] str = String.valueOf(num).toCharArray();
        String rStr = "";
        if (reverseType == 1) {
            for (int i = 0; i < str.length; i++) {
                rStr = rStr + u1[Integer.parseInt(str[i] + "")];
            }
        } else if (reverseType == 2) {
            for (int i = 0; i < str.length; i++) {
                rStr = rStr + u2[Integer.parseInt(str[i] + "")];
            }
        }
        return rStr;
    }

    /**
     * 将数字转化为大写
     *
     * @param num
     * @return
     */
    public static String numToUpper(int num) {
        String u1[] = { "〇", "一", "二", "三", "四", "五", "六", "日"};
        char[] str = String.valueOf(num).toCharArray();
        String rStr = "";
        for (int i = 0; i < str.length; i++) {
            rStr = rStr + u1[Integer.parseInt(str[i] + "")];
        }
        return rStr;
    }


    /**
     * 得到经过处理的时间
     * @param timeString  时间字符串
     * @param date        日期对象
     * @return
     */
    private static String getRecentTime(String timeString,Date date) {
        if(timeString == null || "".equals(timeString))
            return "";
        StringBuilder cn_string = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_YEAR);         //月份中的日
        int week = calendar.get(Calendar.WEEK_OF_YEAR);          //年中周
        int hour = calendar.get(Calendar.HOUR_OF_DAY);           //时间

        int month = calendar.get(Calendar.MONTH)+1;         //月份.显示用
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK)-1;   //周中的日.显示用
        if(day_of_week == 0) day_of_week = 7;
        int minute = calendar.get(Calendar.MINUTE);             //分钟.显示用

        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(new Date());
        int now_day = nowCalendar.get(Calendar.DAY_OF_YEAR);
        int now_week = nowCalendar.get(Calendar.WEEK_OF_YEAR);
        int now_hour = nowCalendar.get(Calendar.HOUR_OF_DAY);
        int now_minute = nowCalendar.get(Calendar.MINUTE);
        //时间字符串
        StringBuilder hoursString = new StringBuilder(""+hour);
        StringBuilder minutesString = new StringBuilder(""+minute);

        if(hour<10)
            hoursString.insert(0, "0");
        if(minute<10)
            minutesString.insert(0, "0");

        if(day == now_day && hour == now_hour && minute == now_minute){
            cn_string.append("刚刚");
        }
        else if(day == now_day && hour == now_hour){
            cn_string.append(Math.abs(now_minute - minute)).append("分钟前");
        }
        else if((now_day - day >=0) &&(now_day - day < 1) && (now_hour - hour < 24) && (now_hour - hour >0)){
            cn_string.append(now_hour-hour).append("小时前");
        }
        else if(now_day == day){
            cn_string.append("今天").append(hoursString).append(":").append(minutesString);
        }
        else if(now_day - day == 1)
            cn_string.append("昨天").append(hoursString).append(":").append(minutesString);
        else if(now_day - day == 2)
            cn_string.append("前天").append(hoursString).append(":").append(minutesString);
        else if(day - now_day == 1)
            cn_string.append("明天").append(hoursString).append(":").append(minutesString);
        else if(day - now_day == 2)
            cn_string.append("后天").append(hoursString).append(":").append(minutesString);
        else if((week == now_week && day>now_day ) || (week - now_week == 1 && day_of_week == 7))
            cn_string.append("周").append(numToUpper(day_of_week)).append(" ").append(hoursString).append(":").append(minutesString);
        else if(week - now_week ==1)
            cn_string.append("下周").append(numToUpper(day_of_week)).append(" ").append(hoursString).append(":").append(minutesString);
        else{  //直接显示
            StringBuilder monthString = new StringBuilder(""+month);
            int _day = calendar.get(Calendar.DAY_OF_MONTH);         //月份中的日
            StringBuilder dayString = new StringBuilder(""+_day);
            if(month <10)
                monthString.insert(0, "0");
            if(_day<10)
                dayString.insert(0, "0");
            cn_string.append(monthString).append("月").append(dayString).append("日").append(hoursString).append(":").append(minutesString);
        }
        return cn_string.toString();
    }

    //日期格式
    static DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 毫秒数转换成日期字符串
     * @param time 毫秒数
     * @return 日期字符串
     */
    public static String long2string(long time){
        return format.format(new Date(time));
    }

    /**
     * 日期字符串转换成毫秒数
     * @param time 日期字符串
     * @return 毫秒数
     */
    public static long string2long(String time){
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 计算天数
     * @param start 开始日期
     * @param end 结束日期
     * @return 天数
     */
    public static long countDays(String start,String end){
        try {
            Date dateStart = format.parse(start);
            Date dateEnd = format.parse(end);
            long days = (dateStart.getTime()-dateEnd.getTime())/(1000L*60*60*24);
            return days;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取一天的毫秒数
     * @return 毫秒数
     */
    public static long getOneDayForMilliSeconds(){
        return 1000L*60*60*24;
    }

    /**
     * 根据时间分别获取年月日
     * @param time 时间
     * @return 年月日的数组
     */
    public static int[] getSplitDate(String time){
        String[] from = time.split("-");
        int[] to = new int[3];
        if(from.length == 3){
            for(int i = 0; i < 3;i++){
                to[i] = Integer.parseInt(from[i]);
            }
        }
        return to;
    }

    /**
     * 根据时间获取中文年月日
     * @param time 时间
     * @return 年月日
     */
    public static String getNianYueRi(String time){
        String[] date = time.split("-");
        if(date.length == 3){
            return date[0] + "年" + date[1] + "月" + date[2]+"日";
        }else {
            return time;
        }
    }

    /**
     * 根据年月获得当月天数
     * @param time 年月
     * @return 年月日数组
     */
    public static String[] getDays(String time){
        String[] date = time.split("-");
        int n = 0;
        if(date.length == 2) {
            switch (Integer.parseInt(date[1])){
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    n = 31;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    n = 30;
                    break;
                case 2:
                    int year = Integer.parseInt(date[0]);
                    if(year%400==0||(year%100!=0&&year%4==0)){
                        n = 29;
                    }else{
                        n = 28;
                    }
                    break;
            }
            String[] times = new String[n];
            for(int i = 1;i <= n;i++ ){
                times[i-1] = time+"-"+i;
            }
            return times;
        }
        return null;
    }

    /**
     * 根据年月获得当月天数
     * @param time 年月
     * @return 天数
     */
    public static int countDays(String time){
        String[] date = time.split("-");
        int n = 0;
        if(date.length == 2) {
            switch (Integer.parseInt(date[1])){
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    n = 31;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    n = 30;
                    break;
                case 2:
                    int year = Integer.parseInt(date[0]);
                    if(year%400==0||(year%100!=0&&year%4==0)){
                        n = 29;
                    }else{
                        n = 28;
                    }
                    break;
            }
        }
        return n;
    }

    /**
     * 根据开始、结束时间获得天数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 年月日数组
     */
    public static String[] getDays(String startTime,String endTime){
        long start = string2long(startTime);
        long end = string2long(endTime);
        long distance = getOneDayForMilliSeconds();
        List<String> list = new ArrayList<>();
        for(long i = start;i <= end; i += distance){
            String time = long2string(i);
            String[] date = time.split("-");
            if(date[1].startsWith("0")){
                date[1] = date[1].substring(1);
            }
            if(date[2].startsWith("0")){
                date[2] = date[2].substring(1);
            }
            time = date[0]+"-"+date[1]+"-"+date[2];
            list.add(time);
        }
        String[] times = new String[list.size()];
        list.toArray(times);
        return times;
    }

}
