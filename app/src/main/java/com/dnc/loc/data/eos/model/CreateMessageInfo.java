package com.dnc.loc.data.eos.model;

import com.dnc.loc.data.model.Args;

public class CreateMessageInfo {
    public String code = "eosio";
    public String action = "newaccount";
    public Args args = null;

    public CreateMessageInfo() {
    }

    @Override
    public String toString() {
        return "GetBinForCreateAccount{" +
                "code='" + code + '\'' +
                ", action='" + action + '\'' +
                ", args=" + args +
                '}';
    }
}
