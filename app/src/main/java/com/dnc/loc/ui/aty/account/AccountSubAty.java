package com.dnc.loc.ui.aty.account;


import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.FragmentUtils;
import com.dnc.loc.R;
import com.dnc.loc.constant.RouteConst;
import com.dnc.loc.databinding.ActivityAccountSubBinding;
import com.zlw.base.ui.aty.BaseActivity;
import com.zlw.base.ui.vm.BaseViewModel;

@Route(path = RouteConst.AV_ACCOUNT_SUB)
public class AccountSubAty extends BaseActivity<BaseViewModel, ActivityAccountSubBinding> {

    @Autowired(name = RouteConst.KEY_FM_PATH)
    public String path;

    @Autowired(name = RouteConst.WALLET_KEY)
    public String key;

    @Autowired(name = RouteConst.TITLE)
    public String title;
    @Autowired(name = RouteConst.SUB_TITLE)
    public String subTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setContentView(R.layout.activity_account_sub);
        setAppBarView(null);
        initView();
    }

    private void initView() {
        Fragment subFm = (Fragment) ARouter.getInstance()
                .build(path)
                .withString(RouteConst.WALLET_KEY, TextUtils.isEmpty(key) ? "" : key)
                .withString(RouteConst.TITLE, TextUtils.isEmpty(title) ? "" : title)
                .withString(RouteConst.SUB_TITLE, TextUtils.isEmpty(subTitle) ? "" : subTitle)
                .navigation();

        FragmentUtils.add(getSupportFragmentManager(), subFm, R.id.container);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}