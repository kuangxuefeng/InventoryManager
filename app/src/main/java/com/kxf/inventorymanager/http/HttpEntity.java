package com.kxf.inventorymanager.http;

import java.util.Arrays;

/**
 * Created by kuangxf on 2017/9/12.
 */

public class HttpEntity<T> {
    private String requestCode;
    private String responseCode;
    private String responseMsg;
    private T[] ts;

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public T[] getTs() {
        return ts;
    }

    public void setTs(T[] ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "HttpEntity{" +
                "requestCode='" + requestCode + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", responseMsg='" + responseMsg + '\'' +
                ", ts=" + Arrays.toString(ts) +
                '}';
    }
}
