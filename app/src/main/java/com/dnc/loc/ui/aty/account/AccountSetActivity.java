package com.dnc.loc.ui.aty.account;


import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dnc.loc.R;
import com.dnc.loc.constant.RouteConst;
import com.dnc.loc.databinding.ActivityAccountSetBinding;
import com.dnc.loc.ui.dialog.PromptDialog;
import com.dnc.loc.ui.widget.HGSoftInput;
import com.dnc.loc.vm.WalletManage;
import com.dnc.loc.vm.WalletVM;
import com.zlw.base.ui.aty.BaseActivity;
import com.zlw.base.ui.vm.BaseViewModel;

@Route(path = RouteConst.AV_ACCOUNT_SET)
public class AccountSetActivity extends BaseActivity<BaseViewModel, ActivityAccountSetBinding> {


    @Autowired(name = "walletId")
    public int walletId;

    public WalletVM walletVM;

    private PromptDialog deleteDialog;
    private HGSoftInput mHGSoftInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setContentView(R.layout.activity_account_set);
        setAppBarView(binding.appbar);
        initView();
    }

    private void initView() {
        WalletManage  walletManage = WalletManage.getInstance();
        if(walletId < walletManage.datas.size()) {
            walletVM = walletManage.datas.get(walletId);
        }
        else {
            walletVM = new WalletVM();
        }

        binding.txtAccountName.setText(walletVM.userName);
        binding.txtAddr.setText(walletVM.pubKey);

        binding.ivBack.setOnClickListener(v -> onBackPressed());
        //
        binding.btnRemove.setOnClickListener(v -> {
            //
            showD();
        });
        // 备份
        binding.llBak.setOnClickListener(v -> {
            if (mHGSoftInput != null) {
                mHGSoftInput.dismiss();
            }
            mHGSoftInput = new HGSoftInput(this);
            mHGSoftInput.setData(key -> {
                //解码测试:
                //String tmpPri = "00439E8F72F113B8262532424B56824163A3B024011BCA7A9C47D694315EABB05C4C7EA6BD07C951564D93CEF1D7606CDB3034995201DB804AFACD3E9B3A0C63";
                //
                String text = walletVM.priKey;
                if (TextUtils.isEmpty(text)) {
                    return;
                }
                byte[] bytes = EncryptUtils.decryptHexStringAES(text,
                        EncryptUtils.encryptMD5ToString(key.getBytes()).getBytes(),
                        "AES/CBC/PKCS5Padding",
                        walletVM.hexString2Bytes(walletVM.iv));
                if (bytes == null || bytes.length == 0) {
                    ToastUtils.showLong("密码错误");
                    mHGSoftInput.clearInput();
                    return;
                }
                String pri = new String(bytes);

                ARouter.getInstance().build(RouteConst.AV_ACCOUNT_SUB)
                        .withString(RouteConst.KEY_FM_PATH, RouteConst.FM_WALLET_KEY_COPY)
                        .withString(RouteConst.TITLE, "备份私钥")
                        .withString(RouteConst.SUB_TITLE, "账户私钥")
                        .withString(RouteConst.WALLET_KEY, pri).navigation(this);
                mHGSoftInput.dismiss();
            });
            mHGSoftInput.show();
        });
    }

    private void removeAccount() {
        WalletManage walletManage = WalletManage.getInstance();
        walletManage.removeWallet(walletVM);
    }

    private void showD() {
        if (deleteDialog != null){
            deleteDialog.dismiss();
        }
        deleteDialog = new PromptDialog(this, R.style.prompt_dialog);

        String content = getString(R.string.content_delete) + walletVM.userName;
        deleteDialog.showTextDialog(getString(R.string.title_delete), content, v -> {
            deleteDialog.dismiss();
        }, v1 -> {
            removeAccount();
            //
            deleteDialog.dismiss();
            finish();
        });
    }


    @Override
    protected void onDestroy() {
        if (deleteDialog != null){
            deleteDialog.dismiss();
        }
        if(mHGSoftInput != null) {
            mHGSoftInput.dismiss();
        }
        super.onDestroy();
    }


}

