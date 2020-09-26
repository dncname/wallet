package com.dnc.loc.ui.fm.create;

import com.dnc.loc.base.BaseContract;
import com.dnc.loc.base.BaseContract.IBaseView;
import com.dnc.loc.base.BaseContract.IBasePresenter;

public interface WalletContract {
    interface View extends IBaseView , BaseContract.Callback {
        void checkNewAccount(String createPubKey, String createUserName);
        void showDialog(String newUserPubKey, String newUserName);
        void showFeeDialog(String newUserPubKey, String newUserName, String fee);
    }

    interface Presenter extends IBasePresenter<View> {
        void getNewKeyAccounts(String createPubKey, String createUserName);
        void createAccount(String priKey, String userName, String newUserPubKey, String newUserName, BaseContract.Callback callback);
        void createFee(String priKey, String userName, String newUserPubKey, String newUserName, BaseContract.Callback callback);
    }

    interface Model {
        void create(String newUserPubKey, String newUserName, Boolean isGetFee, BaseContract.Callback callback);
    }
}

