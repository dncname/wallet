package com.dnc.loc.ui.aty.accountpay;

import com.dnc.loc.base.BaseContract;
import com.dnc.loc.base.BaseContract.IBaseView;
import com.dnc.loc.base.BaseContract.IBasePresenter;

public interface AccountPayContract {
    interface View extends IBaseView , BaseContract.Callback {
        void checkAccountPub(String newUserPubKey, BaseContract.Callback callback);
    }

    interface Presenter extends IBasePresenter<View> {
        void getKeyAccounts(String pubKey, BaseContract.Callback callback);
    }

//    interface Model {
//        void create(String newUserPubKey, String newUserName, BaseContract.Callback callback);
//    }
}

