package com.amazon.device.associates;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import java.util.Date;

/* compiled from: AbstractCacheAgentTask */
abstract class ap<T extends bl> extends AsyncTask<ap<T>, Void, T> {
    protected T f1186a;
    private boolean f1187b;
    private boolean f1188c;
    private final long f1189d;
    private final String f1190e;

    protected abstract T m769b();

    protected /* bridge */ /* synthetic */ void onPostExecute(Object obj) {
        m767a((bl) obj);
    }

    public ap(String str, long j) {
        this.f1187b = false;
        this.f1188c = false;
        this.f1190e = str;
        this.f1189d = j;
    }

    protected synchronized boolean m772d() {
        return this.f1187b;
    }

    protected synchronized void m768a(boolean z) {
        this.f1187b = z;
    }

    protected synchronized boolean m773e() {
        return this.f1188c;
    }

    protected synchronized void m770b(boolean z) {
        this.f1188c = z;
    }

    protected long m774f() {
        return this.f1189d;
    }

    public String m771c() {
        return this.f1190e;
    }

    protected String m775g() {
        return bp.f1276b + m771c();
    }

    protected synchronized void m776h() {
        try {
            if (getStatus() == Status.PENDING) {
                Log.m1018c("AbstractCacheAgentLog", "Task has still not started, starting it.");
                execute(new ap[]{this});
            } else if (getStatus() == Status.FINISHED) {
                Log.m1018c("AbstractCacheAgentLog", "Task might have failed, creating new task");
                ((ap) getClass().newInstance()).execute(new ap[]{this});
            }
        } catch (Exception e) {
            Log.m1017b("Service Failure", "Exception in MobileAssociateService: " + getClass(), e);
        }
    }

    protected synchronized long m777i() {
        long f;
        if (this.f1186a == null || this.f1186a.m893d() == -1 || this.f1186a.m893d() <= 0) {
            Log.m1013a("AbstractCacheAgentLog", "Returning the SDK hardcoded cacheRefreshRate:" + m774f());
            f = m774f();
        } else {
            Log.m1013a("AbstractCacheAgentLog", "Returning the cacheRefreshRate:" + this.f1186a.m893d());
            f = this.f1186a.m893d();
        }
        return f;
    }

    protected synchronized T m778j() {
        T t;
        long i;
        long currentTimeMillis = System.currentTimeMillis();
        if (m772d() && this.f1186a != null) {
            i = m777i();
            if (currentTimeMillis - this.f1186a.m891c() < i) {
                Log.m1018c("AbstractCacheAgentLog", "Cache refresh not needed! LapseTime:" + (currentTimeMillis - this.f1186a.m891c()) + " refreshRate:" + i);
                t = this.f1186a;
            }
        }
        Log.m1018c("AbstractCacheAgentLog", "Checking to load from Cache");
        this.f1186a = (bl) ab.m695a(m775g());
        if (this.f1186a != null) {
            i = m777i();
            Log.m1018c("AbstractCacheAgentLog", "Cache loaded from local file...need to see if refresh is needed for not!");
            if (currentTimeMillis - this.f1186a.m891c() < i) {
                Log.m1018c("AbstractCacheAgentLog", "Cache refresh not needed! LapseTime:" + (currentTimeMillis - this.f1186a.m891c()) + " refreshRate:" + i);
                m768a(true);
                t = this.f1186a;
            } else {
                Log.m1018c("AbstractCacheAgentLog", "Cache refresh needed as lapse time:" + (currentTimeMillis - this.f1186a.m891c()) + " Refresh-Rate:" + i);
            }
        } else {
            Log.m1018c("AbstractCacheAgentLog", "Load Cache failed and got a null object. Hence deleting the local copy");
            ab.m699b(m775g());
        }
        m776h();
        if (this.f1186a == null) {
            this.f1186a = m769b();
        }
        t = this.f1186a;
        return t;
    }

    protected void m767a(T t) {
        if (m773e()) {
            try {
                Log.m1018c("AbstractCacheAgentLog", "Writing Templates to cache bucket at " + bp.f1275a.format(new Date()) + ". Path=" + m775g());
                if (ab.m697a(t, m775g())) {
                    Log.m1018c("AbstractCacheAgentLog", "Wrote Templates to xml at " + bp.f1275a.format(new Date()) + ". Path=" + m775g());
                } else {
                    Log.m1018c("AbstractCacheAgentLog", "Wrote Templates to xml at " + bp.f1275a.format(new Date()) + ". Path=" + m775g() + " failed.");
                }
            } catch (Exception e) {
                Log.m1019c("AbstractCacheAgentLog", "Writing Templates to local cache failed. Ex=", e);
            }
        }
    }
}
