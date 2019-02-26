package com.kxf.inventorymanager.utils;

import android.graphics.Bitmap;

import com.kxf.inventorymanager.AppConfig;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by kuangxf on 2019/2/26.
 */

public class FileUtils {
    public static void saveBitmap(Bitmap bitmap, String fileName) {
        FileOutputStream stream = null;
        try {
            File myFaceFile = new File(AppConfig.getSDPath("pic") + File.separator + fileName);
            stream = new FileOutputStream(myFaceFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
