package com.nextradioapp.androidSDK.actions;

import com.google.android.gms.common.Scopes;
import com.nextradioapp.androidSDK.C1136R;
import com.nextradioapp.androidSDK.interfaces.IActivityManager;
import com.nextradioapp.core.Log;
import com.nextradioapp.core.dependencies.IDatabaseAdapter;
import com.nextradioapp.core.interfaces.IActionBuilder;
import com.nextradioapp.core.objects.ActionPayload;
import com.nextradioapp.core.objects.Enhancement;
import com.nextradioapp.core.objects.EventAction;
import com.nextradioapp.core.objects.NextRadioEventInfo;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.WKSRecord.Service;

public class EventActionBuilder implements IActionBuilder {
    private static final String TAG = "EventActionBuilder";
    private final int ELLIPSE_LENGTH;
    private final int NEXTRADIO_LINK_LENGTH;
    private final int TWITTER_LINK_LENGTH_LIMIT;
    private final int TWITTER_SIZE_LIMIT;
    private IActivityManager mContext;
    private IDatabaseAdapter mDatabase;

    public EventActionBuilder(IDatabaseAdapter db, IActivityManager mContext) {
        this.TWITTER_LINK_LENGTH_LIMIT = 22;
        this.NEXTRADIO_LINK_LENGTH = 14;
        this.ELLIPSE_LENGTH = 3;
        this.TWITTER_SIZE_LIMIT = Service.EMFIS_DATA;
        this.mDatabase = db;
        this.mContext = mContext;
    }

    private String getShareableLink(NextRadioEventInfo nInfo) {
        String linkMessage = null;
        String bodyMessage = getShareableMessage(nInfo);
        if (nInfo.stationInfo != null && nInfo.itemType != 0 && nInfo.UFIDIdentifier != null && nInfo.UFIDIdentifier.length() > 0) {
            linkMessage = " http://nextradioapp.com/share/" + nInfo.stationInfo.publicStationID + ReadOnlyContext.SEPARATOR + nInfo.UFIDIdentifier;
        } else if (nInfo.stationInfo != null) {
            linkMessage = " http://nextradioapp.com/share/" + nInfo.stationInfo.publicStationID;
        }
        int linkLength = 0;
        if (linkMessage != null) {
            linkLength = linkMessage.length();
            if (linkLength > 22) {
                linkLength = 22;
            }
        }
        int bodyLength = bodyMessage.length();
        int totalLength = (bodyLength + 14) + linkLength;
        StringBuffer stringBuffer = new StringBuffer(bodyMessage);
        if (totalLength > Service.EMFIS_DATA) {
            stringBuffer.delete((bodyLength - ((totalLength + 3) - 140)) - 1, bodyLength);
            stringBuffer.append("...");
        }
        stringBuffer.append(" ");
        stringBuffer.append(this.mContext.getCurrentApplication().getString(C1136R.string.via));
        stringBuffer.append(" @nextradioapp");
        if (linkMessage != null) {
            stringBuffer.append(linkMessage);
        }
        stringBuffer.append(" ");
        stringBuffer.append(this.mContext.getCurrentApplication().getString(C1136R.string.nowplayinghashtag));
        return stringBuffer.toString();
    }

