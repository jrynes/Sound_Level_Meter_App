package com.amazon.device.associates;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import com.amazon.android.framework.context.ContextManager;
import com.amazon.android.framework.prompt.PromptContent;
import com.amazon.android.framework.prompt.SimplePrompt;
import com.amazon.android.framework.resource.Resource;
import java.util.HashSet;
import java.util.Set;

/* compiled from: FailurePrompt */
final class bc extends SimplePrompt {
    private static final String f1252a;
    private static final Set<String> f1253b;
    @Resource
    private ContextManager f1254c;
    private final PromptContent f1255d;

    static {
        f1252a = bc.class.getSimpleName();
        f1253b = new HashSet();
        f1253b.add("Amazon Appstore Update Required");
        f1253b.add("Aktualisierung von Amazon App-Shop erforderlich");
        f1253b.add("Es necesario actualizar Tienda Apps de Amazon");
        f1253b.add("Mise \u00e0 jour requise pour Amazon Appstore");
        f1253b.add("La mise \u00e0 jour de Amazon App-Shop est requise");
        f1253b.add("Amazon App-Shop Aggiornamento necessario");
        f1253b.add("Amazon \u30a2\u30d7\u30ea\u30b9\u30c8\u30a2 \u66f4\u65b0\u304c\u5fc5\u8981\u3067\u3059");
        f1253b.add("Atualiza\u00e7\u00e3o da Loja de Apps da Amazon necess\u00e1ria");
        f1253b.add("\u5df2\u8bf7\u6c42 \u4e9a\u9a6c\u900a\u5e94\u7528\u5546\u5e97 \u66f4\u65b0");
    }

    bc(PromptContent promptContent) {
        super(promptContent);
        this.f1255d = promptContent;
    }

    protected void doAction() {
        aa.m693b(f1252a, "doAction");
        if ("Amazon Appstore required".equalsIgnoreCase(this.f1255d.getTitle()) || f1253b.contains(this.f1255d.getTitle())) {
            try {
                Activity visible = this.f1254c.getVisible();
                if (visible == null) {
                    visible = this.f1254c.getRoot();
                }
                visible.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://www.amazon.com/gp/mas/get-appstore/android/ref=mas_mx_mba_iap_dl")));
            } catch (Exception e) {
                aa.m693b(f1252a, "Exception in PurchaseItemCommandTask.OnSuccess: " + e);
            }
        }
    }

    protected long getExpirationDurationInSeconds() {
        return 31536000;
    }

    public String toString() {
        return f1252a;
    }
}
