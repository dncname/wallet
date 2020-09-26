package com.dnc.loc.data.model;

import java.util.List;

public class Args {
    public String creator;
    public String name;
    public Owner owner;
    public Active active;

    public static class Owner {
        public int threshold = 1;
        public List<Keys> keys;
        public List<String> accounts;
        public List<String> waits;

        @Override
        public String toString() {
            return "Active{" +
                    "threshold=" + threshold +
                    ", keys=" + keys +
                    ", accounts=" + accounts +
                    ", waits=" + waits +
                    '}';
        }
    }

    public static class Active {
        public int threshold = 1;
        public List<Keys> keys;
        public List<String> accounts;
        public List<String> waits;

        @Override
        public String toString() {
            return "Active{" +
                    "threshold=" + threshold +
                    ", keys=" + keys +
                    ", accounts=" + accounts +
                    ", waits=" + waits +
                    '}';
        }

//        public Active setKeys(List<Keys> keys) {
//            this.keys = keys;
//            return this;
//        }
    }

    public Args setCreator(String creator) {
        this.creator = creator;
        return this;
    }

    public String getCreator() {
        return creator;
    }

    public Args setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public Args setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    public Owner getOwner() {
        return owner;
    }

    public Args setActive(Active active) {
        this.active = active;
        return this;
    }

    public Active getActive() {
        return active;
    }

    @Override
    public String toString() {
        return "Args{" +
                "creator='" + creator + '\'' +
                ", name='" + name + '\'' +
                ", owner=" + owner +
                ", active=" + active +
                '}';
    }


}
