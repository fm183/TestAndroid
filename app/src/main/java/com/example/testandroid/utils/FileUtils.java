package com.example.testandroid.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

import com.example.testandroid.MainApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 文件工具类
 */

public class FileUtils {

    public static File save(String msg) {

        // 判断存储设备是否已就绪
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        FileWriter fw = null;
        PrintWriter pw = null;
        File logFile = null;
        try {
            // 创建目录
            final File dir = new File(Environment.getExternalStorageDirectory(),"logs");
            if (!dir.exists()) {
                dir.mkdirs();

            }
            // 打开文件
            logFile = new File(dir.getAbsolutePath(),"logs");
            if (!logFile.isFile() || !logFile.exists()) {
                logFile.createNewFile();
            }
            fw = new FileWriter(logFile, true);
            pw = new PrintWriter(fw);
            pw.append(msg);
            pw.println();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return logFile;
    }

    public static File getDownloadFile(String fileName){
        File dir = MainApplication.getInstance().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (dir == null || !dir.exists() || dir.isFile()) {
            return null;
        }

        return new File(dir,fileName);
    }

}
