package com.dnc.loc.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseUserInfo {
    public String authToken;
    public String walletName;

    @SerializedName("account_names")
    public List<String> accountNames;

    @Override
    public String toString() {
        return "BaseUserInfo{" +
                "authToken='" + authToken + '\'' +
                ", walletName='" + walletName + '\'' +
                ", accountNames=" + accountNames +
                '}';
    }
}
