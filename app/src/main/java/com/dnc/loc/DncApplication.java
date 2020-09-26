package com.dnc.loc;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.umeng.commonsdk.UMConfigure;
import com.dnc.loc.data.http.EosHttp;
import com.yatoooon.screenadaptation.ScreenAdapterTools;
import com.zlw.base.ui.utils.LifecycleCallbacks;

import io.reactivex.plugins.RxJavaPlugins;

public class DncApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        ScreenAdapterTools.init(this);
        EosHttp.init(this);
//        ARouter.openLog();
//        ARouter.openDebug();
        ARouter.init(this);
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
//        MobclickAgent.setCatchUncaughtExceptions(false);
        registerActivityLifecycleCallbacks(LifecycleCallbacks.callback(((activity, bundle) -> {
        })));
        RxJavaPlugins.setErrorHandler(throwable -> {
//            ToastUtils.showShort("全局RxJava异常=>" + throwable.getMessage());
            LogUtils.e(throwable.getMessage());
        });
    }
}
