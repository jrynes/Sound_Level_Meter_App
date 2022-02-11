package com.amazon.device.associates;

/* compiled from: AsyncGetURLTemplatesCacheTask */
class be extends ap<RetailURLTemplates> {
    private static RetailURLTemplates f1257b;

    protected /* bridge */ /* synthetic */ bl m870b() {
        return m868a();
    }

    protected /* bridge */ /* synthetic */ Object doInBackground(Object[] objArr) {
        return m869a((ap[]) objArr);
    }

    public be() {
        super("rut.db", 86400000);
    }

    protected RetailURLTemplates m869a(ap<RetailURLTemplates>... apVarArr) {
        ap apVar = apVarArr[0];
        GetURLTemplatesCall getURLTemplatesCall = new GetURLTemplatesCall();
        if (getURLTemplatesCall != null) {
            getURLTemplatesCall.m977a(bp.m901b());
            getURLTemplatesCall.m705e();
            bl d = getURLTemplatesCall.m980d();
            if (d == null) {
                apVar.m770b(false);
                apVar.m768a(false);
                if (apVar.f1186a == null) {
                    apVar.f1186a = m868a();
                    Log.m1018c("AsyncGetURLTemplatesCacheTask", "Setting Default Templates");
                } else {
                    Log.m1018c("AsyncGetURLTemplatesCacheTask", "Setting to expired Templates as getTemplates failed");
                }
                return (RetailURLTemplates) apVar.f1186a;
            }
            if (d.m889b() >= 300 && d.m889b() <= 400) {
                Log.m1018c("AsyncGetURLTemplatesCacheTask", "Checking to load from Cache");
                apVar.f1186a = (RetailURLTemplates) ab.m695a(m775g());
                if (apVar.f1186a != null) {
                    apVar.m768a(true);
                    apVar.m770b(false);
                    return (RetailURLTemplates) apVar.f1186a;
                }
            }
            apVar.f1186a = d;
            apVar.m768a(true);
            apVar.m770b(true);
        }
        return (RetailURLTemplates) apVar.f1186a;
    }

    protected RetailURLTemplates m868a() {
        if (f1257b == null) {
            f1257b = new RetailURLTemplates();
            f1257b.m975a(RetailURLTemplates.f1342a, "http://www.amazon.com/gp/aw/d/$ASIN?tag=defaulttag-20&linkCode=da2&ascsubtag=$SUBTAG");
            f1257b.m975a(RetailURLTemplates.f1343b, "http://www.amazon.com/gp/aw/h.html?tag=defaulttag-20&linkCode=da1&ascsubtag=$SUBTAG");
            f1257b.m975a(RetailURLTemplates.f1344c, "tag=defaulttag-20&linkCode=da5&ascsubtag=$SUBTAG");
            f1257b.m975a(RetailURLTemplates.f1345d, "www.amazon.com/gp/aw/s?tag=defaulttag-20&linkCode=da4&i=$CATEGORY&k=$SEARCH&sort=$SORTTYPE&p_lbr_brands_browse-bin=$BRAND&ascsubtag=$SUBTAG");
            f1257b.m975a(RetailURLTemplates.f1346e, "http://www.amazon.com/gp/aw/d/$ASIN?tag=defaulttag-20&linkCode=da6&ascsubtag=$SUBTAG");
            f1257b.m975a(RetailURLTemplates.f1347f, "http://www.amazon.com/gp/aw/d/$ASIN?tag=defaulttag-20&linkCode=da7&ascsubtag=$SUBTAG");
            f1257b.m975a(RetailURLTemplates.f1348g, "http://www.amazon.com/gp/dmusic/device/mp3/store/album/$ASIN?device=android_browser&tag=defaulttag-20&linkCode=da7&ascsubtag=$SUBTAG");
            f1257b.m975a(RetailURLTemplates.f1349h, "http://www.amazon.com/gp/dmusic/device/mp3/store/track/$ASIN?device=android_browser&tag=defaulttag-20&linkCode=da7&ascsubtag=$SUBTAG");
        }
        return f1257b;
    }
}
