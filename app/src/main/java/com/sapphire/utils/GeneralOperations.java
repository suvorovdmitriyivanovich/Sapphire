package com.sapphire.utils;

import com.sapphire.Sapphire;

public class GeneralOperations {
    public static int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = Sapphire.getInstance().getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    public static boolean isEmailValid(String email, boolean fullValid) {
        int length = email.length() - 1;

        if (fullValid && length == -1) {
            return true;
        }

        if (length < 4)
            return false;

        if (email.indexOf("'") != -1 || email.indexOf("\"") != -1 || email.indexOf("\\") != -1
                || email.indexOf("?") != -1 || email.indexOf("&") != -1) {
            return false;
        }

        String str1 = email.substring(0, 1);
        if ("@.".indexOf(str1) != -1)
            return false;

        if (email.indexOf(".") == -1)
            return false;

        int ind = email.indexOf("@") + 1;
        int ind2 = email.indexOf("@") + 2;

        if (ind == -1 || ind == length + 1)
            return false;

        String str2 = email.substring(ind, ind2);

        if ("@.".indexOf(str2) != -1)
            return false;

        String str3 = email.substring(length);

        if ("@.".indexOf(str3) != -1)
            return false;

        //точка должна быть после @
        String str4 = email.substring(ind);

        return str4.contains(".") && !str4.contains("@");
    }

    public static boolean isSinNumberValid(String sinNumber, boolean fullValid) {
        int length = sinNumber.length() - 1;

        if (fullValid && length == -1) {
            return true;
        }

        if (length != 10) {
            return false;
        }

        if (sinNumber.indexOf("-") != 3) {
            return false;
        }

        String str1 = sinNumber.substring(4);

        if (str1.indexOf("-") != 3) {
            return false;
        }

        for (int i = 0; i < 3; i++) {
            if ("0123456789".indexOf(sinNumber.substring(i,i+1)) == -1) {
                return false;
            }
        }

        for (int i = 4; i < 7; i++) {
            if ("0123456789".indexOf(sinNumber.substring(i,i+1)) == -1) {
                return false;
            }
        }

        for (int i = 8; i < 11; i++) {
            if ("0123456789".indexOf(sinNumber.substring(i,i+1)) == -1) {
                return false;
            }
        }

        return true;
    }

    public static String formatingPhone(String noFormating) {
        String phone = "";

        noFormating = noFormating.replaceAll("\\(", "");
        noFormating = noFormating.replaceAll("\\)", "");
        noFormating = noFormating.replaceAll(" ", "");
        noFormating = noFormating.replaceAll("-", "");

        int length1 = Math.min(3,noFormating.length());
        if (length1 <=0) {
            return phone;
        }
        phone = phone + "(" + noFormating.substring(0, length1) + ")";

        int length2 = Math.min(6,noFormating.length());
        if (length2 <=0 || noFormating.length() <= 3) {
            return phone;
        }
        phone = phone + " " + noFormating.substring(3, length2);

        if (noFormating.length() <= 6) {
            return phone;
        }
        phone = phone + "-" + noFormating.substring(6, noFormating.length());

        return phone;
    }
}