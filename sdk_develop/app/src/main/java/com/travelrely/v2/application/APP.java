package com.travelrely.v2.application;

import android.app.Application;
import android.support.v4.content.LocalBroadcastManager;

import sdk.travelrely.lib.TRSdk;

/**
 ＊ LvXin_V2
 * Created by weihaichao on 16/3/1.
 ＊ ${PACKAGE_NAME}
 ＊ 10:01
 */
public class APP extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TRSdk.init(this).setDebug(true);

    }


}
