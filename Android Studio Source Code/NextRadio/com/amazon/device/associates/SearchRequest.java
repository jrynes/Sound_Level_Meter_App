package com.amazon.device.associates;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchRequest extends ShoppingServiceRequest {
    private final String f1136a;
    private final String f1137b;
    private final Map<FilterType, String> f1138c;
    private SortType f1139d;
    private final int f1140e;

    public SearchRequest(String str, String str2) {
        this(str, str2, 1);
    }

    public SearchRequest(String str, String str2, int i) {
        this.f1138c = new HashMap();
        ar.m782a((Object) str, "category");
        ar.m782a((Object) str2, "searchTerm");
        ar.m781a(i, "page");
        this.f1136a = str;
        this.f1137b = str2;
        this.f1139d = SortType.RELEVANCE;
        this.f1140e = i;
    }

    JSONObject m675a() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("category", this.f1136a);
            jSONObject.put("searchTerm", this.f1137b);
            JSONObject jSONObject2 = new JSONObject();
            for (FilterType filterType : this.f1138c.keySet()) {
                jSONObject2.put(filterType.toString(), this.f1138c.get(filterType));
            }
            jSONObject.put("filters", jSONObject2);
            jSONObject.put("sortType", this.f1139d.toString());
            jSONObject.put("page", this.f1140e);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public String toString() {
        String str = null;
        try {
            str = m675a().toString(4);
        } catch (JSONException e) {
        }
        return str;
    }

    public String getCategory() {
        return this.f1136a;
    }

    public String getSearchTerm() {
        return this.f1137b;
    }

    public void addFilter(FilterType filterType, String str) {
        this.f1138c.put(filterType, str);
    }

    public void removeFilter(FilterType filterType) {
        this.f1138c.remove(filterType);
    }

    public Map<FilterType, String> getFilters() {
        return Collections.unmodifiableMap(this.f1138c);
    }

    public SortType getSortType() {
        return this.f1139d;
    }

    public void setSortType(SortType sortType) {
        this.f1139d = sortType;
    }

    public int getPage() {
        return this.f1140e;
    }
}
