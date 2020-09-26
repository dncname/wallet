package com.zlw.base.ui.aty;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.BarUtils;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.yatoooon.screenadaptation.ScreenAdapterTools;
import com.zlw.base.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;

public abstract class BaseActivity<VM extends BaseObservable, B extends ViewDataBinding> extends RxLifeAndSwipeBackAty {

    public B binding;
    public VM viewModel;
    private boolean isOnBind;


//    @Override
//    protected void attachBaseContext(Context newBase) {
//        Context context = LocaleSettingContextWrapper.wrap(newBase);
//        super.attachBaseContext(context);
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarLightMode(this, true);

        if (getApplication() instanceof InitAppInterface) {
            if(!((InitAppInterface) getApplication()).isInited()){
                ((InitAppInterface) getApplication()).init();
            }
        }
//        ClassicsFooter.REFRESH_FOOTER_PULLING = getString(R.string.footer_pulling);//"上拉加载更多";
//        ClassicsFooter.REFRESH_FOOTER_RELEASE = getString(R.string.footer_release);//"释放立即加载";
//        ClassicsFooter.REFRESH_FOOTER_REFRESHING = getString(R.string.footer_refreshing);//"正在刷新...";
//        ClassicsFooter.REFRESH_FOOTER_LOADING = getString(R.string.footer_loading);//"正在加载...";
//        ClassicsFooter.REFRESH_FOOTER_FINISH = getString(R.string.footer_finish);//"加载完成";
//        ClassicsFooter.REFRESH_FOOTER_FAILED = getString(R.string.footer_failed);//"加载失败";
        ClassicsFooter.REFRESH_FOOTER_NOTHING = getString(R.string.no_more);//"没有更多数据了";
        try {
            EventBus.getDefault().register(this);
        } catch (EventBusException ex) {
//            if (!ex.getStackTrace()[0].getMethodName().equals("findSubscriberMethods")) { //混淆后方法名改了  不能用
//                throw ex;
//            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (isTaskRoot()) {
//            setSwipeBackEnable(false);
//        } else {
//            setSwipeBackEnable(true);
//        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResId) {
        if (!isOnBind) {
            isOnBind = true;
            try {
                binding = DataBindingUtil.setContentView(this, layoutResId);
                if(binding.getRoot() instanceof ViewGroup){
                    ScreenAdapterTools.getInstance().loadView(binding.getRoot());
                }
            } catch (RuntimeException e) {
                super.setContentView(layoutResId);
            }
        } else {
            super.setContentView(layoutResId);
        }
    }

    public void bindViewModel(@IdRes int observableId, VM viewModel) {
        this.viewModel = viewModel;
        binding.setVariable(observableId, viewModel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        binding = null;
        viewModel = null;
        isOnBind = false;
        System.gc();
    }

    protected void setVisiable(int visiable, int... ids){
        for (int id :ids) {
            if(findViewById(id) == null) return;
            findViewById(id).setVisibility(visiable);
        }
    }
    protected void setVisiable(int visiable, View... views){
        for (View view :views) {
            view.setVisibility(visiable);
        }
    }

}
