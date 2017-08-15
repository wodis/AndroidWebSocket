package com.openwudi.androidwebsocket;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.openwudi.androidwebsocket.https.TrustAll;
import com.openwudi.androidwebsocket.model.RequestOptions;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.security.GeneralSecurityException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebsocketClient {
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private static OkHttpClient sClient;
    private static WebSocket sWebSocket;

    public static synchronized void startRequest(String addr) {
        if (sClient == null) {
            sClient = new OkHttpClient();
        }
        if (sWebSocket == null) {
            Request request = new Request.Builder().url(addr).build();
            EchoWebSocketListener listener = new EchoWebSocketListener();
            sWebSocket = sClient.newWebSocket(request, listener);
        }
    }

    private static void sendMessage(WebSocket webSocket) {
        webSocket.send("Knock, knock!");
        webSocket.send("Hello!");
        webSocket.send(ByteString.decodeHex("deadbeef"));
    }

    private static void sendMessage(WebSocket webSocket, String body) {
        webSocket.send(body);
    }

    public static void sendMessage() {
        WebSocket webSocket;
        synchronized (WebsocketClient.class) {
            webSocket = sWebSocket;
        }
        if (webSocket != null) {
            sendMessage(webSocket);
        }
    }

    public static void sendMessage(String body) {
        WebSocket webSocket;
        synchronized (WebsocketClient.class) {
            webSocket = sWebSocket;
        }
        if (webSocket != null) {
            sendMessage(webSocket, body);
        }
    }

    public static synchronized void closeWebSocket() {
        if (sWebSocket != null) {
            sWebSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye!");
            sWebSocket = null;
        }
    }

    public static synchronized void destroy() {
        if (sClient != null) {
            sClient.dispatcher().executorService().shutdown();
            sClient = null;
        }
    }

    private static void resetWebSocket() {
        synchronized (WebsocketClient.class) {
            sWebSocket = null;
        }
    }

    public static class EchoWebSocketListener extends WebSocketListener {
        private static final String TAG = "EchoWebSocketListener";

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            Log.i(TAG, "onOpen");
//            webSocket.send("Android Client Connected.");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Log.i(TAG, "Receiving: " + text);
            String resp = rrdSpiderFetch(text);
            Log.i(TAG, resp);
            webSocket.send(resp);
            EventBus.getDefault().post(new MessageEvent(text, resp));
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            Log.i(TAG, "Receiving: " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            Log.i(TAG, "Closing: " + code + " " + reason);
            resetWebSocket();
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            Log.i(TAG, "Closed: " + code + " " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            t.printStackTrace();
            resetWebSocket();
        }

        public String rrdSpiderFetch(String optionsStr) {
            JSONObject result = new JSONObject();
            RequestOptions requestOptions = null;
            try {
                requestOptions = JSON.parseObject(optionsStr, RequestOptions.class);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }

            boolean invalidOption = requestOptions == null || TextUtils.isEmpty(requestOptions.getUrl());
            if (invalidOption) {
                result.put("ok", false);
                result.put("statusText", "ERR_INVALID_REQUEST");
                return result.toJSONString();
            }

            String certs = TrustAll.decryptCerts(requestOptions.getEncryptedCerts());

            try {
                OkHttpClient client = SpiderEngine.getInstance(requestOptions.isAutoCookie(), certs);
                Request request = SpiderEngine.build(requestOptions);
                Response response = client.newCall(request).execute();
                String data = SpiderEngine.extractData(response, requestOptions.getImg());
                JSONArray headers = SpiderEngine.extractHeader(response.headers());
                result.put("data", data);
                result.put("header", headers);
                result.put("ext", requestOptions.getExt());
                result.put("ok", true);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
                result.put("data", "{'err':'HTTPS CERTS WRONG.'}");
                result.put("ok", false);
                result.put("ext", requestOptions.getExt());
            } catch (Exception e) {
                e.printStackTrace();
                result.put("data", "{'err':'IOException'}");
                result.put("ok", false);
                result.put("ext", requestOptions.getExt());
            }

            return result.toJSONString();
        }
    }
}