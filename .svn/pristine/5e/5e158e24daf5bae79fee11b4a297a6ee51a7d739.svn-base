package com.dnc.loc.utils;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.zlw.base.ui.widget.CircleImageView;


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
    public static void showLoading(SwipeRefreshLayout swipeRefreshLayout, boolean showLoading) {
        if (!showLoading) {
            swipeRefreshLayout.setRefreshing(showLoading);
        }
    }


    @BindingAdapter({"border_color"})
    public static void borderColor(CircleImageView imageView, int color) {
        imageView.setBorderColor(color);
    }


//
//    @BindingAdapter(value = {"imageRes"}, requireAll = false)
//    public static void imageRes(ImageView view, String url) {
//        if (TextUtils.isEmpty(url)) {
//            return;
//        }
//        Http.getPicasso(view.getContext()).load(url).into(view);
//    }


}
