package com.zlw.base.ui.utils;

import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * 作者：tac on 2016/11/18 18:46
 * 邮箱：tangcheng.hn@gmail.com
 */
public class BindingUtils {

    @BindingAdapter({"txtColor"})
    public static void txtColor(TextView view, int textColor) {
        view.setTextColor(textColor);
    }

    @BindingAdapter({"toggleText"})
    public static void toggleText(TextView view, boolean isClose) {
        if (isClose) {
            view.setEllipsize(null); // 展开
            view.setSingleLine(false);
            return;
        }
        view.setEllipsize(TextUtils.TruncateAt.END); // 收缩
        view.setLines(2);
    }

    @BindingAdapter({"showLoading"})
    public static void showLoading(SwipeRefreshLayout swipeRefreshLayout, boolean showLoading){
        if(!showLoading){
            swipeRefreshLayout.setRefreshing(showLoading);
        }
    }

}
