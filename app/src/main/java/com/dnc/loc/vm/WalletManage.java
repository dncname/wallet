package com.dnc.loc.vm;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dnc.loc.base.BaseContract;
import com.dnc.loc.constant.RouteConst;
import com.dnc.loc.constant.SpConst;
import com.dnc.loc.data.http.EosHttp;
import com.dnc.loc.utils.RxUtils;
import com.zlw.base.ui.vm.BaseViewModel;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.net.ssl.SSLHandshakeException;

public class WalletManage extends BaseViewModel {

    // 用单例模式，以便切换账号时少操作数据
    public List<WalletVM> datas;

    public int walletCnt;
    public int currentIndex;


    //单例模式
    private static class Holder{
        private static WalletManage instance = new WalletManage();
    }
 
    //单例模式
    public static WalletManage getInstance(){
        // 初始化列表
        WalletVM  wallet = Holder.instance.getCurrentWallet();
        return Holder.instance;
    }
    
    // 初始化，并读入缓存
    public void initWallet() {
        // 初始化
        datas = new ArrayList<>();

        Set<String> set = new HashSet<>();

        // 读入缓存，实际不能超过5个
        walletCnt = SPUtils.getInstance().getInt(SpConst.WALLET_COUNT, 0);
        for(int i=0; i<walletCnt; i++) {
            WalletVM wallet = new WalletVM();
            // 钱包地址
            wallet.userName = SPUtils.getInstance().getString(SpConst.getSpWalletKey(i, SpConst.WALLET_NAME), "None");
            // 公钥
            wallet.pubKey = SPUtils.getInstance().getString(SpConst.getSpWalletKey(i, SpConst.P_B_K), "None");
            // 私钥(加密)
            wallet.priKey = SPUtils.getInstance().getString(SpConst.getSpWalletKey(i, SpConst.P_K), "None");
            // 下标
            wallet.index = i;

            // 不添加重复（方便测试）
            if(!set.contains(wallet.userName)) {
                set.add(wallet.userName);
                datas.add(wallet);
            }
        }

        // 当前钱包ID
        currentIndex = SPUtils.getInstance().getInt(SpConst.WALLET_ID, 0);
    }

    // 检查重复
//    public Boolean containsWallet2(String userName) {
//        Set<String> set = new HashSet<>();
//
//        for(int i=0; i<datas.size(); i++) {
//            WalletVM wallet = datas.get(i);
//            set.add(wallet.userName);
//        }
//        return set.contains(userName);
//    }

    // 检查重复
    public Boolean containsWallet(String userName) {
        if(walletCnt == 0) {
            initWallet();
        }

        for(int i=0; i<datas.size(); i++) {
            String tmpAccount = datas.get(i).userName;
            if (!TextUtils.isEmpty(tmpAccount) && tmpAccount.equals(userName)) {
                return true;
            }
        }
        return false;
    }

    // 创建、导入钱包
    public void addWallet(String userName, String priKey, String pubKey, String pswKey) {
        if(walletCnt == 0) {
            initWallet();
        }
        if(!containsWallet(userName)) {
            WalletVM wallet = new WalletVM();
            wallet.userName = userName;
            wallet.pubKey   = pubKey;
            // 私钥加密，不能为空，否则不能导出备份
            String ppKey = EncryptUtils.encryptAES2HexString(priKey.getBytes(),
                    EncryptUtils.encryptMD5ToString(pswKey.getBytes()).getBytes(),
                    "AES/CBC/PKCS5Padding",
                    WalletVM.hexString2Bytes(wallet.iv));
            wallet.priKey = ppKey;
            wallet.index = walletCnt;
            datas.add(wallet);

            // 保存钱包地址
            SPUtils.getInstance().put(SpConst.getSpWalletKey(walletCnt, SpConst.WALLET_NAME), userName, false);

            // 保存公钥
            String strPubKey = pubKey;
            strPubKey = strPubKey.replace("EOS", "DNC");
            SPUtils.getInstance().put(SpConst.getSpWalletKey(walletCnt, SpConst.P_B_K), strPubKey, false);

            // 保存私钥(加密)
            SPUtils.getInstance().put(SpConst.getSpWalletKey(walletCnt, SpConst.P_K), ppKey, false);

            walletCnt = datas.size();
            SPUtils.getInstance().put(SpConst.WALLET_COUNT, walletCnt, false);
        }
    }

