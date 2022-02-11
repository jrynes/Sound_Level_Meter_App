package com.amazon.device.ads;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONObject;

class AppInfo {
    private final String applicationLabel;
    private final JSONObject packageInfoUrlJSON;
    private final PackageManager packageManager;
    private final String packageName;
    private final String versionCode;
    private final String versionName;

    public AppInfo(Context context) {
        this.packageName = context.getPackageName();
        this.packageManager = context.getPackageManager();
        this.applicationLabel = (String) this.packageManager.getApplicationLabel(context.getApplicationInfo());
        PackageInfo packageInfo = null;
        try {
            packageInfo = this.packageManager.getPackageInfo(this.packageName, 0);
        } catch (NameNotFoundException e) {
        }
        this.versionName = packageInfo != null ? packageInfo.versionName : Stomp.EMPTY;
        this.versionCode = packageInfo != null ? Integer.toString(packageInfo.versionCode) : Stomp.EMPTY;
        this.packageInfoUrlJSON = new JSONObject();
        JSONUtils.put(this.packageInfoUrlJSON, "lbl", this.applicationLabel);
        JSONUtils.put(this.packageInfoUrlJSON, "pn", this.packageName);
        JSONUtils.put(this.packageInfoUrlJSON, "v", this.versionCode);
        JSONUtils.put(this.packageInfoUrlJSON, "vn", this.versionName);
    }

    protected AppInfo() {
        this.packageName = null;
        this.applicationLabel = null;
        this.versionCode = null;
        this.versionName = null;
        this.packageInfoUrlJSON = null;
        this.packageManager = null;
    }

    public JSONObject getPackageInfoJSON() {
        return this.packageInfoUrlJSON;
    }

    public String getPackageInfoJSONString() {
        if (this.packageInfoUrlJSON != null) {
            return this.packageInfoUrlJSON.toString();
        }
        return null;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public PackageManager getPackageManager() {
        return this.packageManager;
    }
}
