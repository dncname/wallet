package com.zlw.base.ui.utils;

import android.app.Activity;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ActivityUtils;
import com.trycatch.mysnackbar.TSnackbar;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

//依赖ActivityUtils
public class UIToast {
    public static void show(Activity activity, int icon, String message, @TSnackbar.Duration int duration, @TSnackbar.OverSnackAppearDirection int appearDirection) {
        if (activity == null) activity = ActivityUtils.getTopActivity();
        final ViewGroup viewGroup = (ViewGroup) (activity.findViewById(android.R.id.content)).getRootView();
        TSnackbar snackBar = TSnackbar.make(viewGroup, message, duration, appearDirection);
        snackBar.setBackgroundColor(activity.getResources().getColor(com.zlw.base.R.color.day_white));
        snackBar.setMessageTextColor(activity.getResources().getColor(com.zlw.base.R.color.text_black_222222));
        snackBar.setMessageTextSize(ScreenAdapterTools.getInstance().loadCustomAttrValue(16));
        snackBar.addIcon(icon);
        snackBar.show();
        ScreenAdapterTools.getInstance().loadView(snackBar.getView());
    }

    public static void showDefault(int icon, String message) {
        show(null, icon, message, TSnackbar.LENGTH_LONG, TSnackbar.APPEAR_FROM_TOP_TO_DOWN);
    }

    public static void showDefaultAty(Activity activity, int icon, String message) {
        show(activity, icon, message, TSnackbar.LENGTH_LONG, TSnackbar.APPEAR_FROM_TOP_TO_DOWN);
    }
}