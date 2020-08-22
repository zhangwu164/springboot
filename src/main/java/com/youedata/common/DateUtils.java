package com.youedata.common;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * <p>Description:
     时间帮助类
 * </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company:成都优易数据有限公司</p>
 * @author wgj
 * @date 2018-6-20
 * @version V1.0
 */

public class DateUtils {

    /**
     * 无分隔符格式 yyyyMMddHHmmss
     */
    public static final String MY_STANDARD_DATE_FORMAT = "yyyyMMddHHmmss";
    /**
     * 毫秒格式 yyyy-MM-dd HH:mm:ss SSS
     */
    public static final String MILLISECOND_FORMAT = "yyyy-MM-dd HH:mm:ss SSS";

    public static final String SECOND_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String getNowTime(){
        SimpleDateFormat sdf = new SimpleDateFormat(SECOND_FORMAT);
        return sdf.format(new Date());
    }
    public static String getNowTime(String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    public static String getNowDateWithMinute(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        return sdf.format(new Date());
    }
    public static String getNowDateWithHour(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        return sdf.format(new Date());
    }
    /**
     *
     * @Title: getNowDateWithMillisecond
     * @Description: 获取当前时间yyyyMMddHHmmssSSS带三位毫秒
     * @return
     * String
     * @throws
     */
    public static String getNowDateWithMillisecond(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(new Date());
    }
    public static long getNowDateMillisecond(){
        return new Date().getTime();
    }
    /**
     *
     *日期减多少分钟
     *@param time 日期
     *@param frmT 格式
     *@param minute 分钟
     *@return String
     *@throws
     */
    public static String subTimeMinute(String time,String frmT,Object minute){
        SimpleDateFormat sdf = new SimpleDateFormat(frmT); // 这里的格式可以自己设置
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String times = sdf.format(date);
        date.setMinutes(date.getMinutes()-Integer.parseInt(minute.toString()));
        times=sdf.format(date);
        return times;
    }
    /**
     * 字符串类型的日期转换成Date类型的日期
     * @param string
     * @param withtime
     *  withtime=true,日期格式：yyyy-MM-dd HH:mm:ss；withtime=false,日期格式：yyyy-MM-dd
     * @return    Date
     */
    public static Date StringToDate(String string, boolean withtime) {
        Date date1 = null;
        if (string == null || string.equals("")) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (withtime) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }
        try {
            date1 = sdf.parse(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date1;
    }
    public static java.sql.Date convertToSqlDate(Date date)
    {
        if(date==null){
            return null;
        }
        try
        {
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            return sqlDate;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
    public static java.sql.Timestamp convertToTimestamp(Date date)
    {
        if(date==null){
            return null;
        }
        try
        {
            java.sql.Timestamp time = new java.sql.Timestamp(date.getTime());
            return time;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
    public static String getDateBeforeNow(int modifyHours) {
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -1*modifyHours);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        return sdf.format(cal.getTime());
    }
    public static Date addMinute(Date now,int minutes) {
        Calendar cal=Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }
    public static String DateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    public static Date StringToDate(String str, String format) {
        Date date = null;
        if (str == null || str.equals("")) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            date = sdf.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String DateToString(Date date,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    //计算时间差 秒数
    public static Long getTimeInterval(String begTime,String endTime,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date startDate;
        Long interval=null;
        try {
            startDate = sdf.parse(begTime);
            Date endDate=sdf.parse(endTime);
            interval=endDate.getTime()-startDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return interval/1000;
    }
    /**
     * 将字符串转换为毫秒数，如果出错返回指定值t_def
     * @param str   字符串
     * @param t_def 默认返回值
     * @return
     */
    public static  long parse(String str){
        return parse(str,MILLISECOND_FORMAT);
    }
    public static  long parse(String str,String parrten){
        try{
            SimpleDateFormat format = new SimpleDateFormat(parrten);
            long ts = format.parse(str).getTime();
            return ts;
        }catch (ParseException e){
            e.printStackTrace();
        }
        return -1;
    }
    /**
     *
     * 格式化毫秒数为 yyyy-MM-dd HH:mm:ss SSS
     *
     * @param t
     *              the timestamp
     * @return the natural format string value
     */
    public static  String format(long t){
        SimpleDateFormat format = new SimpleDateFormat(MILLISECOND_FORMAT);
        return format.format(new Date(t));
    }
    /**
     *
     * @Title: crateTimeDir
     * @Description: 根据时间生成路径,不包括两边的/
     * @param pattern
     * @param date
     * @return
     * String
     * @throws
     */
    public static String crateDateDir(DirPatten pattern,String date){

        StringBuffer dir = new StringBuffer();
        switch (pattern) {
            case YMDHM:
                //年月日时分
                dir.append(date.substring(0, 4)).append("/");//年
                dir.append(date.substring(5, 7)).append("/");//月
                dir.append(date.substring(8, 10)).append("/");//日
                dir.append(date.substring(11, 13)).append("/");//时
                dir.append(date.substring(14, 16));//分
                break;
            case YMDH:
                //年月日时
                dir.append(date.substring(0, 4)).append("/");//年
                dir.append(date.substring(5, 7)).append("/");//月
                dir.append(date.substring(8, 10)).append("/");//日
                dir.append(date.substring(11, 13));//时
                break;
            case YMD:
                //年月日时
                dir.append(date.substring(0, 4)).append("/");//年
                dir.append(date.substring(5, 7)).append("/");//月
                dir.append(date.substring(8, 10));//日
                break;
        }
        return dir.toString();
    }
    public static enum DirPatten{
        YMDHM,YMDH,YMD
    }
    /**
     * @Title: cutMillisecond
     * @Description: 截去三位毫秒值
     * @param millisecondTime
     * @return
     * String
     * @throws
     */
    public static String cutMillisecond(String millisecondTime){
        if(millisecondTime!=null&&millisecondTime.length()>19){
            return millisecondTime.substring(0, 19);
        }
        return millisecondTime;
    }
}
