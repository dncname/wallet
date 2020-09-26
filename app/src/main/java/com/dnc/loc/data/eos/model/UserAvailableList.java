package com.dnc.loc.data.eos.model;

import java.util.List;

public class UserAvailableList {
    public List<UserAvailable> rows;
    public boolean more;

    public class UserAvailable {
        public String name;
        public String available;
    }
}
