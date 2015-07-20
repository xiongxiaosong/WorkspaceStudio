package com.bestbrand.hh.app;

import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;

import com.easemob.chat.EMChat;
import com.xxs.sdk.app.AppContext;
import com.xxs.sdk.handler.CrashHandler;

import cn.smssdk.SMSSDK;

/**
 * 自定义Application
 *
 * @author xiongxs
 * @date 2015-03-31
 * @intruduce 完成主上下文的初始化工作
 */
public class HhApp extends Application {

    private static HhApp sInstance;

    /**
     * 单利模式获取唯一单利对象
     */
    public static HhApp getInstance() {
        if (sInstance == null) {
            sInstance = new HhApp();
        }
        return sInstance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        AppContext.initContext(this);
        SMSSDK.initSDK(this, "8d3cd90b6de1", "92eee25cc04e67c9b4ecca0360a9fafa");//调用初始化SMSSDK的方法
        initEMChat();// 调用初始化环信的SDK的方法
        // 下面两句为闪退异常日志捕获，开发时不需要，打包测试需将下面两句代码打开，以便收集错误日志
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(this);
    }

    /**
     * 初始化环信SDK的方法
     */
    private void initEMChat() {
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null
                || !processAppName.equalsIgnoreCase("com.bestbrand.hh")) {
            // "com.bestbrand.hh"为项目的包名，换到自己项目中要改成自己包名
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        EMChat.getInstance().init(this);// 初始化环信SDK
        EMChat.getInstance().setDebugMode(true);// 设置输出调试LOG日志，打包混淆时需要关闭此调试模式
    }

    @SuppressWarnings({"rawtypes", "unused"})
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this
                .getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
                    .next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm
                            .getApplicationInfo(info.processName,
                                    PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
