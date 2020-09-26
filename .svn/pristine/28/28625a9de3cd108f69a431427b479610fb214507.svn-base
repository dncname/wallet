package com.dnc.loc.ui.aty.accountpay;


import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.dnc.loc.base.BaseContract;
import com.dnc.loc.data.eos.EosCreateManger;
import com.dnc.loc.data.http.EosHttp;
import com.dnc.loc.utils.RxUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

public class AccountPayPresenter implements AccountPayContract.Presenter{

    private static final String TAG = "_TAG_AccountPayPresenter";

    private AccountPayContract.View view;
    private EosCreateManger model;
    //private AccountPayVM AccountPay;

    public AccountPayPresenter(AccountPayContract.View view) {
        this.view = view;
        //model = EosCreateManger.getInstance();
    }

    /**
     * 检查公钥，是否有用户名，用户名可能有多个
     * @param _pubKey 账号
     * @return
     */
    @Override
    @SuppressLint("CheckResult")
    public void getKeyAccounts(String _pubKey, BaseContract.Callback callback) {
        String pubKey = _pubKey.replace("DNC", "EOS");
        EosHttp.app.getKeyAccounts(pubKey).compose(RxUtils.observableAsync()).subscribe(userInfo -> {
            if (userInfo != null && userInfo.accountNames != null && userInfo.accountNames.size() > 0) {
                // 账户已创建
                String firstStr = userInfo.accountNames.get(0);
                callback.onback(true, firstStr);
            }
            else {
                // 账户未创建(不走这里)
                callback.onback(false, "账户创建未成功!");
            }
        }, e -> {
            if (e instanceof ConnectException || e instanceof UnknownHostException || e instanceof SocketTimeoutException || e instanceof SSLHandshakeException) {
                callback.onback(false, "请检查网络连接!");
            } else {
                // 账户未创建
                callback.onback(false, "账户创建未成功!");
            }
        });
    }

}

