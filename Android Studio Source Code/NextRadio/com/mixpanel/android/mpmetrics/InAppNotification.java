package com.mixpanel.android.mpmetrics;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.admarvel.android.ads.nativeads.AdMarvelNativeAd;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.stomp.Stomp.Headers.Send;
import org.json.JSONException;
import org.json.JSONObject;

public class InAppNotification implements Parcelable {
    public static final Creator<InAppNotification> CREATOR;
    private static final Pattern FILE_EXTENSION_PATTERN;
    private static final String LOGTAG = "MixpanelAPI.InAppNotif";
    private final String mBody;
    private final String mCallToAction;
    private final String mCallToActionUrl;
    private final JSONObject mDescription;
    private final int mId;
    private Bitmap mImage;
    private final String mImageUrl;
    private final int mMessageId;
    private final String mTitle;
    private final String mType;

    /* renamed from: com.mixpanel.android.mpmetrics.InAppNotification.1 */
    static class C10861 implements Creator<InAppNotification> {
        C10861() {
        }

        public InAppNotification createFromParcel(Parcel source) {
            return new InAppNotification(source);
        }

        public InAppNotification[] newArray(int size) {
            return new InAppNotification[size];
        }
    }

    public enum Type {
        UNKNOWN {
            public String toString() {
                return "*unknown_type*";
            }
        },
        MINI {
            public String toString() {
                return "mini";
            }
        },
        TAKEOVER {
            public String toString() {
                return "takeover";
            }
        };
    }

    public InAppNotification(Parcel in) {
        JSONObject temp = new JSONObject();
        try {
            temp = new JSONObject(in.readString());
        } catch (JSONException e) {
            Log.e(LOGTAG, "Error reading JSON when creating InAppNotification from Parcel");
        }
        this.mDescription = temp;
        this.mId = in.readInt();
        this.mMessageId = in.readInt();
        this.mType = in.readString();
        this.mTitle = in.readString();
        this.mBody = in.readString();
        this.mImageUrl = in.readString();
        this.mCallToAction = in.readString();
        this.mCallToActionUrl = in.readString();
        this.mImage = (Bitmap) in.readParcelable(Bitmap.class.getClassLoader());
    }

    InAppNotification(JSONObject description) throws BadDecideObjectException {
        try {
            this.mDescription = description;
            this.mId = description.getInt(Name.MARK);
            this.mMessageId = description.getInt("message_id");
            this.mType = description.getString(Send.TYPE);
            this.mTitle = description.getString(SettingsJsonConstants.PROMPT_TITLE_KEY);
            this.mBody = description.getString("body");
            this.mImageUrl = description.getString("image_url");
            this.mImage = Bitmap.createBitmap(ActiveMQPrefetchPolicy.DEFAULT_QUEUE_BROWSER_PREFETCH, ActiveMQPrefetchPolicy.DEFAULT_QUEUE_BROWSER_PREFETCH, Config.ARGB_8888);
            this.mCallToAction = description.getString(AdMarvelNativeAd.FIELD_CTA);
            this.mCallToActionUrl = description.getString("cta_url");
        } catch (JSONException e) {
            throw new BadDecideObjectException("Notification JSON was unexpected or bad", e);
        }
    }

    String toJSON() {
        return this.mDescription.toString();
    }

    JSONObject getCampaignProperties() {
        JSONObject ret = new JSONObject();
        try {
            ret.put("campaign_id", getId());
            ret.put("message_id", getMessageId());
            ret.put("message_type", "inapp");
            ret.put("message_subtype", this.mType);
        } catch (JSONException e) {
            Log.e(LOGTAG, "Impossible JSON Exception", e);
        }
        return ret;
    }

    public int getId() {
        return this.mId;
    }

    public int getMessageId() {
        return this.mMessageId;
    }

    public Type getType() {
        if (Type.MINI.toString().equals(this.mType)) {
            return Type.MINI;
        }
        if (Type.TAKEOVER.toString().equals(this.mType)) {
            return Type.TAKEOVER;
        }
        return Type.UNKNOWN;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getBody() {
        return this.mBody;
    }

    public String getImageUrl() {
        return this.mImageUrl;
    }

    public String getImage2xUrl() {
        return sizeSuffixUrl(this.mImageUrl, "@2x");
    }

    public String getImage4xUrl() {
        return sizeSuffixUrl(this.mImageUrl, "@4x");
    }

    public String getCallToAction() {
        return this.mCallToAction;
    }

    public String getCallToActionUrl() {
        return this.mCallToActionUrl;
    }

    void setImage(Bitmap image) {
        this.mImage = image;
    }

    public Bitmap getImage() {
        return this.mImage;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mDescription.toString());
        dest.writeInt(this.mId);
        dest.writeInt(this.mMessageId);
        dest.writeString(this.mType);
        dest.writeString(this.mTitle);
        dest.writeString(this.mBody);
        dest.writeString(this.mImageUrl);
        dest.writeString(this.mCallToAction);
        dest.writeString(this.mCallToActionUrl);
        dest.writeParcelable(this.mImage, flags);
    }

    static {
        CREATOR = new C10861();
        FILE_EXTENSION_PATTERN = Pattern.compile("(\\.[^./]+$)");
    }

    static String sizeSuffixUrl(String url, String sizeSuffix) {
        Matcher matcher = FILE_EXTENSION_PATTERN.matcher(url);
        if (matcher.find()) {
            return matcher.replaceFirst(sizeSuffix + "$1");
        }
        return url;
    }
}
