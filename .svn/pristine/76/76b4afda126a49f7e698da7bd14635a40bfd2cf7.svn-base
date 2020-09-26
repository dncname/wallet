package com.dnc.loc.ui.fm;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.dnc.loc.R;
import com.dnc.loc.constant.RouteConst;
import com.dnc.loc.databinding.LayoutFmKeyCopyBinding;
import com.dnc.loc.utils.CopyUtils;
import com.zlw.base.ui.fm.BaseFragment;
import com.zlw.base.ui.vm.BaseViewModel;

@Route(path = RouteConst.FM_WALLET_KEY_COPY)
public class KeyCopyFm extends BaseFragment<BaseViewModel, LayoutFmKeyCopyBinding> {

    @Autowired(name = RouteConst.WALLET_KEY)
    public String key;
    @Autowired(name = RouteConst.TITLE)
    public String title;
    @Autowired(name = RouteConst.SUB_TITLE)
    public String subTitle;
    private boolean isFirst = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setContentView(R.layout.layout_fm_key_copy);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAppBarView(binding.appbar1);

        if (isFirst) {
            isFirst = false;
            setAppBarView(binding.appbar);
            addPaddingTopEqualStatusBarHeight();
        }
        binding.txtTitle.setText(title);
        binding.textKey.setText(key);
        binding.labelPhone.setText(subTitle);

        binding.ivBack.setOnClickListener(v -> getActivity().onBackPressed());
        binding.btnCopy.setOnClickListener(v -> {
            CopyUtils.copy(v.getContext(), "dnc_key", binding.textKey.getText().toString());
            ToastUtils.showShort("复制成功");
        });
    }


}
