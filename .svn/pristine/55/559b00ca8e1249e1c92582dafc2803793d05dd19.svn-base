package com.dnc.loc.vm;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dnc.loc.constant.SpConst;
import com.dnc.loc.data.eos.model.ChainRecord;
import com.dnc.loc.data.eos.model.UserAvailableList;
import com.dnc.loc.data.http.EosHttp;
import com.zlw.base.ui.vm.BaseViewModel;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;

public class WalletTransaction extends BaseViewModel {

     static final String TAG = "_TAG_WalletTransaction";

    // 余额
    public HashMap<String, String> balanceMap;
    // 下标 0~4
    public int index;

    public boolean isHttpError;


    public String getBalance(String token) {
        token = token.replace("EOS", "DNC");
        if (balanceMap != null && balanceMap.containsKey(token)) {
            return balanceMap.get(token);
        }
        if (isHttpError) return "获取不到";
        return "0.0000";
    }

    public List<ChainRecord.RecordItem> getRecord() {
        List<ChainRecord.RecordItem> list = new ArrayList<>();
        // 测试数据
//        for(int i=0; i<5; i++) {
//            ChainRecord.RecordItem record = new ChainRecord.RecordItem();
//            record.from = "AAA";
//            record.to   = "BBB";
//            record.money = "10";
//            record.blockTime = "2020.05.05";
//            list.add(record);
//        }
        return list;
    }

    // 获取当前钱包私钥
    public String getCurrentPriKey() {
        return SPUtils.getInstance().getString(SpConst.getSpWalletKey(index, SpConst.P_K), "None");
    }


    @SuppressLint("CheckResult")
    public void newAccount(String pkey) {
    }

    //    public String sign() {
//        return EncryptUtils.encryptMD5ToString("new_username=" + userName + "&pub_key=" + pubKey + "&key=" + systemKey);
//    }

    @SuppressLint("CheckResult")
    public void notifyServer(String userName) {
        //查询钱包并通知服务端
        //LoadingProgress.showProgress();
        EosHttp.app.getTableRows("eosio", "eosio", "accounts", userName, 20, true)
                .compose(com.dnc.loc.utils.RxUtils.observableAsync())
                .subscribe(o -> {
                    balanceMap.clear();

                    if (o.rows != null && o.rows.size() > 0) {
                        for (UserAvailableList.UserAvailable row : o.rows) {
                            String name = row.name;
                            String available = row.available;
                            //LogUtils.e(TAG, " available " + available);
                            if (!TextUtils.isEmpty(available) && TextUtils.equals(name, userName)) {
                                // 解析
                                String[] balance = available.split(" ");
                                if(balance.length >= 2) {
                                    //LogUtils.e(TAG, " 0 " + balance[0] + " 1 " + balance[1]);
                                    balanceMap.put(balance[1].replace("EOS", "DNC"), balance[0]);
                                }
                            }
                        }
                        getCallback().onResult(100, "导入成功");
                    }
                    else {
                        getCallback().onResult(1, "");
                    }
                    //LoadingProgress.dismissProgress();
                    if (balanceMap != null && !balanceMap.isEmpty()) {
                        isHttpError = false;
                    }
                    else {
                        isHttpError = true;
                    }
                }, e -> {
                    //LoadingProgress.dismissProgress();
                    if (e instanceof ConnectException || e instanceof UnknownHostException || e instanceof SocketTimeoutException || e instanceof SSLHandshakeException) {
                        ToastUtils.showShort("请检查网络连接");
                        getCallback().onResult(0, "");
                    } else {
                        if (getCallback() != null) {
                            getCallback().onResult(0, e.getMessage());
                        }
                    }
                    isHttpError = true;
                    //LogUtils.e(TAG, " isHttpError e: " + isHttpError );
                });
    }


}
