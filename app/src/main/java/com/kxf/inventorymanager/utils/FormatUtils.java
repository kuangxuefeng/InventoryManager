package com.kxf.inventorymanager.utils;

import android.text.TextUtils;

/**
 * Created by kuangxf on 2017/9/14.
 */

public class FormatUtils {
    public enum ALIGN{
        LEFT, RIGHT, CENTER
    }
    public static String FormatStringLen(String src, int len, ALIGN align, String filling){
        if (TextUtils.isEmpty(filling)){
            return src;
        }
        if (null == src){
            src = "";
        }
        switch (align){
            case LEFT:
                while (src.length() < len){
                    src = filling + src;
                }
                break;
            case RIGHT:
                while (src.length() < len){
                    src = src + filling;
                }
                break;
            case CENTER:
                while (src.length() < len){
                    src = filling + src + filling;
                }
                break;
        }
        return src;
    }
}
