package com.amazon.device.associates;

import android.os.RemoteException;
import android.util.Log;
import com.amazon.device.associates.SearchResponse.Status;
import com.amazon.device.associates.bb.CommandDispatcher;
import com.amazon.venezia.command.SuccessResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONArray;
import org.json.JSONException;

/* renamed from: com.amazon.device.associates.n */
final class KiwiSearchCommandTask extends as {
    private static final String f1356b;

    static {
        f1356b = KiwiSearchCommandTask.class.getSimpleName();
    }

    KiwiSearchCommandTask(RequestId requestId, SearchRequest searchRequest, bb bbVar) {
        super("physical_search", Stomp.V1_0, requestId, bbVar);
        m713a("category", (Object) searchRequest.getCategory());
        m713a("searchTerm", (Object) searchRequest.getSearchTerm());
        m713a("sortType", (Object) searchRequest.getSortType().toString());
        m713a("filters", (Object) KiwiSearchCommandTask.m982a(searchRequest.getFilters()));
        m713a("page", (Object) Integer.toString(searchRequest.getPage()));
        m715a(false);
    }

    protected void m983a() {
        this.a.m866a(CommandDispatcher.SEARCH, new SearchResponse(m717b(), Status.NOT_SUPPORTED));
    }

    protected void onSuccess(SuccessResult successResult) throws RemoteException {
        aa.m693b(f1356b, "onSuccess");
        ShoppingServiceResponse searchResponse = new SearchResponse(m717b(), Status.FAILED);
        ShoppingServiceResponse searchResponse2;
        try {
            Map data = successResult.getData();
            aa.m693b(f1356b, "data: " + data);
            if (data.containsKey("errorMessage")) {
                Log.e(f1356b, (String) data.get("errorMessage"));
            }
            Status valueOf = Status.valueOf((String) data.get("requestStatus"));
            if (valueOf == Status.SUCCESSFUL) {
                String str = (String) data.get("userId");
                Object arrayList = new ArrayList();
                try {
                    ba.m862c(new JSONArray((String) data.get("products")), arrayList);
                } catch (JSONException e) {
                    aa.m691a(f1356b, "Error parsing JSON for products: " + e.getMessage());
                }
                searchResponse2 = new SearchResponse(m717b(), valueOf, arrayList, Integer.parseInt((String) data.get("page")), Integer.parseInt((String) data.get("totalPages")));
                this.a.m866a(CommandDispatcher.SEARCH, searchResponse2);
            }
            searchResponse2 = new SearchResponse(m717b(), valueOf);
            this.a.m866a(CommandDispatcher.SEARCH, searchResponse2);
        } catch (Exception e2) {
            aa.m691a(f1356b, "error in onSuccess: " + e2.getMessage());
            searchResponse2 = searchResponse;
        }
    }

    private static Map<String, String> m982a(Map<FilterType, String> map) {
        if (map == null) {
            return null;
        }
        Map<String, String> hashMap = new HashMap(map.size());
        for (Entry entry : map.entrySet()) {
            FilterType filterType = (FilterType) entry.getKey();
            if (filterType != null) {
                hashMap.put(filterType.toString(), entry.getValue());
            }
        }
        return hashMap;
    }
}
