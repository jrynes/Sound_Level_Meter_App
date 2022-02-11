package com.nextradioapp.nextradio;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class BusProvider {
    private static final Bus BUS;

    static {
        BUS = new Bus(ThreadEnforcer.ANY);
    }

    public static Bus getInstance1() {
        return BUS;
    }

    private BusProvider() {
    }
}