    private String getShareableMessage(NextRadioEventInfo nInfo) {
        try {
            String messageBody;
            if (nInfo.itemType == 3 || nInfo.itemType == 2) {
                String messagePayload = nInfo.stationInfo.callLetters + ": " + nInfo.artist + " - " + nInfo.title + " ";
                messageBody = this.mContext.getCurrentApplication().getString(C1136R.string.actions_share_message_heard_on);
                messageBody.concat(" ");
                return messageBody.replace("^1", messagePayload);
            }
            messageBody = this.mContext.getCurrentApplication().getString(C1136R.string.actions_share_message_listening_to);
            String messagePayload1 = " ";
            String messagePayload2 = " ";
            String messagePayload3 = " ";
            messagePayload1 = nInfo.title;
            if (!(nInfo.artist == null || nInfo.artist.equals(Stomp.EMPTY))) {
                messagePayload2 = nInfo.artist;
            }
            if (nInfo.stationInfo.callLetters != null && !nInfo.stationInfo.callLetters.equals(Stomp.EMPTY) && nInfo.stationInfo.frequencySubChannel > 0) {
                messagePayload3 = nInfo.stationInfo.frequencyMHz() + " " + nInfo.stationInfo.callLetters + "-" + (nInfo.stationInfo.frequencySubChannel + 1);
            } else if (!(nInfo.stationInfo.callLetters == null || nInfo.stationInfo.callLetters.equals(Stomp.EMPTY))) {
                messagePayload3 = nInfo.stationInfo.frequencyMHz() + " " + nInfo.stationInfo.callLetters;
            }
            return messageBody.replace("^1", messagePayload1).replace("^2", messagePayload2).replace("^3", messagePayload3);
        } catch (Exception e) {
            return Stomp.EMPTY;
        }
    }

    private String getShareableSubject(NextRadioEventInfo nInfo) {
        return this.mContext.getCurrentApplication().getString(C1136R.string.heardon) + " " + nInfo.stationInfo.callLetters;
    }

    public EventAction getEventAction(NextRadioEventInfo nInfo, String type, ActionPayload payload, Map<String, String> map) {
        if (type.equals("web")) {
            return new WebURLAction(this.mDatabase, payload, this.mContext, (String) map.get("URL"), (String) map.get("ActionText"));
        }
        if (type.equals("phone")) {
            return new PhoneCallEventAction(this.mDatabase, payload, this.mContext, (String) map.get("PhoneNumber"), Stomp.EMPTY);
        }
        if (type.equals(AdWebViewClient.SMS)) {
            String str;
            IDatabaseAdapter iDatabaseAdapter = this.mDatabase;
            IActivityManager iActivityManager = this.mContext;
            if (map.containsKey("Keyword")) {
                str = (String) map.get("Keyword");
            } else {
                str = (String) map.get("PhoneNumber");
            }
            return new SMSEventAction(iDatabaseAdapter, payload, iActivityManager, str, (String) map.get("ShortCode"));
        }
        if (type.equals("findnearby")) {
            return new MapEventAction(this.mDatabase, payload, this.mContext, (String) map.get("FindNearbyString"));
        }
        if (type.equals("mp3search")) {
            return new AmazonMP3SearchEventAction(this.mDatabase, payload, this.mContext, (String) map.get("Title"), (String) map.get("Artist"));
        }
        if (type.equals(CalendarEventAction.ACTION_CALENDAR)) {
            return new CalendarEventAction(this.mDatabase, payload, this.mContext, (String) map.get("Title"), (String) map.get("Description"), (String) map.get(HttpRequest.HEADER_LOCATION), (String) map.get("Starts"), (String) map.get("Ends"));
        }
        if (type.equals("coupon")) {
            return new QRCodeEventAction(this.mDatabase, payload, this.mContext, (String) map.get("CouponCode"), (String) map.get("CouponType"), (String) map.get("Title"), (String) map.get("Description"), (String) map.get(HttpRequest.HEADER_EXPIRES), (String) map.get("URL"));
        }
        boolean isThumb;
        if (!type.equals("thumbsdown")) {
            if (!type.equals("thumbsup")) {
                if (type.equals("share")) {
                    return new ShareEventAction(this.mDatabase, payload, this.mContext, (String) map.get("Subject"), (String) map.get("Link"), nInfo.stationInfo.imageURLHiRes);
                }
                if (type.equals(Scopes.EMAIL)) {
                    return new FeedBackEventAction(this.mDatabase, payload, this.mContext, (String) map.get("Subject"), (String) map.get("Link"));
                }
                if (!type.equals("thumbsup")) {
                    return null;
                }
                return new SaveEventAction(this.mDatabase, payload, this.mContext);
            }
        }
        if (type.equals("thumbsup")) {
            isThumb = true;
        } else {
            isThumb = false;
        }
        return new ThumbEventAction(this.mDatabase, payload, this.mContext, (String) map.get("CallLetters"), isThumb);
    }

