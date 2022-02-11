package com.amazon.device.associates;

import android.os.RemoteException;
import com.amazon.android.Kiwi;
import com.amazon.android.framework.exception.KiwiException;
import com.amazon.android.framework.prompt.PromptContent;
import com.amazon.android.framework.prompt.PromptManager;
import com.amazon.android.framework.task.command.AbstractCommandTask;
import com.amazon.android.licensing.LicenseFailurePromptContentMapper;
import com.amazon.venezia.command.FailureResult;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/* compiled from: KiwiBaseCommandTask */
abstract class as extends AbstractCommandTask {
    private static final String f1171b;
    protected final bb f1172a;
    private final String f1173c;
    private final String f1174d;
    private final RequestId f1175e;
    private boolean f1176f;
    private final Map<String, Object> f1177g;
    private final LicenseFailurePromptContentMapper f1178h;

    protected abstract void m712a();

    static {
        f1171b = as.class.getSimpleName();
    }

    as(String str, String str2, RequestId requestId, bb bbVar) {
        this.f1178h = new LicenseFailurePromptContentMapper();
        this.f1175e = requestId;
        this.f1173c = str;
        this.f1174d = str2;
        this.f1176f = false;
        this.f1172a = bbVar;
        this.f1177g = new HashMap();
        this.f1177g.put("requestId", this.f1175e.toString());
        this.f1177g.put("sdkVersion", this.f1172a.m865a());
    }

    protected void m713a(String str, Object obj) {
        this.f1177g.put(str, obj);
    }

    protected RequestId m717b() {
        return this.f1175e;
    }

    protected void m715a(boolean z) {
        this.f1176f = z;
    }

    protected boolean isExecutionNeeded() {
        return true;
    }

    protected String getCommandName() {
        return this.f1173c;
    }

    protected String getCommandVersion() {
        return this.f1174d;
    }

    protected Map<String, Object> getCommandData() {
        return this.f1177g;
    }

    protected void onFailure(FailureResult failureResult) throws RemoteException, KiwiException {
        if (failureResult == null) {
            aa.m693b(f1171b, "onFailure: null result");
            return;
        }
        aa.m693b(f1171b, "onFailure: result message: " + failureResult.getDisplayableMessage());
        if (this.f1176f) {
            Kiwi.getPromptManager().present(new bc(new PromptContent(failureResult.getDisplayableName(), failureResult.getDisplayableMessage(), failureResult.getButtonLabel(), failureResult.show())));
        }
        m712a();
    }

    protected void onException(KiwiException kiwiException) {
        aa.m693b(f1171b, "onException, result: " + kiwiException.getMessage());
        try {
            if (this.f1176f) {
                PromptManager promptManager = Kiwi.getPromptManager();
                PromptContent map = this.f1178h.map(kiwiException);
                if (map != null) {
                    promptManager.present(new bc(map));
                }
            }
            m712a();
        } catch (Exception e) {
            aa.m691a(f1171b, "error in onException: " + e);
        }
    }

    protected boolean m716a(String str, String str2, JSONObject jSONObject) {
        Object obj;
        boolean isSignedByKiwi;
        Object obj2 = null;
        try {
            Date date;
            String a = this.f1172a.m865a();
            String string = jSONObject.getString("productType");
            String string2 = jSONObject.getString("productId");
            String string3 = jSONObject.getString("purchaseToken");
            String string4 = jSONObject.getString("receiptId");
            int i = jSONObject.getInt("quantity");
            Date date2 = new Date(jSONObject.getLong("purchaseDate"));
            if (jSONObject.getBoolean("isCanceled")) {
                date = new Date(jSONObject.getLong("cancelDate"));
            } else {
                date = null;
            }
            obj2 = String.format("%s|%s|%s|%s|%s|%s|%s|%d|%tQ|%tQ", new Object[]{a, str, str2, string2, string, string3, string4, Integer.valueOf(i), date2, date});
            String string5 = jSONObject.getString("signature");
            aa.m693b(f1171b, "string2sign:\n" + obj2 + "\nsignature:\n" + string5);
            obj = string5;
            isSignedByKiwi = Kiwi.isSignedByKiwi(obj2, string5);
        } catch (Exception e) {
            aa.m691a(f1171b, "error in verifyReceipt: " + e);
            obj = null;
            isSignedByKiwi = false;
        }
        if (!isSignedByKiwi) {
            try {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("stringToSign", obj2);
                jSONObject2.put("signature", obj);
                m714a("SDKReceiptVerificationFailed", jSONObject2);
            } catch (Exception e2) {
                aa.m693b(f1171b, "error calling submitMetric: " + e2);
            }
        }
        return isSignedByKiwi;
    }

    protected void m714a(String str, JSONObject jSONObject) {
        Kiwi.addCommandToCommandTaskPipeline(new bw(m717b(), str, jSONObject.toString(), this.f1172a), this.f1172a.m867b());
    }
}
