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
}