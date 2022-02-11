package com.nextradioapp.androidSDK.actions;

import android.content.Intent;
import android.net.Uri;
import com.amazon.device.associates.AssociatesAPI;
import com.amazon.device.associates.AssociatesAPI.Config;
import com.amazon.device.associates.NotInitializedException;
import com.amazon.device.associates.OpenSearchPageRequest;
import com.nextradioapp.androidSDK.C1136R;
import com.nextradioapp.androidSDK.interfaces.IActivityManager;
import com.nextradioapp.core.dependencies.IDatabaseAdapter;
import com.nextradioapp.core.objects.ActionPayload;
import com.nextradioapp.core.objects.EventAction;
import org.apache.activemq.transport.stomp.Stomp;

public class AmazonMP3SearchEventAction extends EventAction {
    private String mArtist;
    private IActivityManager mContext;
    private Intent mIntent;
    private String mTitle;

    public AmazonMP3SearchEventAction(IDatabaseAdapter dbAdapter, ActionPayload payload, IActivityManager myParent, String title, String artist) {
        super(dbAdapter, payload);
        this.mTitle = title;
        this.mArtist = artist;
        this.mContext = myParent;
        this.mType = "mp3search";
    }

    public String getActionDescription() {
        return this.mContext.getCurrentApplication().getString(C1136R.string.actions_amazon);
    }

    public int getReportingAction() {
        return 3;
    }

    protected void start_internal(boolean specialExecute) {
        if (!this.mContext.getCurrentApplication().getString(C1136R.string.setting_mp3_store, new Object[]{"google"}).equals("amazon") || !doAmazon()) {
            doGooglePlay();
        }
    }

    private boolean doAmazon() {
        String APIKey = this.mContext.getCurrentApplication().getString(C1136R.string.setting_amazon_api_key);
        if (APIKey == null || APIKey.length() == 0) {
            return false;
        }
        AssociatesAPI.initialize(new Config(APIKey, this.mContext.getCurrentActivity()));
        try {
            AssociatesAPI.getLinkService().openRetailPage(new OpenSearchPageRequest("music", this.mArtist + " " + this.mTitle));
            return true;
        } catch (NotInitializedException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean doGooglePlay() {
        this.mIntent = new Intent("android.intent.action.VIEW");
        this.mIntent.setType(Stomp.TEXT_PLAIN);
        this.mIntent.setData(Uri.parse(String.format("https://market.android.com/search?q=%1s+%2s&c=music&featured=MUSIC_STORE_SEARCH", new Object[]{this.mArtist, this.mTitle})));
        this.mContext.getCurrentActivity().startActivity(this.mIntent);
        return true;
    }

    public String[] getDetailedMessages() {
        return new String[]{"Bookmark song", "in NextRadio"};
    }
}
