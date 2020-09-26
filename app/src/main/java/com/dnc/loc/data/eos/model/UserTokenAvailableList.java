package com.dnc.loc.data.eos.model;

import java.util.List;

public class UserTokenAvailableList {
    public List<TokenAvailable> rows;
    public boolean more;

    public class TokenAvailable {
        public String balance;
    }
}
