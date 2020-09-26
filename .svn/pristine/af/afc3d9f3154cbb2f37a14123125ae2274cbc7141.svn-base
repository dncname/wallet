package com.dnc.loc.data.eos.model;

import android.text.TextUtils;

import java.util.List;

public class ChainRecord {
    public String total;
    public List<RecordItem> rows;

    public class RecordItem {
        public String from;
        public String to;
        public String quantity;
        public String memo;
        public String block_time;
        public String fee;
        public String tx;

        public boolean isIncome(String user) {
            return TextUtils.equals(user, to);
        }
    }


//    @Override
//    public String toString() {
//        return "ChainInfo{" +
//                "timestamp='" + timestamp + '\'' +
//                ", block_num=" + block_num +
//                ", ref_block_prefix=" + ref_block_prefix +
//                '}';
//    }
}

