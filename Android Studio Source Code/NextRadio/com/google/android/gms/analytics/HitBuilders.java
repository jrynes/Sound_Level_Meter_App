package com.google.android.gms.analytics;

import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.google.android.gms.analytics.ecommerce.Promotion;
import com.google.android.gms.analytics.internal.zzae;
import com.google.android.gms.analytics.internal.zzam;
import com.google.android.gms.tagmanager.DataLayer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers;

public class HitBuilders {

    protected static class HitBuilder<T extends HitBuilder> {
        private Map<String, String> zzPm;
        ProductAction zzPn;
        Map<String, List<Product>> zzPo;
        List<Promotion> zzPp;
        List<Product> zzPq;

        protected HitBuilder() {
            this.zzPm = new HashMap();
            this.zzPo = new HashMap();
            this.zzPp = new ArrayList();
            this.zzPq = new ArrayList();
        }

        public T addImpression(Product product, String impressionList) {
            if (product == null) {
                zzae.zzaK("product should be non-null");
            } else {
                if (impressionList == null) {
                    impressionList = Stomp.EMPTY;
                }
                if (!this.zzPo.containsKey(impressionList)) {
                    this.zzPo.put(impressionList, new ArrayList());
                }
                ((List) this.zzPo.get(impressionList)).add(product);
            }
            return this;
        }

        public T addProduct(Product product) {
            if (product == null) {
                zzae.zzaK("product should be non-null");
            } else {
                this.zzPq.add(product);
            }
            return this;
        }

        public T addPromotion(Promotion promotion) {
            if (promotion == null) {
                zzae.zzaK("promotion should be non-null");
            } else {
                this.zzPp.add(promotion);
            }
            return this;
        }

        public Map<String, String> build() {
            Map<String, String> hashMap = new HashMap(this.zzPm);
            if (this.zzPn != null) {
                hashMap.putAll(this.zzPn.build());
            }
            int i = 1;
            for (Promotion zzba : this.zzPp) {
                hashMap.putAll(zzba.zzba(zzc.zzZ(i)));
                i++;
            }
            i = 1;
            for (Product zzba2 : this.zzPq) {
                hashMap.putAll(zzba2.zzba(zzc.zzX(i)));
                i++;
            }
            int i2 = 1;
            for (Entry entry : this.zzPo.entrySet()) {
                List<Product> list = (List) entry.getValue();
                String zzac = zzc.zzac(i2);
                int i3 = 1;
                for (Product zzba3 : list) {
                    hashMap.putAll(zzba3.zzba(zzac + zzc.zzab(i3)));
                    i3++;
                }
                if (!TextUtils.isEmpty((CharSequence) entry.getKey())) {
                    hashMap.put(zzac + "nm", entry.getKey());
                }
                i2++;
            }
            return hashMap;
        }

        protected String get(String paramName) {
            return (String) this.zzPm.get(paramName);
        }

        public final T set(String paramName, String paramValue) {
            if (paramName != null) {
                this.zzPm.put(paramName, paramValue);
            } else {
                zzae.zzaK(" HitBuilder.set() called with a null paramName.");
            }
            return this;
        }

        public final T setAll(Map<String, String> params) {
            if (params != null) {
                this.zzPm.putAll(new HashMap(params));
            }
            return this;
        }

        public T setCampaignParamsFromUrl(String utmParams) {
            Object zzbu = zzam.zzbu(utmParams);
            if (!TextUtils.isEmpty(zzbu)) {
                Map zzbs = zzam.zzbs(zzbu);
                set("&cc", (String) zzbs.get("utm_content"));
                set("&cm", (String) zzbs.get("utm_medium"));
                set("&cn", (String) zzbs.get("utm_campaign"));
                set("&cs", (String) zzbs.get("utm_source"));
                set("&ck", (String) zzbs.get("utm_term"));
                set("&ci", (String) zzbs.get("utm_id"));
                set("&anid", (String) zzbs.get("anid"));
                set("&gclid", (String) zzbs.get("gclid"));
                set("&dclid", (String) zzbs.get("dclid"));
                set("&aclid", (String) zzbs.get("aclid"));
                set("&gmob_t", (String) zzbs.get("gmob_t"));
            }
            return this;
        }

        public T setCustomDimension(int index, String dimension) {
            set(zzc.zzT(index), dimension);
            return this;
        }

        public T setCustomMetric(int index, float metric) {
            set(zzc.zzV(index), Float.toString(metric));
            return this;
        }

        protected T setHitType(String hitType) {
            set("&t", hitType);
            return this;
        }

        public T setNewSession() {
            set("&sc", "start");
            return this;
        }