    public ArrayList<EventAction> getEventActions(NextRadioEventInfo nInfo) {
        if (nInfo.stationInfo == null || nInfo.stationInfo.getStationType() == 0) {
            return null;
        }
        Map<String, String> map;
        ArrayList<EventAction> events = new ArrayList();
        if (nInfo.enhancements == null) {
            nInfo.enhancements = new ArrayList();
        }
        ActionPayload payload = new ActionPayload(nInfo.trackingID, nInfo.teID, null, nInfo.UFIDIdentifier, nInfo.stationInfo.publicStationID);
        Iterator i$ = nInfo.enhancements.iterator();
        while (i$.hasNext()) {
            Enhancement ad = (Enhancement) i$.next();
            map = new HashMap();
            if (ad.type.equals("web")) {
                map.put("URL", ad.datafields[0]);
                if (ad.datafields.length > 1) {
                    map.put("ActionText", ad.datafields[1]);
                }
            } else if (ad.type.equals("phone")) {
                map.put("PhoneNumber", ad.datafields[0]);
            } else if (ad.type.equals(AdWebViewClient.SMS)) {
                map.put("Keyword", ad.datafields[1]);
                map.put("ShortCode", ad.datafields[0]);
            } else if (ad.type.equals("findnearby")) {
                map.put("FindNearbyString", ad.datafields[0]);
            } else if (ad.type.equals("mp3search")) {
                map.put("Title", nInfo.title);
                map.put("Artist", nInfo.artist);
            } else if (ad.type.equals(CalendarEventAction.ACTION_CALENDAR)) {
                map.put("Title", ad.datafields[0]);
                map.put("Description", ad.datafields[2]);
                map.put(HttpRequest.HEADER_LOCATION, ad.datafields[1]);
                map.put("Starts", ad.datafields[3]);
                map.put("Ends", ad.datafields[4]);
            } else if (ad.type.equals("coupon")) {
                map.put("CouponCode", ad.datafields[0]);
                map.put("CouponType", ad.datafields[1]);
                map.put("Title", ad.datafields[2]);
                map.put("Description", ad.datafields[3]);
                map.put(HttpRequest.HEADER_EXPIRES, ad.datafields[5]);
                map.put("URL", ad.datafields[4]);
            }
            if (map.size() > 0) {
                events.add(getEventAction(nInfo, ad.type, payload, map));
            }
        }
        map = new HashMap();
        map.put("CallLetters", nInfo.stationInfo.callLetters);
        events.add(getEventAction(nInfo, "thumbsup", payload, map));
        Map<String, String> map2 = new HashMap();
        map2.put("CallLetters", nInfo.stationInfo.callLetters);
        events.add(getEventAction(nInfo, "thumbsdown", payload, map2));
        Map<String, String> map3 = new HashMap();
        map3.put("Subject", getShareableSubject(nInfo));
        map3.put("Link", getShareableLink(nInfo));
        events.add(getEventAction(nInfo, "share", payload, map3));
        if (nInfo.getDisplayType() != 1) {
            return events;
        }
        Map<String, String> mapMusicPurch = new HashMap();
        mapMusicPurch.put("Title", nInfo.title);
        mapMusicPurch.put("Artist", nInfo.artist);
        events.add(getEventAction(nInfo, "mp3search", payload, mapMusicPurch));
        return events;
    }

    public EventAction getPrimaryEventAction(NextRadioEventInfo nInfo) {
        try {
            ArrayList<EventAction> actions = getEventActions(nInfo);
            if (actions == null) {
                return null;
            }
            String primary = nInfo.getPrimaryAction();
            Iterator i$ = actions.iterator();
            while (i$.hasNext()) {
                EventAction action = (EventAction) i$.next();
                if (action.getType().equals(primary)) {
                    return action;
                }
            }
            Log.m1934d(TAG, "No action found");
            return null;
        } catch (Exception ex) {
            Log.m1934d(TAG, "Exception in getPrimaryAction: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
