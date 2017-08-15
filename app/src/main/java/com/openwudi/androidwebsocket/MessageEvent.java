package com.openwudi.androidwebsocket;

/**
 * Created by diwu on 2017/8/15.
 */

class MessageEvent {
    public String req;
    public String msg;

    public MessageEvent(String msg) {
        this.msg = msg;
    }

    public MessageEvent(String req, String msg) {
        this.req = req;
        this.msg = msg;
    }
}
