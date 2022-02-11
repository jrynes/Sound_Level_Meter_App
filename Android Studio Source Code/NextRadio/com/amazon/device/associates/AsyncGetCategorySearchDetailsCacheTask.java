package com.amazon.device.associates;

/* renamed from: com.amazon.device.associates.g */
class AsyncGetCategorySearchDetailsCacheTask extends ap<bz> {
    private static bz f1334b;

    protected /* bridge */ /* synthetic */ bl m959b() {
        return m957a();
    }

    protected /* bridge */ /* synthetic */ Object doInBackground(Object[] objArr) {
        return m958a((ap[]) objArr);
    }

    static {
        f1334b = null;
    }

    public AsyncGetCategorySearchDetailsCacheTask() {
        super("catsearch.db", 2592000000L);
    }

    protected bz m958a(ap<bz>... apVarArr) {
        ap apVar = apVarArr[0];
        bo boVar = new bo();
        if (boVar != null) {
            boVar.m705e();
            bl d = boVar.m897d();
            if (d == null) {
                apVar.m770b(false);
                apVar.m768a(false);
                if (apVar.f1186a == null) {
                    apVar.f1186a = m957a();
                    Log.m1018c("AsyncGetgetCategorySearchDetailsAsyncTask", "Setting Default Templates");
                } else {
                    Log.m1018c("AsyncGetgetCategorySearchDetailsAsyncTask", "Setting to expired Templates as getTemplates failed");
                }
                return (bz) apVar.f1186a;
            }
            if (d.m889b() >= 300 && d.m889b() <= 400) {
                Log.m1018c("AsyncGetgetCategorySearchDetailsAsyncTask", "Checking to load from Cache");
                apVar.f1186a = (bz) ab.m695a(m775g());
                if (apVar.f1186a != null) {
                    apVar.m768a(true);
                    apVar.m770b(false);
                    return (bz) apVar.f1186a;
                }
            }
            apVar.f1186a = d;
            apVar.m768a(true);
            apVar.m770b(true);
        }
        return (bz) apVar.f1186a;
    }

    protected bz m957a() {
        if (f1334b != null) {
            return f1334b;
        }
        f1334b = new bz();
        f1334b.m939b("All", "aps");
        f1334b.m939b("Appliances", "appliances");
        f1334b.m939b("ArtsCraftsAndSewing", "arts-crafts");
        f1334b.m939b("Automotive", "automotive");
        f1334b.m939b("Baby", "baby");
        f1334b.m939b("Beauty", "beauty");
        f1334b.m939b("Books", "books");
        f1334b.m939b("CellPhones", "mobile");
        f1334b.m939b("CellPhoneAccessories", "mobile");
        f1334b.m939b("ClothingAndAccessories", "apparel");
        f1334b.m939b("Collectibles", "collectibles");
        f1334b.m939b("Computers", "computers");
        f1334b.m939b("Electronics", "electronics");
        f1334b.m939b("GiftCards", "gift-cards");
        f1334b.m939b("Grocery", "grocery");
        f1334b.m939b("Home", "garden");
        f1334b.m939b("Kitchen", "garden");
        f1334b.m939b("OutdoorLiving", "garden");
        f1334b.m939b("HealthPersonalCare", "hpc");
        f1334b.m939b("IndustrialAndScientific", "industrial");
        f1334b.m939b("Jewelry", "jewelry");
        f1334b.m939b("KindleStore", "digital-text");
        f1334b.m939b("LawnAndGarden", "lawngarden");
        f1334b.m939b("MagazineSubscriptions", "magazines");
        f1334b.m939b("Misc", "misc");
        f1334b.m939b("MobileApps", "mobile-apps");
        f1334b.m939b("MoviesAndTV", "dvd");
        f1334b.m939b("MP3Downloads", "digital-music");
        f1334b.m939b("Music", "music");
        f1334b.m939b("MusicalInstruments", "mi");
        f1334b.m939b("OfficeProducts", "office-products");
        f1334b.m939b("PetSupplies", "pets");
        f1334b.m939b("Photo", "photo");
        f1334b.m939b("Shoes", "shoes");
        f1334b.m939b("Software", "software");
        f1334b.m939b("SportsAndOutdoors", "sporting");
        f1334b.m939b("ToolsAndHomeImprovement", "tools");
        f1334b.m939b("ToysAndGames", "toys");
        f1334b.m939b("VideoGames", "videogames");
        f1334b.m939b("Watches", "watches");
        f1334b.m938a("RELEVANCE", "relevancerank");
        f1334b.m938a("BESTSELLING", "relevance-fs-browse-rank");
        f1334b.m938a("PRICE_LOW_TO_HIGH", "price");
        f1334b.m938a("PRICE_HIGH_TO_LOW", "-price");
        return f1334b;
    }
}
