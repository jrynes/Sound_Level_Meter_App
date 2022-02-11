package com.amazon.device.associates;

import android.os.RemoteException;
import android.util.Log;
import com.amazon.device.associates.bb.CommandDispatcher;
import com.amazon.venezia.command.SuccessResult;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.activemq.transport.stomp.Stomp;

/* renamed from: com.amazon.device.associates.c */
final class KiwiUserDataCommandTask extends as {
    private static final String f1303b;
    private final Set<PurchaseExperience> f1304c;
    private boolean f1305d;
    private boolean f1306e;

    static {
        f1303b = KiwiUserDataCommandTask.class.getSimpleName();
    }

    KiwiUserDataCommandTask(RequestId requestId, bb bbVar) {
        super("get_userData", Stomp.V1_0, requestId, bbVar);
        this.f1304c = new HashSet();
        this.f1304c.add(PurchaseExperience.DIRECT_WITH_DETAIL);
        this.f1304c.add(PurchaseExperience.DIRECT_WITH_PREVIEW);
        this.f1305d = false;
        this.f1306e = false;
        m715a(false);
    }

    protected void m941a() {
        this.a.m866a(CommandDispatcher.GET_SERVICE_STATUS, new ServiceStatusResponse(m717b(), null, this.f1304c, this.f1305d, this.f1306e));
    }

    protected void onSuccess(SuccessResult successResult) throws RemoteException {
        ShoppingServiceResponse serviceStatusResponse;
        aa.m693b(f1303b, "onSuccess");
        try {
            Map data = successResult.getData();
            aa.m693b(f1303b, "data: " + data);
            if (data.containsKey("errorMessage")) {
                Log.e(f1303b, (String) data.get("errorMessage"));
            }
            if (((String) data.get("requestStatus")).equals("SUCCESSFUL")) {
                String str = (String) data.get("userId");
                String str2 = (String) data.get("marketplace");
                if (((Set) data.get("availableProductTypes")).contains("PHYSICAL")) {
                    this.f1304c.add(PurchaseExperience.IN_APP);
                    this.f1305d = true;
                    this.f1306e = true;
                }
                serviceStatusResponse = new ServiceStatusResponse(m717b(), new UserData(str, str2), this.f1304c, this.f1305d, this.f1306e);
            } else {
                serviceStatusResponse = new ServiceStatusResponse(m717b(), new UserData(null, null), this.f1304c, this.f1305d, this.f1306e);
            }
        } catch (Exception e) {
            aa.m691a(f1303b, "error in onSuccess: " + e);
            serviceStatusResponse = null;
        }
        this.a.m866a(CommandDispatcher.GET_SERVICE_STATUS, serviceStatusResponse);
    }
}
