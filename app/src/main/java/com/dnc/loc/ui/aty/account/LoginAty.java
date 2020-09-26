package com.dnc.loc.ui.aty.account;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.dnc.loc.R;
import com.dnc.loc.constant.RouteConst;
import com.dnc.loc.databinding.ActivityAccountHomeBinding;
import com.dnc.loc.ui.aty.MainActivity;
import com.dnc.loc.vm.WalletManage;
import com.zlw.base.ui.aty.BaseActivity;
import com.zlw.base.ui.utils.ViewUtils;
import com.zlw.base.ui.vm.BaseViewModel;

@Route(path = RouteConst.AV_PATH_LOGIN)
public class LoginAty extends BaseActivity<BaseViewModel, ActivityAccountHomeBinding> {
    private static final String TAG = "_TAG_Login";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_home);

        if (RouteConst.checkUserStatus(true) == RouteConst.STATE_LOGIN_OK) { //已登录
            MainActivity.from = "Login";
            ARouter.getInstance().build(RouteConst.AV_MAIN).navigation(this);
            finish();
            return;
        }
        setLoginImageSize();

        binding.txtBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.llCreate.setOnClickListener(v -> {
            // 随机有可能已激活
            if (RouteConst.checkUserStatus(true) == RouteConst.STATE_LOGIN_OK) { //已登录
                MainActivity.from = "Login";
                ARouter.getInstance().build(RouteConst.AV_MAIN).navigation(this);
                finish();
                return;
            }

            WalletManage walletManage = WalletManage.getInstance();
            if(walletManage.datas.size() >= 5) {
                // 应该不可能走这里
                ToastUtils.showLong(getString(R.string.account_max_number));
                return;
            }

            // 随机有可能已激活
            if (RouteConst.checkUserStatus(false) == RouteConst.STATE_CREATE_OK) {
                //WalletManage walletManage = WalletManage.getInstance();
                walletManage.checkLoginStatus((isSuccess, o) -> {
                    if(isSuccess) {
                        // 更新新增账号
                        ToastUtils.showLong("账号" + o + "已创建");
                        MainActivity.from = "Login";
                        ARouter.getInstance().build(RouteConst.AV_MAIN).navigation(this);
                        finish();
                    }
                    else {
                        ARouter.getInstance().build(RouteConst.AV_ACCOUNT_CREATE)
                                .withInt(RouteConst.USER_STATE , RouteConst.checkUserStatus(false))
                                .navigation(this);
                    }
                });
            }
            else {
                ARouter.getInstance().build(RouteConst.AV_ACCOUNT_CREATE)
                        .withInt(RouteConst.USER_STATE , RouteConst.checkUserStatus(false))
                        .navigation(this);
            }
        });

        binding.llImport.setOnClickListener(v ->
            ARouter.getInstance().build(RouteConst.AV_ACCOUNT_IMPORT).navigation(this)
        );
    }

    /**
     * 底部裁剪
     */
    private void setLoginImageSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        Matrix matrix=new Matrix();
        Bitmap mBitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.image_login);
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        float scale = screenWidth * 1.0f / width;
        int barHeight = ViewUtils.getStatusBarHeight(this);
        matrix.setScale(scale, scale);
        matrix.postTranslate(0, -(height*scale-screenHeight-barHeight));//+80

        binding.ivLoginBg.setBitmap(R.mipmap.image_login);
        binding.ivLoginBg.setImageMatrix(matrix);
    }

    @Override
    public boolean canBeSlideBack() {
        return false;
    }

    @Override
    public boolean supportSlideBack() {
        return false;
    }

}
