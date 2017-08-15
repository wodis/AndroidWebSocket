package com.openwudi.androidwebsocket.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by diwu on 17/5/24.
 */

public class RequestOptions {
    //请求方法
    private String method;
    //地址
    private String url;
    //HTTP正文
    private String body = "";
    //HTTP头
    private Map<String, String> headers = new HashMap<>();
    //HTTP请求Content-Type
    private String type;
    //图片参数
    private ImageOpt img;
    //是否由客户端管理Cookie
    @JSONField(name = "auto_cookie")
    private boolean autoCookie;

    //加密后的HTTPS证书
    @JSONField(name = "c")
    private String encryptedCerts;

    //是否需要CC
    private boolean cc;

    //缓存需要
    @JSONField(name = "resp_id")
    private String respId;

    @JSONField(name = "resp_key")
    private String respKey;

    @JSONField(name = "ext")
    private String ext;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ImageOpt getImg() {
        return img;
    }

    public void setImg(ImageOpt img) {
        this.img = img;
    }

    public boolean isAutoCookie() {
        return autoCookie;
    }

    public void setAutoCookie(boolean autoCookie) {
        this.autoCookie = autoCookie;
    }

    public String getEncryptedCerts() {
        return encryptedCerts;
    }

    public void setEncryptedCerts(String encryptedCerts) {
        this.encryptedCerts = encryptedCerts;
    }

    public boolean isCc() {
        return cc;
    }

    public void setCc(boolean cc) {
        this.cc = cc;
    }

    public String getRespId() {
        return respId;
    }

    public void setRespId(String respId) {
        this.respId = respId;
    }

    public String getRespKey() {
        return respKey;
    }

    public void setRespKey(String respKey) {
        this.respKey = respKey;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
