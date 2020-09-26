package com.dnc.loc.ui.aty;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dnc.loc.constant.RouteConst;
import com.zlw.base.ui.utils.LifecycleCallbacks;

public class RouteFilterActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        String path = uri.getPath();
        if (TextUtils.equals(path, RouteConst.AV_MAIN)) {
            MainActivity.from = "";
            ARouter.getInstance().build(uri).navigation(this);
        } else {
            if (LifecycleCallbacks.getActivity(MainActivity.class) == null) {
                MainActivity.from = "";
                ARouter.getInstance().build(RouteConst.AV_MAIN).navigation(this);

            }
        }
        finish();
    }
}