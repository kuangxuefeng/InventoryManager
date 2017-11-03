package com.kxf.inventorymanager.http;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.kxf.inventorymanager.utils.EncUtil;
import com.kxf.inventorymanager.utils.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kuangxf on 2017/9/12.
 */

public class HttpUtils {
    public static final String BASE_URL = "http://192.168.1.100:8080/InventoryManagerServer/servlet/";//192.168.1.100  116.196.95.211
    public static final String USER_URL = "UserServlet";
    public static final String COMMODITY_URL = "CommodityServlet";
    public static String sendMsg(String urlItem, HttpEntity he) {
        try {
            return sendMsgEx(urlItem, he);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String sendMsgEx(String urlItem, HttpEntity he) throws IOException {
        LogUtil.i("urlItem=" + urlItem + "; he=" + he);
        StringBuffer sb = new StringBuffer();
        URL url = new URL(BASE_URL + urlItem);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
        // http正文内，因此需要设为true, 默认情况下是false;
        con.setDoOutput(true);

        // 设置是否从httpUrlConnection读入，默认情况下是true;
        con.setDoInput(true);

        // Post 请求不能使用缓存
        con.setUseCaches(false);

        //设置超时时间
        con.setConnectTimeout(10*1000);//设置连接主机超时（单位：毫秒）
        con.setReadTimeout(30*1000);//设置从主机读取数据超时（单位：毫秒）

        // 设定传送的内容类型是可序列化的java对象
        // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
//		con.setRequestProperty("Content-type", "application/x-java-serialized-object");
        con.setRequestProperty("Content-type", "text/plain; charset=utf-8");//application/json

        // 设定请求的方法为"POST"，默认是GET
        con.setRequestMethod("POST");

        // 连接，从上述第2条中url.openConnection()至此的配置必须要在connect之前完成，
        Gson gson = new Gson();
        try {
            con.connect();
        } catch (Exception e) {
            e.printStackTrace();
            he.setResponseCode(-200 + "");
            he.setResponseMsg("通讯失败 code=" + -200);
            return gson.toJson(he);
        }

        String jstr = gson.toJson(he);
        LogUtil.d("jstr=" + jstr);
        jstr = EncUtil.encryptAsString(null, jstr);
        LogUtil.d("加密后 jstr=" + jstr);

        OutputStream out = con.getOutputStream();
        out.write(jstr.getBytes("utf-8"));
        out.flush();
        out.close();

        int code = con.getResponseCode();
        LogUtil.d("code=" + code);
        if (200 == code) {
            InputStream in = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String responseStr;
            while ((responseStr = reader.readLine()) != null) {
                sb.append(responseStr);
            }
            in.close();
        }else {
            he.setResponseCode(code + "");
            he.setResponseMsg("通讯失败 code=" + code);
            sb.append(EncUtil.encryptAsString(null, gson.toJson(he)));
        }
        LogUtil.d("sb=" + sb);
        con.disconnect();
        String re = EncUtil.desEncryptAsString(null, sb.toString());
        LogUtil.d("解密 re=" + re);
        return re;
    }

    public static <T> HttpEntity<T> ParseJson(HttpEntity<T> reqHe, String jsonStr, Type typeOfT){
        if (TextUtils.isEmpty(jsonStr)){
            reqHe.setResponseCode("-1");
            reqHe.setResponseMsg("后台响应失败");
        }else {
            Gson gson = new Gson();
            HttpEntity<T> heRe = gson.fromJson(jsonStr, typeOfT);
            if (null == heRe){
                reqHe.setResponseCode("-2");
                reqHe.setResponseMsg("后台响应数据异常");
            } else {
                reqHe = heRe;
            }
        }
        return reqHe;
    }
}
