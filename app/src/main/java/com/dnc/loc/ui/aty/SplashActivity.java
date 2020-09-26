package com.dnc.loc.ui.aty;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dnc.loc.R;
import com.dnc.loc.constant.RouteConst;
import com.dnc.loc.ui.widget.MatrixImageView;
import com.dnc.loc.utils.RxUtils;
import com.dnc.loc.vm.WalletManage;
import com.dnc.loc.vm.WalletVM;
import com.yatoooon.screenadaptation.ScreenAdapterTools;
import com.zlw.base.ui.utils.ViewUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;


/**
 */
//@Keep
@Route(path = RouteConst.AV_SPLASH)
public class SplashActivity extends Activity {
    //private static final String TAG = "_TAG_Splash";

    private Disposable disposable;


    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        BarUtils.setNavBarVisibility(this, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            BarUtils.setNavBarImmersive(this);
        }
        BarUtils.setNotificationBarVisibility(false);


        setContentView(R.layout.activity_splash);
        ScreenAdapterTools.getInstance().loadView(findViewById(R.id.rl_splash));

        setSplashImageSize();

        // 可能会加延时--直到创建失效、有效。
        if (disposable != null && !disposable.isDisposed()) return;
        disposable = Observable.interval(1, 500, TimeUnit.MILLISECONDS)
                .compose(RxUtils.observableAsync())
                .subscribe(l -> {
                    if (l >= 4 ) {
                        ARouter.getInstance().build(RouteConst.AV_PATH_LOGIN).navigation(this);
                        disposable.dispose();
                        finish();
                    }
                });

        WalletManage walletManage = WalletManage.getInstance();
        walletManage.checkLoginStatus(null);
    }

    /**
     * 底部裁剪
     */
    private void setSplashImageSize() {
        MatrixImageView image_splash = (MatrixImageView) findViewById(R.id.image_splash);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        Matrix matrix=new Matrix();
        Bitmap mBitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.image_splash);
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        float scale = screenWidth * 1.0f / width;
        int barHeight = ViewUtils.getStatusBarHeight(this);
        matrix.setScale(scale, scale);
        matrix.postTranslate(0, -(height*scale-screenHeight-barHeight));//+80

        image_splash.setBitmap(R.mipmap.image_splash);
        image_splash.setImageMatrix(matrix);
    }

}
