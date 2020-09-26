package com.dnc.loc.data.model;

import java.io.Serializable;
import java.util.List;

public class ResultPageInfo implements Serializable {

    public static final long serialVersionUID = 1L;

    public List<ResultInfo> infos;

    public static class ResultInfo implements Serializable {
        public static final long serialVersionUID = 1L;
        public String key;
        public String value;
        public boolean isExp;
        public boolean needCopy;

        public ResultInfo(String key, String value, boolean isExp, boolean needCopy) {
            this.key = key;
            this.value = value;
            this.isExp = isExp;
            this.needCopy = needCopy;
        }

        public ResultInfo(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public boolean isShowBtn(){
            return needCopy || isExp;
        }
    }

}