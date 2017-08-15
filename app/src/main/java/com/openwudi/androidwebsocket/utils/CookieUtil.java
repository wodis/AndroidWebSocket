package com.openwudi.androidwebsocket.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Cookie;

/**
 * Created by diwu on 17/5/19.
 */

public final class CookieUtil {
    private static HashMap<String, List<Cookie>> store = new HashMap<>();

    public static void put(String host, Cookie cookie) {
        List<Cookie> list = list(host);

        if (list.size() == 0){
            list.add(cookie);
            store.put(host, list);
            return;
        }

        //滤重
        List<Cookie> remove = new ArrayList<>();
        for (Cookie c : list) {
            if (c.name().equalsIgnoreCase(cookie.name())) {
                remove.add(c);
            }
        }

        if (remove.size() > 0) {
            list.removeAll(remove);
        }

        //添加
        list.add(cookie);
        store.put(host, list);
    }

    public static void putAll(String host, List<Cookie> cookieList) {
        List<Cookie> list = list(host);
        if (list.size() == 0){
            list.addAll(cookieList);
            store.put(host, list);
            return;
        }

        //去除Name
        Set<String> names = new HashSet<>(cookieList.size());
        for (Cookie c : cookieList){
            String name = c.name();
            names.add(name);
        }

        //滤重
        List<Cookie> remove = new ArrayList<>();
        for (Cookie c : list) {
            if (names.contains(c.name())) {
                remove.add(c);
            }
        }

        if (remove.size() > 0) {
            list.removeAll(remove);
        }

        //添加
        list.addAll(cookieList);
        store.put(host, list);
    }

    public static List<Cookie> list(String host) {
        List<Cookie> list = store.get(host);
        if (list == null) list = new ArrayList<>();
        return list;
    }

    public static String string(String host){
        StringBuilder sb = new StringBuilder();
        for (Cookie c : list(host)){
            sb.append(c.name()).append("=").append(c.value()).append("; ");
        }
        return sb.toString();
    }

    public static void clear(){
        store = new HashMap<>();
    }
}
