package com.zlw.base.ui.vm;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.zlw.base.BR;


/**
 */
public class BaseViewModel extends BaseObservable {

    //    private String error;//错误
//    private boolean isRefresh; // 刷新中
//    private boolean showLoading; // 首次加载显示
    private boolean isNullData; // 暂无数据
//
    protected Callback back;

    public void setCallback(Callback callback) {
        this.back = callback;
    }

    public Callback getCallback(){
        return back;
    }
//
//    @Bindable
//    public boolean isRefresh() {
//        return isRefresh;
//    }
//
//    public void setRefresh(boolean refresh) {
//        isRefresh = refresh;
//        notifyPropertyChanged(BR.refresh);
//    }
//
//    @Bindable
//    public boolean isShowLoading() {
//        return showLoading;
//    }
//
//    public void setShowLoading(boolean showLoading) {
//        setError(null);
//        this.showLoading = showLoading;
//        notifyPropertyChanged(BR.showLoading);
//    }
//
    @Bindable
    public boolean isNullData() {
        return isNullData;
    }

    public void setNullData(boolean isNullData) {
        this.isNullData = isNullData;
        notifyPropertyChanged(BR.nullData);
    }
//
//    @Bindable
//    public String getError() {
//        return error;
//    }
//
//    public void setError(String error) {
//        setShowLoading(false);
//        this.error = error;
//        notifyPropertyChanged(BR.error);
//    }

    public interface Callback {
        public static final int SUCCESS = 1;
        public static final int FAILURE = 0;

        void onResult(int status, String msg);
    }
}