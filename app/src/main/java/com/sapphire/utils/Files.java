package com.litnet.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Files {
    public static void writeToFile(byte[] data, File file) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            stream.write(data);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(String data, File file) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            stream.write(data.getBytes());
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] readFromFile(File file) {
        int length = (int) file.length();

        byte[] bytes = new byte[length];

        try {
            FileInputStream in = new FileInputStream(file);
            in.read(bytes);
            in.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        return bytes;
    }

    public static byte[] readFromFileByte(File file) {
        int length = (int) file.length();

        byte[] bytes = new byte[length];

        try {
            FileInputStream in = new FileInputStream(file);
            in.read(bytes); //2
            in.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        return bytes;
    }

    public static String readFromFileString(File file) {
        int length = (int) file.length();

        byte[] bytes = new byte[length];

        try {
            FileInputStream in = new FileInputStream(file);
            in.read(bytes);
            in.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        return new String(bytes).toString();
    }
}
