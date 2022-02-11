package com.amazon.device.associates;

public class OpenSearchPageRequest extends OpenRetailPageRequest {
    private String f1082a;
    private String f1083b;
    private String f1084c;
    private SortType f1085d;

    public OpenSearchPageRequest(String str) {
        ar.m782a((Object) str, "searchTerm");
        this.f1082a = str;
    }

    public OpenSearchPageRequest(String str, String str2) {
        ar.m782a((Object) str2, "searchTerm");
        ar.m782a((Object) str, "category");
        this.f1083b = str;
        this.f1082a = str2;
    }

    public String getSearchTerm() {
        return this.f1082a;
    }

    public String getCategory() {
        return this.f1083b;
    }

    public String getBrand() {
        return this.f1084c;
    }

    public void setBrand(String str) {
        this.f1084c = str;
    }

    public SortType getSortType() {
        return this.f1085d;
    }

    public void setSortType(SortType sortType) {
        this.f1085d = sortType;
    }
}
