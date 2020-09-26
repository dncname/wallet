package com.zlw.base.ui.aty;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.Locale;

public class LocaleSettingContextWrapper extends android.content.ContextWrapper {

    public LocaleSettingContextWrapper(Context base) {
        super(base);
    }

    public static ContextWrapper wrap(Context context) {
        Resources res = context.getResources();
        Configuration configuration = res.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(getLocale());
            LocaleList localeList = new LocaleList(getLocale());
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            context = context.createConfigurationContext(configuration);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            configuration.setLocale(getLocale());
            context = context.createConfigurationContext(configuration);
        }
        return new ContextWrapper(context);
    }

    private static Locale getLocale() {
        //读取SharedPreferences数据，默认选中第一项
        int language = SPUtils.getInstance("setting", Context.MODE_PRIVATE).getInt("language", 0);
        switch (language) {
            case 0://默认英语
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    return Locale.ENGLISH;
                }
                break;
            case 1://中文
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    return Locale.CHINA;
                }
                break;
            case 2://英语
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    return new Locale("id", "ID");
                }
                break;
            default:
                break;
        }
        return Locale.ENGLISH;
    }
}
