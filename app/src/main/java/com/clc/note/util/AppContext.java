package com.clc.note.util;

import android.app.Application;

//创建之后一定要修改清单文件中application标签的name值
public class AppContext extends Application {
    private static AppContext sAppContext;

    public int typeIdAfterSave;
    public boolean isBack;

    public static AppContext getInstance(){
        return sAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this;
    }
}
