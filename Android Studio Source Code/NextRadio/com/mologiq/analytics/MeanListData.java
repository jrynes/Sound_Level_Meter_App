package com.mologiq.analytics;

import java.util.List;

/* renamed from: com.mologiq.analytics.i */
final class MeanListData {
    private static final MeanListData f2146a;
    private MeanListData f2147b;

    /* renamed from: com.mologiq.analytics.i.a */
    class MeanListData {
        final /* synthetic */ MeanListData f2138a;
        private int f2139b;
        private int f2140c;
        private List<Integer> f2141d;

        MeanListData(MeanListData meanListData) {
            this.f2138a = meanListData;
        }

        final int m1753a() {
            return this.f2139b;
        }

        final void m1754a(int i) {
            this.f2139b = i;
        }

        final void m1755a(List<Integer> list) {
            this.f2141d = list;
        }

        final int m1756b() {
            return this.f2140c;
        }

        final void m1757b(int i) {
            this.f2140c = i;
        }

        final List<Integer> m1758c() {
            return this.f2141d;
        }
    }

    /* renamed from: com.mologiq.analytics.i.b */
    class MeanListData {
        final /* synthetic */ MeanListData f2142a;
        private String f2143b;
        private int f2144c;
        private List<MeanListData> f2145d;

        MeanListData(MeanListData meanListData) {
            this.f2142a = meanListData;
        }

        final String m1759a() {
            return this.f2143b;
        }

        final void m1760a(int i) {
            this.f2144c = i;
        }

        final void m1761a(String str) {
            this.f2143b = str;
        }

        final void m1762a(List<MeanListData> list) {
            this.f2145d = list;
        }

        final int m1763b() {
            return this.f2144c;
        }

        final List<MeanListData> m1764c() {
            return this.f2145d;
        }
    }

    static {
        f2146a = new MeanListData();
    }

    MeanListData() {
    }

    static MeanListData m1765a() {
        return f2146a;
    }

    final MeanListData m1766b() {
        return this.f2147b;
    }
}
