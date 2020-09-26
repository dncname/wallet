package com.zlw.base.ui.aty;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.aitangba.swipeback.SwipeBackHelper;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zlw.base.R;
import com.zlw.base.ui.utils.StatusBarUtils;
import com.zlw.base.ui.utils.ViewUtils;


public abstract class RxLifeAndSwipeBackAty extends RxAppCompatActivity implements SwipeBackHelper.SlideBackManager {
//    private SwipeBackActivityHelper mHelper;
private SwipeBackHelper mSwipeBackHelper;

    private View appBarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmerseLayout();
//        mHelper = new SwipeBackActivityHelper(this);
//        mHelper.onActivityCreate();
//        setSwipeBackEnable(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
//        if (v == null && mHelper != null)
//            return mHelper.findViewById(id);
        return v;
    }

//    @Override
//    public SwipeBackLayout getSwipeBackLayout() {
//        return mHelper.getSwipeBackLayout();
//    }
//
//    @Override
//    public void setSwipeBackEnable(boolean enable) {
//        getSwipeBackLayout().setEnableGesture(enable);
//    }
//
//    @Override
//    public void scrollToFinishActivity() {
//        Utils.convertActivityToTranslucent(this);
//        getSwipeBackLayout().scrollToFinishActivity();
//    }

    public void appInitSuccess() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appBarView = null;
        mSwipeBackHelper = null;
    }

    //在需要的时候设置
    protected void setImmerseLayout() {
        //新的sdk不用设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected void setAppBarView(View view) {
        if(view != null) {
            appBarView = view;
        }
        StatusBarUtils.setDrawable(this, R.drawable.shape_toolbar_bg);
    }

//新的sdk不用设置
    @Override
    protected void onStart() {
        super.onStart();
        if (appBarView == null)
            return;

        // 冲突
        // 设置高度与StatusBarUtils.setDrawable(...)冲突
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            int height = ViewUtils.getStatusBarHeight(this);
//            appBarView.setPadding(0, height, 0, 0);
//        }
    }

    @Override


    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.backin, R.anim.backout);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(mSwipeBackHelper == null) {
            mSwipeBackHelper = new SwipeBackHelper(this);
        }
        return mSwipeBackHelper.processTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    public Activity getSlideActivity() {
        return this;
    }

    @Override
    public boolean supportSlideBack() {
        return true;
    }

    @Override
    public boolean canBeSlideBack() {
        return true;
    }

    @Override
    public void finish() {
        if(mSwipeBackHelper != null) {
            mSwipeBackHelper.finishSwipeImmediately();
            mSwipeBackHelper = null;
        }
        super.finish();
    }
}
