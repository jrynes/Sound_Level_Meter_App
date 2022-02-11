package com.google.android.gms.analytics.internal;

import android.util.Log;
import com.google.android.gms.analytics.Logger;

class zzs implements Logger {
    private boolean zzPk;
    private int zzRB;

    zzs() {
        this.zzRB = 2;
    }

    public void error(Exception exception) {
    }

    public void error(String msg) {
    }

    public int getLogLevel() {
        return this.zzRB;
    }

    public void info(String msg) {
    }

    public void setLogLevel(int level) {
        this.zzRB = level;
        if (!this.zzPk) {
            Log.i((String) zzy.zzRL.get(), "Logger is deprecated. To enable debug logging, please run:\nadb shell setprop log.tag." + ((String) zzy.zzRL.get()) + " DEBUG");
            this.zzPk = true;
        }
    }

    public void verbose(String msg) {
    }

    public void warn(String msg) {
    }
}
