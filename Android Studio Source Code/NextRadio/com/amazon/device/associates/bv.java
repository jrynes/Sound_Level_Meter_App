package com.amazon.device.associates;

import org.apache.activemq.transport.stomp.Stomp;

/* compiled from: AsyncGetUDPPopoverJSTask */
public class bv extends ap<bu> {
    private static final String f1298b;
    private final String f1299c;

    protected /* bridge */ /* synthetic */ bl m929b() {
        return m927a();
    }

    public /* bridge */ /* synthetic */ String m930c() {
        return super.m771c();
    }

    protected /* bridge */ /* synthetic */ Object doInBackground(Object[] objArr) {
        return m928a((ap[]) objArr);
    }

    static {
        f1298b = bv.class.getName();
    }

    public bv() {
        super("udpjs.db", 1000);
        this.f1299c = "UdpPopoverJS";
    }

    protected bu m928a(ap<bu>... apVarArr) {
        ap apVar = apVarArr[0];
        try {
            ac acVar = new ac("ATVPDKIKX0DER", "UdpPopoverJS", Stomp.V1_0);
            acVar.m705e();
            String d = acVar.m709d();
            bl buVar = new bu();
            if (d != null) {
                buVar.m925b(d);
                apVar.m770b(true);
                apVar.m768a(true);
            }
            apVar.f1186a = buVar;
        } catch (Exception e) {
            Log.m1019c(f1298b, "Service call threw exception.", e);
        }
        return (bu) apVar.f1186a;
    }

    protected bu m927a() {
        return null;
    }
}
