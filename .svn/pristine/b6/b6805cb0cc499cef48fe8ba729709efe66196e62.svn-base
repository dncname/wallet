package com.zlw.base.ui.fm;

import android.content.Context;
import androidx.databinding.BaseObservable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SPUtils;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.yatoooon.screenadaptation.ScreenAdapterTools;
import com.zlw.base.R;
import com.zlw.base.ui.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;

import ezy.ui.layout.LoadingLayout;

public abstract class BaseFragment<VM extends BaseObservable, B extends ViewDataBinding> extends RxFragment {

    public B binding;
    public VM viewModel;
    private
    @LayoutRes
    int layoutId;
//    private TextView errorView;
//    private View loadingView;
//    private View emptyView;
//    private View mainView;
    public ViewGroup appBarView;
    private boolean isRegister = false;
    public LoadingLayout loading;

    public void setContentView(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (layoutId == 0) {
            try {
                throw new Throwable("setContentView(int layoutId) first");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, layoutId, container, false);
        }
        return binding == null ? null : binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!isRegister) {
            isRegister = true;
            if(view instanceof ViewGroup){
                ScreenAdapterTools.getInstance().loadView((ViewGroup) view);
            }
            try {
                EventBus.getDefault().register(this);
            } catch (EventBusException ex) {
//                if (!ex.getStackTrace()[0].getMethodName().equals("findSubscriberMethods")) {
//                    throw ex;
//                }
            }
            loading = view.findViewById(R.id.loading);
        }
    }

    public void bindViewModel(@IdRes int observableId, VM viewModel) {
        this.viewModel = viewModel;
        binding.setVariable(observableId, viewModel);
    }

    public void resetAppBarView(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int height = ViewUtils.getStatusBarHeight(getActivity());
            view.setPadding(0, height, 0, 0);
        }
    }

    public void resetToolBarView(View toolbar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams lp = toolbar.getLayoutParams();
            int toolbarHeight = lp.height;
            lp.height = toolbarHeight + ViewUtils.getStatusBarHeight(getActivity());
            toolbar.setLayoutParams(lp);
        }
    }


    public void toast(String str) {
        if(TextUtils.isEmpty(str)) return;
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }


//    public void clearError() {
//        errorView.setVisibility(View.GONE);
//    }

//    protected void setLoadingView(View view) {
//        loadingView = view;
//    }
//    protected void setMainView(View view) {
//        this.mainView = view;
//    }
//
//    public void setEmptyView(View view) {
//        this.emptyView = view;
//        emptyView.setVisibility(View.GONE);
//    }
//
//    protected void setErrorView(TextView errorView) {
//        this.errorView = errorView;
//        errorView.setVisibility(View.GONE);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        binding = null;
        loading = null;
//        errorView = null;
//        mainView = null;
//        loadingView = null;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void showEmpty() {
        loading.showEmpty();
    }

    public void showLoading() {
//        if(loadingView != null) loadingView.setVisibility(View.VISIBLE);
//        if(errorView != null) errorView.setVisibility(View.GONE);
//        if(emptyView != null) emptyView.setVisibility(View.GONE);
//        if(mainView != null) mainView.setVisibility(View.GONE);

        loading.showLoading();
    }

//    public void hidLoading() {
//        if(loadingView != null) loadingView.setVisibility(View.GONE);
//        if(errorView != null) errorView.setVisibility(View.GONE);
//        if(emptyView != null) emptyView.setVisibility(View.GONE);
//        if(mainView != null) mainView.setVisibility(View.VISIBLE);
//    }

    public void showContent(){
        loading.showContent();
    }
    public void showError(String error) {
//        if(loadingView != null) loadingView.setVisibility(View.GONE);
//        if(errorView != null) {
//            errorView.setVisibility(View.VISIBLE);
//            errorView.setText(error);
//        }
//        if(emptyView != null) emptyView.setVisibility(View.GONE);
//        if(mainView != null) mainView.setVisibility(View.GONE);
        loading.showError();
        loading.setErrorText(error);
    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(getClass().getSimpleName()); // 统计页面
    }

    public void setAppBarView(ViewGroup appBarView) {
        this.appBarView = appBarView;
    }

    public void addPaddingTopEqualStatusBarHeight(){
        // 冲突
        // 设置高度与StatusBarUtils.setDrawable(...)冲突
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            int height = BarUtils.getStatusBarHeight();
//            appBarView.setPadding(0, height, 0, 0);
//        }
    }

}