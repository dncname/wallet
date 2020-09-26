package com.dnc.loc.constant;

import java.util.Locale;

public class SpConst {

    public static final String UID = "UID";

    //创建状态:
    public static final String LOGIN_STATE  = "LOGIN_STATE";

    //创建状态:
    public static final String CREATE_STATE  = "CREATE_STATE";

    //钱包数量，最多导入5个
    public static final String WALLET_COUNT  = "WALLET_COUNT";

    //当前钱包下标
    public static final String WALLET_ID     = "WALLET_ID";

    //钱包地址
    public static final String WALLET_NAME = "WALLET_NAME";

    //私钥
    public static final String P_K = "P_K";

    //公钥
    public static final String P_B_K = "P_B_K";

    //密码
    public static final String PSW = "PSW";

    //创建信息
    public static String getSpCreateKey(String key){
        return String.format(Locale.getDefault(), "Create_%s", key);
    }
    
    //导入钱包信息，编号0~4
    public static String getSpWalletKey(int id, String key){
        return String.format(Locale.getDefault(), "Wallet_%d_%s", id, key);
    }
}
