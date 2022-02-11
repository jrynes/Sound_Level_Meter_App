package com.nextradioapp.androidSDK.actions;

import android.content.Intent;
import com.nextradioapp.androidSDK.C1136R;
import com.nextradioapp.androidSDK.NextRadioAndroid;
import com.nextradioapp.androidSDK.activities.BarCodeActivity;
import com.nextradioapp.androidSDK.data.schema.Tables.stations;
import com.nextradioapp.androidSDK.interfaces.IActivityManager;
import com.nextradioapp.core.dependencies.IDatabaseAdapter;
import com.nextradioapp.core.objects.ActionPayload;
import com.nextradioapp.core.objects.EventAction;
import org.apache.activemq.transport.stomp.Stomp.Headers.Send;

public class QRCodeEventAction extends EventAction {
    private String mCouponType;
    private String mData;
    private String mExpiration;
    private String mFooter;
    private String mHeader;
    private String mImageURL;
    private IActivityManager myParent;

    public QRCodeEventAction(IDatabaseAdapter dbAdapter, ActionPayload payload, IActivityManager myParent, String data, String couponType, String header, String footer, String expiration, String imageURL) {
        super(dbAdapter, payload);
        this.myParent = myParent;
        this.mData = data;
        this.mCouponType = couponType;
        this.mType = "coupon";
        this.mHeader = header;
        this.mFooter = footer;
        this.mExpiration = expiration;
        this.mImageURL = imageURL;
    }

    public void start_internal(boolean specialExecution) throws Exception {
        Intent intent = new Intent(this.myParent.getCurrentActivity(), BarCodeActivity.class);
        intent.putExtra(MPDbAdapter.KEY_DATA, this.mData);
        intent.putExtra(Send.TYPE, this.mCouponType);
        intent.putExtra("header", this.mHeader);
        intent.putExtra("footer", this.mFooter);
        intent.putExtra("expiration", this.mExpiration);
        intent.putExtra(stations.imageURL, this.mImageURL);
        if (NextRadioAndroid.getInstance().isTablet(this.myParent.getCurrentActivity())) {
            this.myParent.getCurrentActivity().setRequestedOrientation(0);
            this.myParent.getCurrentActivity().startActivity(intent);
            return;
        }
        this.myParent.getCurrentActivity().startActivity(intent);
    }

    public String getActionDescription() {
        return this.myParent.getCurrentApplication().getString(C1136R.string.actions_qr_code);
    }

    public int getReportingAction() {
        return 5;
    }

    public String[] getDetailedMessages() {
        return new String[]{"Save coupon", "in NextRadio"};
    }
}