    // 删除钱包
    public void removeWallet(WalletVM wallet) {
        if(walletCnt == 0) {
            initWallet();
        }

        int deleteId = wallet.index;

        // 删除钱包地址(用户名)
        SPUtils.getInstance().remove(SpConst.getSpWalletKey(deleteId, SpConst.WALLET_NAME));
        // 删除公钥
        SPUtils.getInstance().remove(SpConst.getSpWalletKey(deleteId, SpConst.P_B_K));
        // 删除私钥(加密)
        SPUtils.getInstance().remove(SpConst.getSpWalletKey(deleteId, SpConst.P_K));

        if(deleteId < datas.size() && datas.get(deleteId).userName.equals(wallet.userName)) {
            datas.remove(deleteId);
        }
        else {
            Iterator<WalletVM> iterator = datas.iterator();
            while (iterator.hasNext()) {
                WalletVM wa = iterator.next();
                if (wa.userName.equals(wallet.userName))
                    iterator.remove();
            }
//            datas.removeIf(
//                    wa -> wa.userName.equals(wallet.userName)
//            );
        }
        // update Id
        walletCnt = datas.size();
        SPUtils.getInstance().put(SpConst.WALLET_COUNT, walletCnt, false);

        // update sp
        int newIndex = -1;
        for(int i=0; i<walletCnt; i++) {
            WalletVM wa = datas.get(i);
            // 更新ID, 比如从列表1-2-4-5中设置当前4
            if(currentIndex == wa.index) {
                newIndex = i;
            }
            wa.index = i;
            // 钱包地址
            SPUtils.getInstance().put(SpConst.getSpWalletKey(i, SpConst.WALLET_NAME), wa.userName, false);
            // 公钥
            SPUtils.getInstance().put(SpConst.getSpWalletKey(i, SpConst.P_B_K), wa.pubKey, false);
            // 私钥(加密)
            SPUtils.getInstance().put(SpConst.getSpWalletKey(i, SpConst.P_K), wa.priKey, false);
        }
        if(-1 == newIndex) {
            // 即删除当前钱包
            currentIndex = 0;
        }
        else {
            currentIndex = newIndex;
        }
        SPUtils.getInstance().put(SpConst.WALLET_ID, currentIndex);
    }

    // 获取当前钱包
    public WalletVM getCurrentWallet() {
        if(walletCnt == 0) {
            initWallet();
        }

        // 钱包信息，包括钱包地址、公钥
        //   私钥另外读取
        if(currentIndex < datas.size()) {
            return datas.get(currentIndex);
        }
        else {
            WalletVM  wallet = new WalletVM();
            wallet.getPriPubKey();
            wallet.setUserName("");
            return wallet;
        }
    }

    // 设置当前钱包
    public Boolean setCurrentWallet(String userName) {
        for(int i=0; i<datas.size(); i++) {
            String tmpAccount = datas.get(i).userName;
            if (!TextUtils.isEmpty(tmpAccount) && tmpAccount.equals(userName)) {
                currentIndex = i;
                SPUtils.getInstance().put(SpConst.WALLET_ID, currentIndex);
            }
        }
        return false;
    }

    // 检查登录(公钥对应的用户名可能不同)
    public void checkLoginStatus(BaseContract.Callback callback) {
        if (RouteConst.checkUserStatus(false) == RouteConst.STATE_INIT) {
            if(callback != null) {
                callback.onback(false, null);
            }
            return;
        }

        WalletVM viewModel = new WalletVM();
        // 公钥
        viewModel.pubKey = SPUtils.getInstance().getString(SpConst.getSpCreateKey(SpConst.P_B_K), "None");

        if(!viewModel.pubKey.isEmpty()) {
            getKeyAccounts(viewModel.pubKey, new BaseContract.Callback() {
                @Override
                public void onback(boolean isSuccess, Object o) {
                    if(isSuccess) {
                        // 私钥(未加密的私钥)
                        viewModel.priKey = SPUtils.getInstance().getString(SpConst.getSpCreateKey(SpConst.P_K), "None");
                        // 用户
                        viewModel.userName = SPUtils.getInstance().getString(SpConst.getSpCreateKey(SpConst.WALLET_NAME), "");

                        // 账户已创建
                        String name = o.toString();
                        if(!viewModel.userName.equals(name)) {
                            viewModel.userName = name;
                        }

                        // 密码
                        String psw = SPUtils.getInstance().getString(SpConst.getSpCreateKey(SpConst.PSW), "");

                        //保存钱包信息，加密保存priKey
                        WalletManage.getInstance().addWallet(viewModel.userName, viewModel.priKey, viewModel.pubKey, psw);
                        // 记住状态
                        RouteConst.setUserStatus(RouteConst.STATE_LOGIN_OK);
                        // clear create tmp data, not import
                        SPUtils.getInstance().remove(SpConst.getSpCreateKey(SpConst.P_K));
                        SPUtils.getInstance().remove(SpConst.getSpCreateKey(SpConst.P_B_K));
                        SPUtils.getInstance().remove(SpConst.getSpCreateKey(SpConst.WALLET_NAME));
                        SPUtils.getInstance().remove(SpConst.getSpCreateKey(SpConst.PSW));

                        if(callback != null) {
                            callback.onback(true, name);
                        }
                    }
                    else {
                        if(callback != null) {
                            callback.onback(false, null);
                        }
                    }
                }
            });
        }
    }

    /**
     * 检查公钥，是否有用户名，用户名可能有多个
     * @param _pubKey 账号
     * @return
     */
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


