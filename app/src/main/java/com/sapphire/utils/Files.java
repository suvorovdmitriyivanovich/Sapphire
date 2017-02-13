package com.sapphire.utils;

import com.sapphire.Sapphire;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

    public boolean readZip(String fallfileName) {
        boolean isOk = true;

        String FilesDir = Sapphire.getInstance().getFilesDir().getAbsolutePath() + "/temp";

        try {
            FileInputStream inputStream = new FileInputStream(fallfileName);
            ZipInputStream zipStream = new ZipInputStream(inputStream);
            ZipEntry zEntry = null;
            while ((zEntry = zipStream.getNextEntry()) != null) {
                FileOutputStream fout = new FileOutputStream(
                        FilesDir + "/" + zEntry.getName());
                BufferedOutputStream bufout = new BufferedOutputStream(fout);
                byte[] buffer = new byte[1024];
                int read = 0;
                while ((read = zipStream.read(buffer)) != -1) {
                    bufout.write(buffer, 0, read);
                }

                zipStream.closeEntry();
                bufout.close();
                fout.close();

                //isOk = parseXml(FilesDir + "/" + zEntry.getName());
            }
            zipStream.close();

            //после удаляем файл
            File f = new File(fallfileName);
            f.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isOk;
    }
}
