package com.amazon.device.associates;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import com.admarvel.android.ads.Constants;
import com.amazon.device.associates.PurchaseResponse.Status;
import com.amazon.device.associates.bb.CommandDispatcher;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

/* renamed from: com.amazon.device.associates.o */
class ShoppingServiceImpl implements ShoppingService, bb {
    private static final String f1361a;
    private String f1362b;
    private Context f1363c;
    private Handler f1364d;
    private ShoppingListener f1365e;
    private Map<String, ShoppingServiceRequest> f1366f;

    /* renamed from: com.amazon.device.associates.o.1 */
    static /* synthetic */ class ShoppingServiceImpl {
        static final /* synthetic */ int[] f1357a;

        static {
            f1357a = new int[CommandDispatcher.values().length];
            try {
                f1357a[CommandDispatcher.GET_SERVICE_STATUS.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1357a[CommandDispatcher.GET_RECEIPTS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f1357a[CommandDispatcher.SEARCH.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f1357a[CommandDispatcher.SEARCH_BY_ID.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f1357a[CommandDispatcher.PURCHASE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f1357a[CommandDispatcher.GET_PURCHASE_RESULT.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                f1357a[CommandDispatcher.NOTIFY_RECEIPT_RECEIVED.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    /* renamed from: com.amazon.device.associates.o.2 */
    class ShoppingServiceImpl implements Runnable {
        final /* synthetic */ ShoppingServiceResponse f1358a;
        final /* synthetic */ CommandDispatcher f1359b;
        final /* synthetic */ ShoppingServiceImpl f1360c;

        ShoppingServiceImpl(ShoppingServiceImpl shoppingServiceImpl, ShoppingServiceResponse shoppingServiceResponse, CommandDispatcher commandDispatcher) {
            this.f1360c = shoppingServiceImpl;
            this.f1358a = shoppingServiceResponse;
            this.f1359b = commandDispatcher;
        }

        public void run() {
            try {
                boolean z;
                aw b;
                Object obj;
                RequestId requestId = this.f1358a.getRequestId();
                ShoppingServiceRequest shoppingServiceRequest = (ShoppingServiceRequest) this.f1360c.f1366f.remove(requestId.toString());
                boolean a = this.f1358a.m664a();
                if (shoppingServiceRequest != null && (shoppingServiceRequest instanceof PurchaseRequest)) {
                    PurchaseResponse purchaseResponse = (PurchaseResponse) this.f1358a;
                    if (((PurchaseRequest) shoppingServiceRequest).getPurchaseExperience() == null && purchaseResponse.getStatus() == Status.INVALID_ID) {
                        z = true;
                        b = az.m821b();
                        if (!(shoppingServiceRequest == null || !r2 || b == null)) {
                            switch (ShoppingServiceImpl.f1357a[this.f1359b.ordinal()]) {
                                case Zone.SECONDARY /*2*/:
                                case Protocol.GGP /*3*/:
                                case Type.MF /*4*/:
                                case Service.RJE /*5*/:
                                    this.f1360c.m984a(this.f1359b, requestId, shoppingServiceRequest, b);
                                    obj = null;
                                    break;
                            }
                        }
                        obj = 1;
                        if (obj != null) {
                            this.f1360c.m987a(this.f1359b, this.f1358a);
                        }
                    }
                }
                z = a;
                b = az.m821b();
                switch (ShoppingServiceImpl.f1357a[this.f1359b.ordinal()]) {
                    case Zone.SECONDARY /*2*/:
                    case Protocol.GGP /*3*/:
                    case Type.MF /*4*/:
                    case Service.RJE /*5*/:
                        this.f1360c.m984a(this.f1359b, requestId, shoppingServiceRequest, b);
                        obj = null;
                        break;
                }
                obj = 1;
                if (obj != null) {
                    this.f1360c.m987a(this.f1359b, this.f1358a);
                }
            } catch (Exception e) {
                aa.m691a(ShoppingServiceImpl.f1361a, "Error in sendResponse: " + e);
            }
        }
    }

    static {
        f1361a = ShoppingServiceImpl.class.getName();
    }

    protected ShoppingServiceImpl(Context context, boolean z) {
        this.f1362b = null;
        this.f1363c = null;
        this.f1364d = null;
        this.f1365e = null;
        this.f1366f = new ConcurrentHashMap();
        this.f1362b = "1.0.64.0";
        this.f1363c = context;
        az.m820a(z);
    }

    private void m990d() throws NoListenerException {
        if (this.f1365e == null) {
            throw new NoListenerException();
        }
    }

    public void setListener(ShoppingListener shoppingListener) {
        ar.m782a((Object) shoppingListener, "listener");
        this.f1365e = shoppingListener;
    }

    public RequestId getServiceStatus() throws NoListenerException {
        m990d();
        return m991a(CommandDispatcher.GET_SERVICE_STATUS, new RequestId(), null);
    }

    public RequestId getReceipts(ReceiptsRequest receiptsRequest) throws NoListenerException {
        ar.m782a((Object) receiptsRequest, Constants.AD_REQUEST);
        m990d();
        return m991a(CommandDispatcher.GET_RECEIPTS, new RequestId(), (ShoppingServiceRequest) receiptsRequest);
    }

    public RequestId search(SearchRequest searchRequest) throws NoListenerException {
        ar.m782a((Object) searchRequest, Constants.AD_REQUEST);
        m990d();
        return m991a(CommandDispatcher.SEARCH, new RequestId(), (ShoppingServiceRequest) searchRequest);
    }

    public RequestId searchById(SearchByIdRequest searchByIdRequest) throws NoListenerException {
        ar.m782a((Object) searchByIdRequest, Constants.AD_REQUEST);
        m990d();
        return m991a(CommandDispatcher.SEARCH_BY_ID, new RequestId(), (ShoppingServiceRequest) searchByIdRequest);
    }

    public RequestId purchase(PurchaseRequest purchaseRequest) throws NoListenerException {
        ar.m782a((Object) purchaseRequest, Constants.AD_REQUEST);
        m990d();
        return m991a(CommandDispatcher.PURCHASE, new RequestId(), (ShoppingServiceRequest) purchaseRequest);
    }

    public String m992a() {
        return this.f1362b;
    }

    public Context m995b() {
        return this.f1363c;
    }

    public RequestId m991a(CommandDispatcher commandDispatcher, RequestId requestId, ShoppingServiceRequest shoppingServiceRequest) {
        if (shoppingServiceRequest != null) {
            this.f1366f.put(requestId.toString(), shoppingServiceRequest);
        }
        return m984a(commandDispatcher, requestId, shoppingServiceRequest, az.m818a());
    }

    private RequestId m984a(CommandDispatcher commandDispatcher, RequestId requestId, ShoppingServiceRequest shoppingServiceRequest, aw awVar) {
        switch (ShoppingServiceImpl.f1357a[commandDispatcher.ordinal()]) {
            case Zone.PRIMARY /*1*/:
                awVar.m734a(requestId, this);
                break;
            case Zone.SECONDARY /*2*/:
                awVar.m731a(requestId, (ReceiptsRequest) shoppingServiceRequest, (bb) this);
                break;
            case Protocol.GGP /*3*/:
                awVar.m733a(requestId, (SearchRequest) shoppingServiceRequest, (bb) this);
                break;
            case Type.MF /*4*/:
                awVar.m732a(requestId, (SearchByIdRequest) shoppingServiceRequest, (bb) this);
                break;
            case Service.RJE /*5*/:
                awVar.m730a(requestId, (PurchaseRequest) shoppingServiceRequest, (bb) this);
                break;
            case Protocol.TCP /*6*/:
                awVar.m735b(requestId, this);
                break;
            case Service.ECHO /*7*/:
                awVar.m736c(requestId, this);
                break;
        }
        return requestId;
    }

    public void m994a(CommandDispatcher commandDispatcher, ShoppingServiceResponse shoppingServiceResponse) {
        this.f1364d = bf.m871a();
        this.f1364d.post(new ShoppingServiceImpl(this, shoppingServiceResponse, commandDispatcher));
    }

    private void m987a(CommandDispatcher commandDispatcher, Object obj) {
        if (this.f1365e == null) {
            aa.m693b(f1361a, "No listener has been registered.");
            return;
        }
        try {
            switch (ShoppingServiceImpl.f1357a[commandDispatcher.ordinal()]) {
                case Zone.PRIMARY /*1*/:
                    this.f1365e.onServiceStatusResponse((ServiceStatusResponse) obj);
                    return;
                case Zone.SECONDARY /*2*/:
                    this.f1365e.onReceiptsResponse((ReceiptsResponse) obj);
                    return;
                case Protocol.GGP /*3*/:
                    this.f1365e.onSearchResponse((SearchResponse) obj);
                    return;
                case Type.MF /*4*/:
                    this.f1365e.onSearchByIdResponse((SearchByIdResponse) obj);
                    return;
                case Service.RJE /*5*/:
                    this.f1365e.onPurchaseResponse((PurchaseResponse) obj);
                    PurchaseResponse purchaseResponse = (PurchaseResponse) obj;
                    List receipts = purchaseResponse.getReceipts();
                    if (purchaseResponse.getStatus() == Status.SUCCESSFUL && receipts != null && receipts.size() > 0) {
                        m991a(CommandDispatcher.NOTIFY_RECEIPT_RECEIVED, purchaseResponse.getRequestId(), null);
                        return;
                    }
                    return;
                default:
                    return;
            }
        } catch (Exception e) {
            aa.m691a(f1361a, "Error in callListener: " + e);
        }
        aa.m691a(f1361a, "Error in callListener: " + e);
    }

    protected void m993a(Context context, Intent intent) {
        az.m822c().m681a(context, intent, this);
    }
}
