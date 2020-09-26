package com.dnc.loc.ui.aty.account;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dnc.loc.BR;
import com.dnc.loc.R;
import com.dnc.loc.base.BaseContract;
import com.dnc.loc.constant.RouteConst;
import com.dnc.loc.constant.SpConst;
import com.dnc.loc.data.eos.model.UserAvailableList;
import com.dnc.loc.data.http.EosHttp;
import com.dnc.loc.databinding.ActivityAccountCreateBinding;
import com.dnc.loc.ui.aty.MainActivity;
import com.dnc.loc.utils.Click;
import com.dnc.loc.utils.EditInputFilter;
import com.dnc.loc.vm.WalletManage;
import com.dnc.loc.vm.WalletVM;
import com.zlw.base.ui.aty.BaseActivity;
import com.zlw.base.ui.utils.LifecycleCallbacks;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

@Route(path = RouteConst.AV_ACCOUNT_CREATE)
public class AccountCreate extends BaseActivity<WalletVM, ActivityAccountCreateBinding> {

    @Autowired(name = RouteConst.USER_STATE)
    public int createState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setContentView(R.layout.activity_account_create);
        setAppBarView(binding.appbar);
        init();
        initView();
    }
    
    private void init() {
        bindViewModel(BR.viewModel, new WalletVM());

        //仅仅测试--状态1
//        if(true) {
//
//            SPUtils.getInstance().remove(SpConst.getSpCreateKey(SpConst.P_K));
//            SPUtils.getInstance().remove(SpConst.getSpCreateKey(SpConst.P_B_K));
//            SPUtils.getInstance().remove(SpConst.getSpCreateKey(SpConst.WALLET_NAME));
//            SPUtils.getInstance().remove(SpConst.getSpCreateKey(SpConst.PSW));
//            // 记住状态
//            RouteConst.setUserStatus(RouteConst.STATE_INIT);
//            createState = RouteConst.STATE_INIT;
//        }


        // 未创建(from 登录、账号管理)
        if(createState == RouteConst.STATE_INIT) {
            // 这一步已经替换 EOS->DNC
            viewModel.getPriPubKey();
            // 用户名
            SPUtils.getInstance().remove(SpConst.getSpCreateKey(SpConst.WALLET_NAME));
            viewModel.randomUserName();

            //binding.btnSubmit.setText(getString(R.string.share_pub));
            binding.tvPriInput.setText(viewModel.priKey);
            binding.tvPubInput.setText(viewModel.pubKey);
            binding.editUserName.setText(viewModel.userName);

            //binding.llUserName.setVisibility(View.GONE);
            //binding.llPsw.setVisibility(View.GONE);
            //binding.llPswConfirm.setVisibility(View.GONE);
        }
        // 已创建(from 登录、账号管理) 为了不重复创建、不重复支付
        else if(createState == RouteConst.STATE_CREATE_OK) {
            // 公钥
            viewModel.pubKey = SPUtils.getInstance().getString(SpConst.getSpCreateKey(SpConst.P_B_K), "None");
            // 私钥(未加密的私钥)
            viewModel.priKey = SPUtils.getInstance().getString(SpConst.getSpCreateKey(SpConst.P_K), "None");
            // 用户
            viewModel.userName = SPUtils.getInstance().getString(SpConst.getSpCreateKey(SpConst.WALLET_NAME), "");
            // 密码
            String psw = SPUtils.getInstance().getString(SpConst.getSpCreateKey(SpConst.PSW), "");

            //binding.btnSubmit.setText(getString(R.string.share_pub));
            binding.tvPriInput.setText(viewModel.priKey);
            binding.tvPubInput.setText(viewModel.pubKey);
            binding.editUserName.setText(viewModel.userName);
            binding.editPsw.setText(psw);
            binding.editPswValid.setText(psw);

            //binding.llUserName.setVisibility(View.GONE);
            //binding.llPsw.setVisibility(View.GONE);
            //binding.llPswConfirm.setVisibility(View.GONE);
        }
        // 已激活(无需要这一步)
        else if(createState == RouteConst.STATE_PUSH_OK) {
            // 钱包地址(按下已创建时保存当前用户名)
            viewModel.userName = SPUtils.getInstance().getString(SpConst.getSpCreateKey(SpConst.WALLET_NAME), "None");
            // 公钥
            viewModel.pubKey = SPUtils.getInstance().getString(SpConst.getSpCreateKey(SpConst.P_B_K), "None");
            // 私钥(未加密的私钥)
            viewModel.priKey = SPUtils.getInstance().getString(SpConst.getSpCreateKey(SpConst.P_K), "None");

            binding.tvPriInput.setText(viewModel.priKey);
            binding.tvPubInput.setText(viewModel.pubKey);
            binding.editUserName.setText(viewModel.userName);
        }
    }

    private void initView() {
        binding.ivBack.setOnClickListener(v -> onBackPressed());

        binding.btnSubmit.setOnClickListener(v -> {
            if(Click.isFastClick()) return;

            verifyAccount();
        });

        binding.editUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 多次保存用户名
                if (TextUtils.isEmpty(editable.toString())) {
                    SPUtils.getInstance().remove(SpConst.getSpCreateKey(SpConst.WALLET_NAME));
                }
                else {
                    SPUtils.getInstance().put(SpConst.getSpCreateKey(SpConst.WALLET_NAME), editable.toString(), false);
                }
            }
        });
        binding.editUserName.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //获取焦点
                    binding.tvAccInfo.setText("");
                    binding.ivAccState.setVisibility(View.GONE);
                } else {
                    //失去焦点
                    String userName = binding.editUserName.getText().toString().trim();
                    if(!userName.isEmpty()) {
                        if (userName.length() < 5) {
                            binding.tvAccInfo.setText("请输入至少5位钱包地址");
                            binding.ivAccState.setImageDrawable(getDrawable(R.mipmap.ic_acc_wrong));
                            binding.ivAccState.setVisibility(View.VISIBLE);
                            return;
                        }
                        checkNewAccount(userName, (isSuccess, o) -> {
                            if(isSuccess) {
                                // 多次保存用户名
                                viewModel.userName = userName;
                                SPUtils.getInstance().put(SpConst.getSpCreateKey(SpConst.WALLET_NAME), viewModel.userName, false);

                                if(createState == RouteConst.STATE_INIT) {
                                    // 记住状态
                                    RouteConst.setUserStatus(RouteConst.STATE_CREATE_OK);
                                    createState = RouteConst.STATE_CREATE_OK;
                                    // 记住账号(未加密的私钥)
                                    SPUtils.getInstance().put(SpConst.getSpCreateKey(SpConst.P_K), viewModel.priKey, false);
                                    SPUtils.getInstance().put(SpConst.getSpCreateKey(SpConst.P_B_K), viewModel.pubKey, false);
                                    // 不保存密码
                                }

                                binding.ivAccState.setImageDrawable(getDrawable(R.mipmap.ic_acc_right));
                                binding.ivAccState.setVisibility(View.VISIBLE);
                            }
                            else {
                                binding.tvAccInfo.setText(o.toString());
                                binding.ivAccState.setImageDrawable(getDrawable(R.mipmap.ic_acc_wrong));
                                binding.ivAccState.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }

            }
        });

        // 随机5次用户名
        // (界面初始化)创建界面默认判断账号是否存在、我已支付界面默认判断公钥是否创建
        // STATE_CREATE_OK一定是验证过用户名, 所有这里初始化不再验证账号是否创建，不一定要判断账号是否存在
        setAccount(5);
    }

    private void setAccount(int times) {
        // 随机失败不会更新文字
        String userName = viewModel.userName;
        if(!userName.isEmpty()) {
            checkNewAccount(userName, (isSuccess, o) -> {
                if(isSuccess) {
                    // 多次保存用户名
                    viewModel.userName = userName;
                    SPUtils.getInstance().put(SpConst.getSpCreateKey(SpConst.WALLET_NAME), viewModel.userName, false);
                    binding.editUserName.setText(viewModel.userName);

                    if(createState == RouteConst.STATE_INIT) {
                        // 记住状态
                        RouteConst.setUserStatus(RouteConst.STATE_CREATE_OK);
                        createState = RouteConst.STATE_CREATE_OK;
                        // 记住账号(未加密的私钥)
                        SPUtils.getInstance().put(SpConst.getSpCreateKey(SpConst.P_K), viewModel.priKey, false);
                        SPUtils.getInstance().put(SpConst.getSpCreateKey(SpConst.P_B_K), viewModel.pubKey, false);
                        // 不保存密码
                    }

                    binding.ivAccState.setImageDrawable(getDrawable(R.mipmap.ic_acc_right));
                    binding.ivAccState.setVisibility(View.VISIBLE);
                }
                else {
                    if(createState == RouteConst.STATE_INIT) {
                        if(times > 0) {
                            // 用户名
                            SPUtils.getInstance().remove(SpConst.getSpCreateKey(SpConst.WALLET_NAME));
                            viewModel.randomUserName();
                            setAccount(times - 1);
                        }
                        return;
                    }
                    // 这句无用，设置失败标志
                    // 第2次进入只设置成功标志，不设置失败标志
                    //binding.tvAccInfo.setText(o.toString());
                    //binding.ivAccState.setImageDrawable(getDrawable(R.mipmap.ic_acc_wrong));
                    //binding.ivAccState.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(createState == RouteConst.STATE_CREATE_OK) {
                // 空密码时清除密码缓存
                String psw = binding.editPsw.getText().toString();
                if(psw.isEmpty()) {
                    SPUtils.getInstance().remove(SpConst.getSpCreateKey(SpConst.PSW));
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void verifyAccount() {
        String psw = binding.editPsw.getText().toString();
        if (TextUtils.isEmpty(psw)) {
            ToastUtils.showShort("请输入密码");
            return;
        }
        if (psw.length() < 6) {
            ToastUtils.showShort("请输入6位密码");
            return;
        }

        String psw_valid = binding.editPswValid.getText().toString();
        if (TextUtils.isEmpty(psw_valid)) {
            ToastUtils.showShort("请输入确认密码");
            return;
        }
        if (!TextUtils.equals(psw, psw_valid)) {
            ToastUtils.showShort("两次密码不一致");
            return;
        }

        String userName = binding.editUserName.getText().toString().trim();
        if (userName.length() < 5) {
            ToastUtils.showShort("请输入至少5位钱包地址");
            return;
        }

        if(WalletManage.getInstance().containsWallet(userName)) {
            ToastUtils.showShort(userName + "已添加到我的账户列表，请更换其他钱包地址");
            return;
        }

        // 再次检查一遍账号是否有效
        checkNewAccount(userName, (isSuccess, o) -> {
            if(isSuccess) {
                // 没有创建过
                binding.ivAccState.setImageDrawable(getDrawable(R.mipmap.ic_acc_right));
                binding.ivAccState.setVisibility(View.VISIBLE);
                // 多次保存用户名
                viewModel.userName = binding.editUserName.getText().toString().trim();
                SPUtils.getInstance().put(SpConst.getSpCreateKey(SpConst.WALLET_NAME), viewModel.userName, false);
                //
                shareAccount(psw);
            }
            else {
                // 记住状态
                RouteConst.setUserStatus(RouteConst.STATE_CREATE_OK);
                // 记住账号(未加密的私钥)
                SPUtils.getInstance().put(SpConst.getSpCreateKey(SpConst.P_K), viewModel.priKey, false);
                SPUtils.getInstance().put(SpConst.getSpCreateKey(SpConst.P_B_K), viewModel.pubKey, false);
                SPUtils.getInstance().put(SpConst.getSpCreateKey(SpConst.PSW), psw, false);
                SPUtils.getInstance().put(SpConst.getSpCreateKey(SpConst.WALLET_NAME), viewModel.userName, false);

                WalletManage walletManage = WalletManage.getInstance();
                walletManage.checkLoginStatus((isSuccess1, o1) -> {
                    if(isSuccess1) {
                        // 更新新增账号
                        ToastUtils.showLong("账号" + o1 + "已创建");
                        MainActivity.from = "Create";
                        ARouter.getInstance().build(RouteConst.AV_MAIN).navigation(this);
                        //关闭登录界面
                        LifecycleCallbacks.finishActivity(LoginAty.class);
                        finish();
                        return;
                    }
                    else {
                        // 注意是第1个o
                        binding.tvAccInfo.setText(o.toString());
                        binding.ivAccState.setImageDrawable(getDrawable(R.mipmap.ic_acc_wrong));
                        binding.ivAccState.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    private void shareAccount(String psw) {
        // 记住状态
        RouteConst.setUserStatus(RouteConst.STATE_CREATE_OK);
        // 记住账号(未加密的私钥)
        SPUtils.getInstance().put(SpConst.getSpCreateKey(SpConst.P_K), viewModel.priKey, false);
        SPUtils.getInstance().put(SpConst.getSpCreateKey(SpConst.P_B_K), viewModel.pubKey, false);
        SPUtils.getInstance().put(SpConst.getSpCreateKey(SpConst.PSW), psw, false);
        SPUtils.getInstance().put(SpConst.getSpCreateKey(SpConst.WALLET_NAME), viewModel.userName, false);

        ARouter.getInstance().build(RouteConst.AV_ACCOUNT_PAY)
                .withString(RouteConst.WALLET_USER_NAME , viewModel.userName)
                .withString(RouteConst.WALLET_PUB_KEY , viewModel.pubKey)
                .navigation(this);

        // 当前状态结束，关闭界面
        finish();
    }

    /**
     * 是否能创建
     * @param userName 账号
     * @return
     */
    @SuppressLint("CheckResult")
    public void checkNewAccount(String userName,  BaseContract.Callback callback) {
        binding.tvAccInfo.setText("");
        //查询钱包并通知服务端
        //LoadingProgress.showProgress();
        EosHttp.app.getTableRows("eosio", "eosio", "accounts", userName, 20, true)
                .compose(com.dnc.loc.utils.RxUtils.observableAsync())
                .subscribe(o -> {
                    if (o.rows != null && o.rows.size() > 0) {
                        for (UserAvailableList.UserAvailable row : o.rows) {
                            String name = row.name;
                            if (TextUtils.equals(name, userName)) {
                                // 用户名存在，重新随机一次
                                //ToastUtils.showShort(userName + "已存在，请更换其他钱包地址");
                                callback.onback(false, "此钱包地址已存在，请更换其他名称");
                                return;
                            }
                        }
                    }
                    else {
                        if(callback != null) {
                            callback.onback(true, "");
                        }
                    }
                }, e -> {
                    //LoadingProgress.dismissProgress();
                    if (e instanceof ConnectException || e instanceof UnknownHostException || e instanceof SocketTimeoutException || e instanceof SSLHandshakeException) {
                        ToastUtils.showShort("请检查网络连接");
                    } else {
                        //view.onback(false, "无法获取钱包地址!");
                    }
                });
    }
}
