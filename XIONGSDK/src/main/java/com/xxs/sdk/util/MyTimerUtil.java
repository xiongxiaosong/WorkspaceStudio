package com.xxs.sdk.util;


import com.xxs.sdk.myinterface.MyTimerInterCallback;

/**
 * 自定义计时器
 *
 * @author 熊小松
 * @time 2014-01-07
 */
public class MyTimerUtil extends CountDownTimer {
    /**
     * 计时器回调接口
     */
    private MyTimerInterCallback callback;

    /**
     * 是否已经启动倒计时
     */
    private boolean isstarted;

    /**
     * 构造函数
     *
     * @param millisInFuture    倒计时间总数（单位毫秒）
     * @param countDownInterval 每次计时间隔时间（单位毫秒）
     * @param callback          自定义计时器回调接口
     */
    public MyTimerUtil(long millisInFuture, long countDownInterval,
                       MyTimerInterCallback callback) {
        super(millisInFuture, countDownInterval);
        // TODO Auto-generated constructor stub
        this.callback = callback;
    }

    /**
     * 每次计时都会调用的方法
     */
    @Override
    public void onTick(long millisUntilFinished) {
        // TODO Auto-generated method stub
        callback.timerLeft(millisUntilFinished / 1000);
    }

    /**
     * 计时完成后将要自动调用的方法
     */
    @Override
    public void onFinish() {
        isstarted = false;
        cancel();
        callback.timerFinish();
    }

    /**
     * 取消倒计时的方法
     */
    public void onCancle() {
        isstarted = false;
        cancel();
    }

    /**
     * 启动倒计时的方法
     */
    public void onStart() {
        isstarted = true;
        start();
    }

    /**
     * 获取倒计时是否已经启动
     */
    public boolean isstarted() {
        return isstarted;
    }

    /**
     * 设置倒计时是否已经启动
     */
    public void setIsstarted(boolean isstarted) {
        this.isstarted = isstarted;
    }

}
