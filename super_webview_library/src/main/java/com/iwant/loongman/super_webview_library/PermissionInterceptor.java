package com.iwant.loongman.super_webview_library;

/**
 * Created by cenxiaozhong on 2017/8/17.
 */

public interface PermissionInterceptor {

    boolean intercept(String url, String[] permissions, String action);

}