        public T setNonInteraction(boolean nonInteraction) {
            set("&ni", zzam.zzK(nonInteraction));
            return this;
        }

        public T setProductAction(ProductAction action) {
            this.zzPn = action;
            return this;
        }

        public T setPromotionAction(String action) {
            this.zzPm.put("&promoa", action);
            return this;
        }
    }

    @Deprecated
    public static class AppViewBuilder extends HitBuilder<AppViewBuilder> {
        public AppViewBuilder() {
            set("&t", "screenview");
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }
    }

    public static class EventBuilder extends HitBuilder<EventBuilder> {
        public EventBuilder() {
            set("&t", DataLayer.EVENT_KEY);
        }

        public EventBuilder(String category, String action) {
            this();
            setCategory(category);
            setAction(action);
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }

        public EventBuilder setAction(String action) {
            set("&ea", action);
            return this;
        }

        public EventBuilder setCategory(String category) {
            set("&ec", category);
            return this;
        }

        public EventBuilder setLabel(String label) {
            set("&el", label);
            return this;
        }

        public EventBuilder setValue(long value) {
            set("&ev", Long.toString(value));
            return this;
        }
    }

    public static class ExceptionBuilder extends HitBuilder<ExceptionBuilder> {
        public ExceptionBuilder() {
            set("&t", "exception");
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }

        public ExceptionBuilder setDescription(String description) {
            set("&exd", description);
            return this;
        }

        public ExceptionBuilder setFatal(boolean fatal) {
            set("&exf", zzam.zzK(fatal));
            return this;
        }
    }

    @Deprecated
    public static class ItemBuilder extends HitBuilder<ItemBuilder> {
        public ItemBuilder() {
            set("&t", "item");
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }

        public ItemBuilder setCategory(String category) {
            set("&iv", category);
            return this;
        }

        public ItemBuilder setCurrencyCode(String currencyCode) {
            set("&cu", currencyCode);
            return this;
        }

        public ItemBuilder setName(String name) {
            set("&in", name);
            return this;
        }

        public ItemBuilder setPrice(double price) {
            set("&ip", Double.toString(price));
            return this;
        }

        public ItemBuilder setQuantity(long quantity) {
            set("&iq", Long.toString(quantity));
            return this;
        }

        public ItemBuilder setSku(String sku) {
            set("&ic", sku);
            return this;
        }

        public ItemBuilder setTransactionId(String transactionid) {
            set("&ti", transactionid);
            return this;
        }
    }

    public static class ScreenViewBuilder extends HitBuilder<ScreenViewBuilder> {
        public ScreenViewBuilder() {
            set("&t", "screenview");
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }
    }

    public static class SocialBuilder extends HitBuilder<SocialBuilder> {
        public SocialBuilder() {
            set("&t", NotificationCompat.CATEGORY_SOCIAL);
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }

        public SocialBuilder setAction(String action) {
            set("&sa", action);
            return this;
        }

        public SocialBuilder setNetwork(String network) {
            set("&sn", network);
            return this;
        }

        public SocialBuilder setTarget(String target) {
            set("&st", target);
            return this;
        }
    }

    public static class TimingBuilder extends HitBuilder<TimingBuilder> {
        public TimingBuilder() {
            set("&t", "timing");
        }

        public TimingBuilder(String category, String variable, long value) {
            this();
            setVariable(variable);
            setValue(value);
            setCategory(category);
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }

        public TimingBuilder setCategory(String category) {
            set("&utc", category);
            return this;
        }

        public TimingBuilder setLabel(String label) {
            set("&utl", label);
            return this;
        }

        public TimingBuilder setValue(long value) {
            set("&utt", Long.toString(value));
            return this;
        }

        public TimingBuilder setVariable(String variable) {
            set("&utv", variable);
            return this;
        }
    }

    @Deprecated
    public static class TransactionBuilder extends HitBuilder<TransactionBuilder> {
        public TransactionBuilder() {
            set("&t", Headers.TRANSACTION);
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }

        public TransactionBuilder setAffiliation(String affiliation) {
            set("&ta", affiliation);
            return this;
        }

        public TransactionBuilder setCurrencyCode(String currencyCode) {
            set("&cu", currencyCode);
            return this;
        }

        public TransactionBuilder setRevenue(double revenue) {
            set("&tr", Double.toString(revenue));
            return this;
        }

        public TransactionBuilder setShipping(double shipping) {
            set("&ts", Double.toString(shipping));
            return this;
        }

        public TransactionBuilder setTax(double tax) {
            set("&tt", Double.toString(tax));
            return this;
        }

        public TransactionBuilder setTransactionId(String transactionid) {
            set("&ti", transactionid);
            return this;
        }
    }
}
