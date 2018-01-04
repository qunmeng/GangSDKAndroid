package com.qm.gangsdk.ui.utils;/*
 * Copyright (c) 2014, 西安蘑菇科技有限公司 All rights reserved.
 * File Name：EditTextShakeHelper.java
 * Version：V1.0
 * Author：zshu 
 * Date： 创建时间：2015/11/27 14:57
 */


import com.qm.gangsdk.core.outer.common.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * TimeUtils
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 */
public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DATE_FORMAT_DATE_MY = new SimpleDateFormat("yyyy/MM/dd");
    public static final SimpleDateFormat DATE_FORMAT_DATE_DDMM = new SimpleDateFormat("dd/MM");

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(Long timeInMillis, SimpleDateFormat dateFormat) {
        if (timeInMillis == null) {
            return "";
        }
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * 获取年龄值
     *
     * @param birthday
     * @return
     */
    public static int getAge(String birthday) {
        if (StringUtils.isEmpty(birthday)) return 0;
        String yearStr = birthday.substring(0, 4);
        int integer = Integer.parseInt(yearStr);
        int newYear = new GregorianCalendar().get(Calendar.YEAR);
        return newYear - integer;
    }


    /**
     * 转换日期格式到用户体验好的时间格式
     * @param time 2015-04-11 12:45:06
     * @return
     */
    /*public static String dateString2GoodExperienceFormat(String time) {
        if (isNullString(time)) {
            return "";
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
            try {
                String timeString;
                Date parse = simpleDateFormat.parse(time);
                long distanceTime = new Date().getTime() - parse.getTime();
                if (distanceTime < 0L) {
                    timeString = "0 mins ago";
                } else {
                    long n2 = distanceTime / 60000L;
                    new SimpleDateFormat("HH:mm");
                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM-dd");
                    if (n2 < 60L) {
                        timeString = String.valueOf(n2) + " mins ago";
                    } else if (n2 < 720L) {
                        timeString = String.valueOf(n2 / 60L) + " hours ago";
                    } else {
                        timeString = simpleDateFormat2.format(parse);
                    }
                }
                return timeString;
            } catch (Exception ex) {
                ex.printStackTrace();
                return "";
            }
        }
    }
*/

    /**
     * 转换日期格式到用户体验好的时间格式
     *
     * @param timeLong 2015-04-11 12:45:06
     * @return
     */
    public static String dateString2GoodExperienceFormat(Long timeLong) {
        return dateString2GoodExperienceFormat(getTime(timeLong));
    }

    /**
     * 转换日期格式到用户体验好的时间格式
     *
     * @param time 2015-04-11 12:45:06
     * @return
     */
    public static String dateString2GoodExperienceFormat(String time) {
        if (isNullString(time)) {
            return "";
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
            Calendar calNow = Calendar.getInstance();
            Calendar calCreated = Calendar.getInstance();
            try {
                String timeString;
                Date parse = simpleDateFormat.parse(time);
                calNow.setTime(parse);
                calCreated.setTimeInMillis(System.currentTimeMillis());
                long distanceTime = new Date().getTime() - parse.getTime();
                if (distanceTime < 0L) {
                    timeString = "刚刚";
                } else {
                    long n2 = distanceTime / 60000L;
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM月dd日");
                    int year = calCreated.get(Calendar.YEAR) - calNow.get(Calendar.YEAR);
                    int day = calCreated.get(Calendar.DAY_OF_YEAR) - calNow.get(Calendar.DAY_OF_YEAR);
                    if (n2 < 60 * 48 && year < 1) {
                        if (day == 1) {
                            timeString = "昨天";
                        } else {
                            timeString = simpleDateFormat1.format(parse);
                        }
                    } else if (day > 15) {
                        timeString = "15天前";
                    } else {
                        timeString = simpleDateFormat2.format(parse);
                    }
                }
                return timeString;
            } catch (Exception ex) {
                ex.printStackTrace();
                return "";
            }
        }
    }

    /**
     * 转换日期格式
     * @param timeLong
     * @return
     */
    public static String dateStringToOnlineTimeFormat(Long timeLong) {
        return dateStringToOnlineTimeFormat(getTime(timeLong));
    }


    /**
     * 转换日期格式
     *
     * @param time 2015-04-11 12:45:06
     * @return
     */
    private static String dateStringToOnlineTimeFormat(String time) {
        if (isNullString(time)) {
            return "";
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
            Calendar calNow = Calendar.getInstance();
            Calendar calCreated = Calendar.getInstance();
            try {
                String timeString = null;
                Date parse = simpleDateFormat.parse(time);
                calNow.setTime(parse);
                calCreated.setTimeInMillis(System.currentTimeMillis());
                long distanceTime = new Date().getTime() - parse.getTime();
                long n2 = distanceTime / 60000L;
                int year = calCreated.get(Calendar.YEAR) - calNow.get(Calendar.YEAR);
                int day = calCreated.get(Calendar.DAY_OF_YEAR) - calNow.get(Calendar.DAY_OF_YEAR);
                if (year < 1) {
                    if(n2 < 60 * 48 && day < 1 ){
                        if(n2 < 1){
                            timeString = "刚刚";
                        }else if(n2 < 60){
                            timeString = ( n2 % 60) + "分钟前";
                        }else if(n2 < 60 * 24) {
                            timeString = (n2 / 60) + "小时前";
                        }else {
                            timeString = "今天";
                        }
                    }else if (day == 1) {
                        timeString = "昨天";
                    }else if(day == 2){
                        timeString = "前天";
                    }else if((calCreated.get(Calendar.MONTH) - calNow.get(Calendar.MONTH)) < 1){
                        if(calCreated.get(Calendar.WEEK_OF_MONTH) == calNow.get(Calendar.WEEK_OF_MONTH)) {
                            timeString = "本周";
                        }else {
                            timeString = "本月";
                        }
                    }else if((calCreated.get(Calendar.MONTH) - calNow.get(Calendar.MONTH)) < 3){
                        timeString = "三个月内";
                    }else {
                        timeString = "超过三个月";
                    }
                } else if(1 == year){
                    int oldmonth = 11 - calNow.get(Calendar.MONTH);
                    if(oldmonth >= 2){
                        timeString = "超过三个月";
                    }else {
                        if (0 == oldmonth) {
                            if(calCreated.get(Calendar.MONTH) > 1) {
                                timeString = "超过三个月";
                            }else {
                                timeString = "三个月内";
                            }
                        } else {
                            if(calCreated.get(Calendar.MONTH) > 0) {
                                timeString = "超过三个月";
                            }else {
                                timeString = "三个月内";
                            }
                        }
                    }
                }else {
                    timeString = "超过三个月";
                }
                return timeString;
            } catch (Exception ex) {
                ex.printStackTrace();
                return "";
            }
        }
    }



    /**
     * 获取时间差
     *
     * @param createdTime 开始时间
     * @return 返回表示时间段的字符串，例如：2小时20分钟 前
     */
    public static String cntTimeDifference(String createdTime, String suffix) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calNow = Calendar.getInstance();
        Calendar calCreated = Calendar.getInstance();
        try {
            calCreated.setTime(sdf.parse(createdTime));
            int year = calNow.get(Calendar.YEAR)
                    - calCreated.get(Calendar.YEAR);
            int month = calNow.get(Calendar.MONTH)
                    - calCreated.get(Calendar.MONTH);
            int day = calNow.get(Calendar.DAY_OF_MONTH)
                    - calCreated.get(Calendar.DAY_OF_MONTH);
            int hour = calNow.get(Calendar.HOUR_OF_DAY)
                    - calCreated.get(Calendar.HOUR_OF_DAY);
            int minute = calNow.get(Calendar.MINUTE)
                    - calCreated.get(Calendar.MINUTE);
            int total = minute + hour * 60 + day * 24 * 60 + month * 30 * 24
                    * 60 + year * 365 * 24 * 60;
            if (total > 365 * 24 * 60) {
                return total / (365 * 24 * 60) + "年" + suffix;
            }
            if (total > 30 * 24 * 60) {
                return total / (30 * 24 * 60) + "月" + suffix;
            }
            if (total > 24 * 60) {
                return total / (24 * 60) + "天" + suffix;
            }
            if (total > 60) {
                return total / 60 + "小时" + suffix;
            }
            if (total > 0) {
                return total + "分钟" + suffix;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "未知时间";
        }
        return "刚刚";
    }

    public static String cntTimeDifference(Long createdTime) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long diff = createdTime - getCurrentTimeInLong();
        long day = diff / nd;
        long hour = diff % nd / nh;
        long min = diff % nd % nh / nm;
        if (day > 0) {
            return day + "天" + hour + "时" + min + "分";
        } else {
            if (min > 0) {
                return hour + "时" + min + "分";
            } else {
                return min + "分";
            }
        }
    }

    public static boolean isNullString(String s) {
        return s == null || s.equals("") || s.equals("null");
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 10*1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
