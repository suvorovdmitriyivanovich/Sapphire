package com.sapphire.utils;

import android.util.Base64;
import com.sapphire.logic.Environment;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Encrypt {
    private static byte[] getKey() {
        byte[] key = new byte[0];

        try {
            byte[] keyStart = Environment.KEY.getBytes();
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(keyStart);
            kgen.init(128, sr); //192 and 256 bits may not be available
            SecretKey skey = kgen.generateKey();
            key = skey.getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return key;
    }

    public static byte[] encryptData(String in) {
        byte[] encrypted = new byte[0];

        try {
            SecretKeySpec skeySpec = new SecretKeySpec(getKey(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            encrypted = cipher.doFinal(in.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;
    }

    public static String decryptData(byte[] in) {
        String rezult = "";

        try {
            SecretKeySpec skeySpec = new SecretKeySpec(getKey(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] decrypted = cipher.doFinal(in);
            rezult = new String(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rezult;
    }

    public static byte[] encryptDataUTF8(String in) {
        byte[] encrypted = new byte[0];
        try {
            byte[] keyData = Environment.KEY.getBytes();
            SecretKey ks = new SecretKeySpec(keyData, "AES");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, ks);
            encrypted = c.doFinal(in.getBytes("UTF-8"));
            //return Base64.encodeToString(encryptedText, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;
    }

    public static String decryptDataUTF8(byte[] in) {
        String rezult = "";

        try {
            byte[] keyData = Environment.KEY.getBytes();
            SecretKey ks = new SecretKeySpec(keyData, "AES");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, ks);
            byte[] clearText = c.doFinal(Base64.decode(in, Base64.DEFAULT));
            rezult = new String(clearText, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rezult;
    }

    public static String encrypt(String clearText) {
        byte[] encryptedText = null;
        try {
            byte[] keyData = Environment.KEY.getBytes();
            SecretKey ks = new SecretKeySpec(keyData, "AES");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, ks);
            encryptedText = c.doFinal(clearText.getBytes("UTF-8"));
            return Base64.encodeToString(encryptedText, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }
    }

    public static String decrypt (String encryptedText) {
        byte[] clearText = null;
        try {
            byte[] keyData = Environment.KEY.getBytes();
            SecretKey ks = new SecretKeySpec(keyData, "AES");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, ks);
            clearText = c.doFinal(Base64.decode(encryptedText, Base64.DEFAULT));
            return new String(clearText, "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }
}
