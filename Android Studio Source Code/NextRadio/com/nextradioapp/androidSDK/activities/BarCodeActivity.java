package com.nextradioapp.androidSDK.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;
import com.nextradioapp.androidSDK.C1136R;
import com.nextradioapp.androidSDK.NextRadioAndroid;
import com.nextradioapp.androidSDK.data.schema.Tables.stations;
import com.nextradioapp.androidSDK.utils.BarcodeImageGenerator;
import com.nextradioapp.core.objects.DateTransform;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers.Send;

public class BarCodeActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C1136R.layout.couponcode);
        if (NextRadioAndroid.getInstance().isTablet(this)) {
            setRequestedOrientation(0);
        }
        try {
            Intent inputIntent = getIntent();
            String data = inputIntent.getExtras().getString(MPDbAdapter.KEY_DATA);
            String type = inputIntent.getExtras().getString(Send.TYPE);
            String imageURL = inputIntent.getExtras().getString(stations.imageURL);
            String expiration = inputIntent.getExtras().getString("expiration");
            String header = inputIntent.getExtras().getString("header");
            String footer = inputIntent.getExtras().getString("footer");
            TextView lblExpires = (TextView) findViewById(C1136R.id.lblExpires);
            TextView lblCodeHeader = (TextView) findViewById(C1136R.id.lblCodeHeader);
            TextView lblCodeLegalText = (TextView) findViewById(C1136R.id.lblCodeLegalText);
            ImageView imgCode = (ImageView) findViewById(C1136R.id.imgCode);
            ImageView imgSecondaryImage = (ImageView) findViewById(C1136R.id.imgSecondaryImage);
            if (expiration != null) {
                if (!expiration.equals(Stomp.EMPTY)) {
                    lblExpires.setText("Expires " + DateFormat.format("MM/dd/yyyy", new DateTransform().read(expiration)));
                    lblCodeHeader.setText(header);
                    lblCodeLegalText.setText(footer);
                    imgCode.setImageBitmap(BarcodeImageGenerator.generateBarcodeBitmap(this, data, type));
                    if (imageURL != null) {
                        if (!imageURL.equals(Stomp.EMPTY)) {
                            ImageLoader.getInstance().displayImage(imageURL, imgSecondaryImage);
                            return;
                        }
                    }
                    imgSecondaryImage.setVisibility(8);
                }
            }
            lblExpires.setVisibility(8);
            lblCodeHeader.setText(header);
            lblCodeLegalText.setText(footer);
            imgCode.setImageBitmap(BarcodeImageGenerator.generateBarcodeBitmap(this, data, type));
            if (imageURL != null) {
                if (imageURL.equals(Stomp.EMPTY)) {
                    ImageLoader.getInstance().displayImage(imageURL, imgSecondaryImage);
                    return;
                }
            }
            imgSecondaryImage.setVisibility(8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
