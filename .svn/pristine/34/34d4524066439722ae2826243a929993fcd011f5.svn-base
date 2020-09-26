package com.dnc.loc.constant;

import com.blankj.utilcode.util.SPUtils;

public class RouteConst {
    public static final String AV_SPLASH            = "/page/splash";
    public static final String AV_PATH_LOGIN        = "/page/login";

    public static final String AV_ACCOUNT_CREATE    = "/page/account_create";
    public static final String AV_ACCOUNT_MANAGE    = "/page/account_manage";
    public static final String AV_ACCOUNT_RECEIVE   = "/page/account_receive";
    public static final String AV_ACCOUNT_PAY       = "/page/account_pay";
    public static final String AV_ACCOUNT_IMPORT    = "/page/account_import";
    public static final String AV_ACCOUNT_SET       = "/page/account_set";

    public static final String AV_MAIN = "/page/index";
    public static final String AV_TRANSACTION = "/page/transaction";
    public static final String AV_ACCOUNT_SUB = "/page/account_sub";
    public static final String AV_TRANSACTION_RESULT = "/page/transaction_result";
    public static final String AV_TRANSACTION_RECORD = "/page/transaction_record"; //++
    public static final String AV_SCAN = "/page/scan";

    public static final String KEY_FM_PATH = "FM_PATH";

    public static final String FM_WALLET = "/wallet/index";

    public static final String FM_WALLET_KEY_COPY = "/wallet/key_copy";

    public static final String LINK = "url";


    //创建状态 (CREATE_STATE):
    public static final int STATE_INIT    = 0;    // 创建状态--未创建
    public static final int STATE_CREATE_OK  = 1; // 创建状态--已创建未激活
    public static final int STATE_PUSH_OK  = 2;   // 创建状态--已激活
    public static final int STATE_LOGIN_OK  = 3;  // 登录状态
    public static final int STATE_SIGN_OUT  = 4;  // 退出登录

    public static final String TOKEN_NAME = "TOKEN_NAME";//代币名

    public static final String WALLET_KEY = "WALLET_KEY";
    public static final String TITLE = "TITLE";
    public static final String SUB_TITLE = "SUB_TITLE";
    
    public static final String WALLET_USER_NAME     = "user_name";   // ++
    public static final String WALLET_PUB_KEY       = "pub_key";   // ++

    // 创建或登录状态
    public static final String USER_STATE  = "USER_STATE";

    public static int checkUserStatus(Boolean isLogin) {
        int loginState = SPUtils.getInstance().getInt(SpConst.LOGIN_STATE, RouteConst.STATE_INIT);
        //1.检查登录，返回登录或默认0
        //  登录状态
        if (isLogin && loginState == STATE_LOGIN_OK) {  //是否登录
            return STATE_LOGIN_OK;
        }
        //2.不检查登录，返回创建状态: 未创建，未激活，已激活
        //  创建状态
        else {
            int createState = SPUtils.getInstance().getInt(SpConst.CREATE_STATE, RouteConst.STATE_INIT);
            return createState;
        }
    }

    public static void setUserStatus(int state) {
        if (state == STATE_SIGN_OUT) {  //退出登录
            //登录状态: 登录或默认0
            SPUtils.getInstance().put(SpConst.LOGIN_STATE, STATE_INIT, false);
            //创建状态: 未创建，未激活，已激活
            //SPUtils.getInstance().put(SpConst.CREATE_STATE, STATE_INIT, false);
        }
        else if (state == STATE_LOGIN_OK) {  //是否登录
            SPUtils.getInstance().put(SpConst.LOGIN_STATE, STATE_LOGIN_OK, false);
            SPUtils.getInstance().put(SpConst.CREATE_STATE, STATE_INIT, false);
        } else {
            SPUtils.getInstance().put(SpConst.CREATE_STATE, state, false);
            // 当前只使用前2种:
            //SPUtils.getInstance().put(SpConst.CREATE_STATE, STATE_INIT, false);
            //SPUtils.getInstance().put(SpConst.CREATE_STATE, STATE_CREATE_OK, false);
            //SPUtils.getInstance().put(SpConst.CREATE_STATE, STATE_PUSH_OK, false);
        }
    }
}
