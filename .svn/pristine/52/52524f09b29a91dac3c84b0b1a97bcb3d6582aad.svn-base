package com.dnc.loc.vm;

import androidx.databinding.Bindable;

import com.blankj.utilcode.util.EncryptUtils;
import com.dnc.loc.BR;
import com.dnc.loc.blockchain.cypto.ec.EosPrivateKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class WalletVM extends WalletTransaction {

    // static final String TAG = "_TAG_WalletVM";

    // 修改...
    public String iv = "42C9B2AC14DF14BD7A90040683556907";
    
    public String userName;
    public String priKey;
    public String pubKey;
    // 每个账号设置不同背景
    public String background;
    // 余额
    //public HashMap<String, String> balanceMap;

//    private boolean hasPermission;


    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        notifyPropertyChanged(BR.userName);
    }

    public void randomUserName() {
        Random random = new Random();
//        String str = String.valueOf((char) (97 + random.nextInt(26)));
//        for (int i = 1; i < 11; i++) {
//            boolean b = random.nextBoolean();
//            if (b) {
//                str += (char) (97 + random.nextInt(26));
//            } else {
//                str += String.valueOf(1 + random.nextInt(5));
//            }
//        }

        String str = "dnc.";
        for (int i = 1; i < 9; i++) {
            boolean b = random.nextBoolean();
            if (b) {
                str += (char) (97 + random.nextInt(26));
            } else {
                str += String.valueOf(1 + random.nextInt(5));
            }
        }
        setUserName(str);
    }

    public void getPriPubKey() {
        EosPrivateKey ownerKey = getPrivateKey(1).get(0);
        priKey = ownerKey.toString();
        String strPubKey = ownerKey.getPublicKey().toString();
        pubKey = strPubKey.replace("EOS", "DNC");
    }

    private ArrayList<EosPrivateKey> getPrivateKey(int count) {
        ArrayList<EosPrivateKey> keys = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            keys.add(new EosPrivateKey());
        }
        return keys;
    }

    /**
     * 更新钱包余额
     *
     * @param update     是否联网
     * @return
     */
    public void checkBalance(Boolean update) {
        if (balanceMap == null ) {
            balanceMap = new HashMap<>();
            notifyServer(userName);
        }
        else if(update) {
            notifyServer(userName);
        }
        else {
            getCallback().onResult(100, "导入成功");
        }
    }


    private static String bytes2HexString(final byte[] bytes) {
        if (bytes == null) return "";
        int len = bytes.length;
        if (len <= 0) return "";
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = HEX_DIGITS[bytes[i] >> 4 & 0x0f];
            ret[j++] = HEX_DIGITS[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    //
    private static final char HEX_DIGITS[] =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static byte[] hexString2Bytes(String hexString) {
        if (isSpace(hexString)) return null;
        int len = hexString.length();
        if (len % 2 != 0) {
            hexString = "0" + hexString;
            len = len + 1;
        }
        char[] hexBytes = hexString.toUpperCase().toCharArray();
        byte[] ret = new byte[len >> 1];
        for (int i = 0; i < len; i += 2) {
            ret[i >> 1] = (byte) (hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
        }
        return ret;
    }

    private static int hex2Dec(final char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - '0';
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 保存钱包信息 (无用)
     * @param key     钱包密码
     * @return
     */
    public void keepPri(String key) {

        // 保存公钥
        String strPubKey = pubKey;
        strPubKey = strPubKey.replace("EOS", "DNC");
        //SPUtils.getInstance().put(SpConst.getSpCreateKey(SpConst.P_B_K), strPubKey, false);

        // 保存私钥
        String ppKey = EncryptUtils.encryptAES2HexString(priKey.getBytes(), EncryptUtils.encryptMD5ToString(key.getBytes()).getBytes(), "AES/CBC/PKCS5Padding", hexString2Bytes(iv));
        //SPUtils.getInstance().put(SpConst.getSpCreateKey(SpConst.P_K), ppKey, false);
//        String text = SPUtils.getInstance().getString(SpConst.P_K);

        // 保存钱包地址
        //SPUtils.getInstance().put(SpConst.getSpCreateKey(SpConst.WALLET_NAME), userName, false);

    }

}
