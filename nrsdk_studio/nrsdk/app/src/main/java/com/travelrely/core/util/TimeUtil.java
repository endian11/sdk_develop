
package com.travelrely.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.travelrely.core.nrs.Engine;
import com.travelrely.sdk.R;

public class TimeUtil {

    /**
     * 15/04/2012
     */
    public static final String dateFormat1 = "dd/MM/yyyy";

    /**
     * 2012-11-15 10:00:00
     */
    public static final String dateFormat2 = "yyyy-MM-dd HH:mm:ss";

    // public static final String dateFormat3 = "yyyy-MM-dd";//
    /**
     * MM.dd.yyyy
     */
    public static final String dateFormat3 = "MM.dd.yyyy";//

    public static final String dateFormat3_1 = "yyyy.MM.dd";//
    /**
     * yyyy-MM-dd
     */
    public static final String dateFormat4 = "yyyy-MM-dd";

    /**
     * 
     */
    public static final String dateFormat5 = "MM.dd";

    public static final String detaFormat6Split = ".";

    public static final String detaFormat6 = "MMM" + detaFormat6Split + "yyyy";

    public static final String dateFormat7 = "yyyy年MM月dd日";

    /**
     * MM月yyyy
     */
    public static final String dateFormat8 = "MM月yyyy";

    public static final String dateFormat9 = "HH:mm";

    public static final String dateFormat11 = "hh:mm";

    public static final String dateFormat10 = "a";
    
    public static final String dateFormat12 = "HH:mm:ss";
    
    /**
     * 2012-11-15 10:00
     */
    public static final String dateFormat13 = "yyyy-MM-dd HH:mm";

    public static String getDateString(long milliSeconds, String dateFormat) {
        DateFormat formatter = new SimpleDateFormat(dateFormat, Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    
    public static String getIntToDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat , Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Date date = new Date(milliSeconds * 1000);
        sdf.format(date);
        return sdf.format(date);
    }

    public static long getTimeByString(String time, String format) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(format, Locale.US);

        return formatter.parse(time).getTime();

    }

    public static String changeFormattrString(String srcTime, String srcFormat, String targetFormat) {
        String result = "";
        try {
            long time = TimeUtil.getTimeByString(srcTime, srcFormat);

            result = TimeUtil.getDateString(time, targetFormat);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 输出格式: 2006年4月16日 星期六
     * 
     * @param time
     * @return
     */
    public static String generateFullTime(long time) {
        Date d = new Date();
        d.setTime(time);
        String s = DateFormat.getDateInstance(DateFormat.FULL).format(d);
        return s;
    }

    private static final int ONE_MIN = 60;

    public static String generateUsedTime(Context context, int seconds) {

        String result;
        if (seconds < ONE_MIN) {
            result = seconds + context.getString(R.string.second);
        } else {
            int min = seconds / ONE_MIN;
            int second = seconds % ONE_MIN;
            result = min + context.getString(R.string.min) + second+context.getString(R.string.second);
        }
        return result;
    }

    public static long getTimeZoneTime(String number) {

        long localTime = System.currentTimeMillis();
        String TimeZoneId = getTimeZoneId(number);

        long targetOffset = TimeZone.getTimeZone(TimeZoneId).getOffset(localTime);
        long defaultOffset = TimeZone.getDefault().getOffset(localTime);
        long added = targetOffset - defaultOffset + localTime;

        return added;
    }

    public static String getTimeZoneId(String number) {
        if (number == null || number.equals("") || number.length() < 3) {
            return TimeZoneIDS.Asia_Shang_hai;
        }
        // 如果包含+
        if (number.charAt(0) == '+') {
            if (number.charAt(1) == '1') {
                // 是美国
                return TimeZoneIDS.America_New_York;
            }
        }
        if (number.charAt(0) == '0') {

            if (number.charAt(1) == '0') {
                // 说明他是国家码

                if (number.charAt(2) == '1') {
                    return TimeZoneIDS.America_New_York;

                }
            }

        }
        return TimeZoneIDS.Asia_Shang_hai;
    }

    public static String getDay(long time) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int dayOfweek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfweek) {
            case 1:
                return Engine.getInstance().getString(R.string.sunday);
            case 2:
                return Engine.getInstance().getString(R.string.monday);
            case 3:
                return Engine.getInstance().getString(R.string.tuesday);
            case 4:
                return Engine.getInstance().getString(R.string.wendesday);
            case 5:
                return Engine.getInstance().getString(R.string.thursday);
            case 6:
                return Engine.getInstance().getString(R.string.friday);
            case 7:
                return Engine.getInstance().getString(R.string.saturday);

        }

        return "";
    }
    
    

    public static String getTimeAMorPM(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        int i = calendar.get(Calendar.AM_PM);
        String str = "";
        if (i == Calendar.AM) {
            str = Engine.getInstance().getString(R.string.am);
        } else {
            str = Engine.getInstance().getString(R.string.pm);
        }
        return str + " " + getDateString(time, dateFormat11);

    }
    
    // date类型转换为String类型
    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType)
    {
        return new SimpleDateFormat(formatType).format(data);
    }
    
    public static Date stringToDate(String time,String time_type)
    {
        Date date = null;
        DateFormat format = new SimpleDateFormat(time_type);
        try
        {
            date = format.parse(time);
        } catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }
    
    public static String getTime(long time)
    {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
        return format.format(new Date(time));
    }

    public static String getHourAndMin(long time)
    {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }
    
    public static String getChatTime(long timesamp)
    {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(timesamp);
        int temp = Integer.parseInt(sdf.format(today))
                - Integer.parseInt(sdf.format(otherDay));

        switch (temp)
        {
            case 0:
                result = "今天  " + getHourAndMin(timesamp);
                break;
                
            case 1:
                result = "昨天  " + getHourAndMin(timesamp);
                break;
                
            case 2:
                result = "前天  " + getHourAndMin(timesamp);
                break;
    
            default:
                result = getTime(timesamp);
                break;
        }

        return result;
    }
    
    public static String getChatTime(String timesamp, String format)
    {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = stringToDate(timesamp, format);
        int temp = Integer.parseInt(sdf.format(today))
                - Integer.parseInt(sdf.format(otherDay));

        switch (temp)
        {
            case 0:
                result = "今天 " + getHourAndMin(otherDay.getTime());
                break;
                
            case 1:
                result = "昨天 " + getHourAndMin(otherDay.getTime());
                break;
                
            case 2:
                result = "前天 " + getHourAndMin(otherDay.getTime());
                break;
    
            default:
                result = getTime(otherDay.getTime());
                break;
        }

        return result;
    }
    
    public static String getDateLastMonth(int transactionBy)
    {
        Date d = new Date();
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gc.setTime(d);
        gc.add(2, -2);
        gc.set(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH),
                gc.get(Calendar.DATE));

        return sf.format(gc.getTime());
    }
    
    static String dateTime;
    static PopupWindow popup;
    
    private static void dismissPwd(){
        if (popup != null && popup.isShowing())
        {
            popup.dismiss();
        }
    }
}
