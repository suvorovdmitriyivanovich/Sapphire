package com.sapphire.utils;

import com.sapphire.logic.UserInfo;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateOperations {
    public static Long getDate(String date) {
        Long dateLong = 0l;
        int index = date.lastIndexOf(".");
        String SSS = "";
        if (index != -1) {
            SSS = ".";
            int lenght = date.substring(index+1).length()-1;
            if (lenght <= 0) {
                return dateLong;
            }
            for (int i = 0; i < lenght; i++) {
                SSS = SSS + "S";
            }
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"+SSS+"'Z'");
        try {
            Date newdate = format.parse(date);
            dateLong = newdate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dateLong > 0l) {
            int indexT = date.lastIndexOf("T");
            if (indexT != -1) {
                String time = "";
                if (index != -1) {
                    time = date.substring(indexT+1, index);
                } else {
                    time = date.substring(indexT+1, date.length()-1);
                }
                if (!time.equals("00:00:00")) {
                    String gMTOffset = UserInfo.getUserInfo().getTimeZone().getGMTOffset();
                    if (!gMTOffset.equals("")) {
                        Double gMTOffsetDouble = 0d;
                        try {
                            gMTOffsetDouble = Double.valueOf(gMTOffset) * (60d*60d*1000d);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (gMTOffsetDouble != 0d) {
                            dateLong = dateLong + gMTOffsetDouble.longValue();
                        }
                    }
                }
            }
        }
        return dateLong;
    }

    public static String getDateServer(Long date) {
        String dateString = "";
        if (date != 0l) {
            String gMTOffset = UserInfo.getUserInfo().getTimeZone().getGMTOffset();
            if (!gMTOffset.equals("")) {
                Double gMTOffsetDouble = 0d;
                try {
                    gMTOffsetDouble = Double.valueOf(gMTOffset) * (60d*60d*1000d);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (gMTOffsetDouble != 0d) {
                    date = date - gMTOffsetDouble.longValue();
                }
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date dateT = new Date();
            dateT.setTime(date);
            dateString = format.format(dateT);
        }
        return dateString;
    }
}