package com.amazon.device.ads;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.google.android.gms.actions.SearchIntents;

class AmazonDeviceLauncher {
    AmazonDeviceLauncher() {
    }

    boolean isWindowshopPresent(Context context) {
        if (context.getPackageManager().getLaunchIntentForPackage("com.amazon.windowshop") == null) {
            return false;
        }
        return true;
    }

    void launchWindowshopDetailPage(Context context, String asin) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.amazon.windowshop");
        if (intent != null) {
            intent.putExtra("com.amazon.windowshop.refinement.asin", asin);
            context.startActivity(intent);
        }
    }

    void launchWindowshopSearchPage(Context context, String keyword) {
        Intent intent = new Intent("android.intent.action.SEARCH");
        intent.setComponent(new ComponentName("com.amazon.windowshop", "com.amazon.windowshop.search.SearchResultsGridActivity"));
        intent.putExtra(SearchIntents.EXTRA_QUERY, keyword);
        try {
            context.startActivity(intent);
        } catch (RuntimeException e) {
        }
    }
}
