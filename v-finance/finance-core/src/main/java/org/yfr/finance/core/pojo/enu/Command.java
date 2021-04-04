package org.yfr.finance.core.pojo.enu;

public enum Command {

    HEART_BEAT,

    DUMP_MIN_K,

    DUMP_DAY_K,

    ORDER;

    public String build() {
        return toString() + "\n";
    }

}
