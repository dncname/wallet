package com.dnc.loc.data.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WalletBalance implements Serializable {

    //private static final String TAG = "_TAG_WalletBalance";

    public static final long serialVersionUID = 1L;

    public List<WalletBalance.BalanceInfo> infos;

    public static class BalanceInfo implements Serializable {
        public static final long serialVersionUID = 1L;
        public String balance;
        public String currency;
        public String userName;

        public BalanceInfo(String balance, String currency, String userName) {
            this.balance = balance;
            this.currency = currency;
            this.userName = userName;
        }
    }

    //  添加记录
    public void appendBalance(HashMap<String, String> map, String userName) {
        infos = new ArrayList<>();
        //  传 null 初始化 infos
        if(map == null)
            return;

        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String balance = entry.getKey();
            String currency = entry.getValue();
            //LogUtils.e(TAG, " 0 " + balance + " 1 " + currency);
            BalanceInfo info = new BalanceInfo(balance, currency, userName);
            infos.add(info);
        }

    }

}

