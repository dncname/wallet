package com.dnc.loc.ui.fm.create;


import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dnc.loc.base.BaseContract;
import com.dnc.loc.data.eos.EosCreateManger;
import com.dnc.loc.data.eos.model.UserAvailableList;
import com.dnc.loc.data.http.EosHttp;
import com.dnc.loc.utils.RxUtils;
import com.dnc.loc.vm.WalletVM;
import com.zlw.base.ui.utils.LoadingProgress;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

public class WalletPresenter implements WalletContract.Presenter{

    private static final String TAG = "_TAG_WalletPresenter";
    private static final Boolean isRandomName = false;

    private WalletContract.View view;
    private EosCreateManger model;
    private WalletVM wallet;

    public WalletPresenter(WalletContract.View view) {
        this.view = view;
        model = EosCreateManger.getInstance();
    }

    @Override
    public void createAccount(String priKey, String userName, String newUserPubKey, String newUserName, BaseContract.Callback callback) {
        model.create(newUserPubKey, newUserName, false, callback);
    }

    @Override
    public void createFee(String priKey, String userName, String newUserPubKey, String newUserName, BaseContract.Callback callback) {
        model.setCreator(userName).setCreatorPriKey(priKey)
                .create(newUserPubKey, newUserName, true, callback);
    }

    /**
     * 检查公钥，是否能创建
     *
     * @param createPubKey 账号
     * @param createUserName 账号
     * @return
     */
    @Override
    @SuppressLint("CheckResult")
    public void getNewKeyAccounts(String createPubKey, String createUserName) {
        String pubKey = createPubKey.replace("DNC", "EOS");
        EosHttp.app.getKeyAccounts(pubKey).compose(RxUtils.observableAsync()).subscribe(userInfo -> {
            if (userInfo != null && userInfo.accountNames != null && userInfo.accountNames.size() > 0) {
                view.onback(false, "账户已创建！无需重复创建");
            }
            else {
                if(isRandomName) { // isRandomName == false
                    // 账户未创建 (最多随机5次)
                    //randomNewUserName(pubKey, createUserName, 5);
                }
                else {
                    // 用获取余额的接口验证用户名是否存在
                    getAccountsBalance(createPubKey, createUserName, 0);
                }
            }
        }, e -> {
            //LogUtils.e(TAG, " 连接错误: " + e.toString() );

            if(isRandomName) { // isRandomName == false
                // 账户未创建 (最多随机5次)
                //randomNewUserName(pubKey, createUserName, 5);
            }
            else {
                // 用获取余额的接口验证用户名是否存在
                getAccountsBalance(createPubKey, createUserName, 0);
            }
        });
    }

//    public void randomNewUserName(String createPubKey, String createUserName, int queryCount) {
//        if(wallet == null) {
//            wallet = new WalletVM();
//        }
//        wallet.randomUserName();
//        if(queryCount>0) {
//            // 用获取余额的接口验证用户名是否存在
//            getAccountsBalance(createPubKey, wallet.userName, queryCount - 1);
//        }
//        else {
//            view.onback(false, "无法获取钱包地址!");
//        }
//    }

    @SuppressLint("CheckResult")
    public void getAccountsBalance(String newUserPubKey, String userName, int queryCount) {
        //查询钱包并通知服务端
        //LoadingProgress.showProgress();
        EosHttp.app.getTableRows("eosio", "eosio", "accounts", userName, 20, true)
                .compose(com.dnc.loc.utils.RxUtils.observableAsync())
                .subscribe(o -> {
                    if (o.rows != null && o.rows.size() > 0) {
                        for (UserAvailableList.UserAvailable row : o.rows) {
                            String name = row.name;
                            //LogUtils.e(TAG, " available " + available);
                            if (TextUtils.equals(name, userName)) {
                                if(isRandomName) { // isRandomName == false
                                    // 用户名存在，重新随机一次
                                    //randomNewUserName(newUserPubKey, userName, queryCount);
                                }
                                else {
                                    view.onback(false, "用户名已存在!");
                                }
                                return;
                            }
                        }
                    }
                    // 用户名不存在, 显示密码输入 -> 创建手续费 -> 确认 -> 创建
                    view.showDialog(newUserPubKey, userName);
                }, e -> {
                    //LoadingProgress.dismissProgress();
                    if (e instanceof ConnectException || e instanceof UnknownHostException || e instanceof SocketTimeoutException || e instanceof SSLHandshakeException) {
                        ToastUtils.showShort("请检查网络连接");
                    } else {
                        view.onback(false, "无法获取钱包地址!");
                    }
                });
    }

}