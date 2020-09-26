package com.dnc.loc.ui.aty.account;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dnc.loc.BR;
import com.dnc.loc.R;
import com.dnc.loc.blockchain.cypto.ec.EosPrivateKey;
import com.dnc.loc.constant.RouteConst;
import com.dnc.loc.constant.SpConst;
import com.dnc.loc.data.http.EosHttp;
import com.dnc.loc.databinding.ActivityAccountImportBinding;
import com.dnc.loc.ui.aty.MainActivity;
import com.dnc.loc.utils.Click;
import com.dnc.loc.utils.RxUtils;
import com.dnc.loc.vm.WalletManage;
import com.dnc.loc.vm.WalletVM;
import com.zlw.base.ui.aty.BaseActivity;
import com.zlw.base.ui.utils.LifecycleCallbacks;

@Route(path = RouteConst.AV_ACCOUNT_IMPORT)
public class AccountImport extends BaseActivity<WalletVM, ActivityAccountImportBinding> {

    private static final String TAG = "_TAG_AccountImport";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_import);
        init();
        initView();
    }
    
    private void init() {
        // 临时变量，当前输入
        bindViewModel(BR.viewModel, new WalletVM());
        //viewModel.pubKey = "";
    }

    private void initView() {
        setAppBarView(binding.appbar);

        binding.ivBack.setOnClickListener(v -> onBackPressed());
        binding.editPri.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                saveKey(editable.toString());
            }
        });
        saveKey(binding.editPri.getText().toString());

        // 第1步导入
        binding.btnImport.setOnClickListener(v -> {
            if(Click.isFastClick()) return;

            String tmpAccount = binding.editAccount.getText().toString();
            if (TextUtils.isEmpty(tmpAccount)) {
                ToastUtils.showShort("请输入正确的私钥！");
                return;
            }

            if (WalletManage.getInstance().containsWallet(tmpAccount)) {
                ToastUtils.showShort("当前账号已导入，请输入其他账号私钥！");
                binding.editPri.setText("");
                binding.editPub.setText("");
                binding.editAccount.setText("");
                binding.editPsw.setText("");
                return;
            }
            binding.countGroup1.setVisibility(View.GONE);
            binding.countGroup2.setVisibility(View.VISIBLE);
        });

        // 第2步确认密码
        binding.btnSubmit.setOnClickListener(v -> {
            if(Click.isFastClick()) return;

            String pswKey = binding.editPsw.getText().toString();
            if (TextUtils.isEmpty(pswKey)) {
                ToastUtils.showShort("请输入密码");
                return;
            }
            if (pswKey.length() < 6) {
                ToastUtils.showShort("请输入6位密码");
                return;
            }

            String psw_valid = binding.editPswValid.getText().toString();
            if (TextUtils.isEmpty(psw_valid)) {
                ToastUtils.showShort("请输入确认密码");
                return;
            }
            if (!TextUtils.equals(pswKey, psw_valid)) {
                ToastUtils.showShort("两次密码不一致");
                return;
            }

            //保存钱包
            WalletManage.getInstance().addWallet(viewModel.userName, viewModel.priKey, viewModel.pubKey, pswKey);
            // 记住状态
            RouteConst.setUserStatus(RouteConst.STATE_LOGIN_OK);
            // clear create tmp data, not import
            SPUtils.getInstance().remove(SpConst.getSpCreateKey(SpConst.P_K));
            SPUtils.getInstance().remove(SpConst.getSpCreateKey(SpConst.P_B_K));
            SPUtils.getInstance().remove(SpConst.getSpCreateKey(SpConst.WALLET_NAME));
            SPUtils.getInstance().remove(SpConst.getSpCreateKey(SpConst.PSW));

            MainActivity.from = "Import";
            ARouter.getInstance().build(RouteConst.AV_MAIN).navigation(this);

            //关闭登录界面
            LifecycleCallbacks.finishActivity(LoginAty.class);
            this.finish();
        });
    }

    private void saveKey(String priKey) {
        viewModel.pubKey = "";
        binding.editPub.setText("");
        viewModel.userName = "";
        binding.editAccount.setText("");

        if (priKey.length()>=51){
            viewModel.priKey = priKey;
            EosPrivateKey privateKey;
            try {
                privateKey = new EosPrivateKey(viewModel.priKey);
            } catch (Exception e) {
                return;
            }
            if (privateKey != null && privateKey.getPublicKey() != null) {
                viewModel.pubKey = privateKey.getPublicKey().toString().replace("EOS", "DNC");
                // 更新公钥
                binding.editPub.setText(viewModel.pubKey);
            }
            if (!TextUtils.isEmpty(viewModel.pubKey)) {
                binding.btnSubmit.setEnabled(false);
                getKeyAccounts(viewModel.pubKey);
            }
        }else {
            viewModel.priKey = "";
        }
        //binding.btnSubmit.setEnabled(false);
    }

    private void saveAccounts(String userName) {
        viewModel.userName = userName;
        binding.editAccount.setText(userName);

//        if (TextUtils.isEmpty(userName)) {
//            binding.btnSubmit.setEnabled(false);
//        }else {
//            binding.btnSubmit.setEnabled(true);
//        }

        // 联网结束再便能按钮
        binding.btnSubmit.setEnabled(true);
    }

    @SuppressLint("CheckResult")
    private void getKeyAccounts(String _pubKey) {
        String pubKey = _pubKey.replace("DNC", "EOS");
        EosHttp.app.getKeyAccounts(pubKey).compose(RxUtils.observableAsync()).subscribe(userInfo -> {
            if (userInfo != null && userInfo.accountNames != null && userInfo.accountNames.size() > 0) {
                    //SPUtils.getInstance().put(SpConst.WALLET_NAME, userInfo.accountNames.get(0));
                    if (!TextUtils.isEmpty(viewModel.userName)) {
                        String firstStr = userInfo.accountNames.get(0);
                        if (!TextUtils.equals(viewModel.userName, firstStr)) {
                            saveAccounts(firstStr);
                            return;
                        }
                    }
                    else {
                        saveAccounts(userInfo.accountNames.get(0));
                        return;
                    }
            }
            else {
                saveAccounts("");
                return;
            }
        }, e -> {
            saveAccounts("");
            return;
        });
    }

}