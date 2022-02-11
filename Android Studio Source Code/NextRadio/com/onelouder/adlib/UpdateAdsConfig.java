package com.onelouder.adlib;

import android.content.Context;
import android.content.Intent;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import javax.xml.parsers.SAXParserFactory;
import org.apache.activemq.transport.stomp.Stomp;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

class UpdateAdsConfig {
    public static final String ACTION_PLACMENTS_AVAILABLE = "com.onelouder.adlib.ACTION_PLACMENTS_AVAILABLE";
    private static final String ENDPOINT_DEV = "https://advrts-dev.s3.amazonaws.com/sdk2/APPNAME.xml";
    private static final String ENDPOINT_PROD = "https://advrts.s3.amazonaws.com/sdk2/APPNAME.xml";
    private static final String ENDPOINT_QA = "https://advrts-qa.s3.amazonaws.com/sdk2/APPNAME.xml";
    private static final String ENDPOINT_STAGE = "https://advrts-stage.s3.amazonaws.com/sdk2/APPNAME.xml";
    private static final String TAG = "UpdateAdsConfig";
    private Context mContext;
    private int mVTry;
    private int mVersion;
    private ArrayList<String> previousPlacements;

    class ConfigHandler extends DefaultHandler {
        StringBuilder all_placements;
        boolean hadInitAdcolony;
        boolean inInit;
        boolean inPlacements;
        boolean inStrings;
        StringBuilder sbToken;

        ConfigHandler() {
            this.all_placements = new StringBuilder();
            this.inPlacements = false;
            this.inInit = false;
            this.hadInitAdcolony = false;
            this.inStrings = false;
        }

        public void endDocument() throws SAXException {
            if (this.all_placements.length() > 0) {
                Diagnostics.m1956v(UpdateAdsConfig.TAG, this.all_placements.toString());
                Preferences.setSimplePref(UpdateAdsConfig.this.mContext, "all-placement-ids", this.all_placements.toString());
            }
            if (UpdateAdsConfig.this.previousPlacements != null) {
                Iterator i$ = UpdateAdsConfig.this.previousPlacements.iterator();
                while (i$.hasNext()) {
                    String oldPlacement = (String) i$.next();
                    Diagnostics.m1956v(UpdateAdsConfig.TAG, "destroying old placement: " + oldPlacement);
                    Preferences.setSimplePref(UpdateAdsConfig.this.mContext, oldPlacement, null);
                }
            }
            if (!this.hadInitAdcolony) {
                Diagnostics.m1956v(UpdateAdsConfig.TAG, "destroying any previous adcolony init");
                Preferences.setSimplePref(UpdateAdsConfig.this.mContext, "adcolonyAppId", null);
                Preferences.setSimplePref(UpdateAdsConfig.this.mContext, "adcolonyZoneIds", null);
                Preferences.setSimplePref(UpdateAdsConfig.this.mContext, "adcolonyIsInstafeed", null);
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void startElement(java.lang.String r33, java.lang.String r34, java.lang.String r35, org.xml.sax.Attributes r36) {
            /*
            r32 = this;
            r0 = r32;
            r0 = r0.inStrings;
            r29 = r0;
            if (r29 == 0) goto L_0x0014;
        L_0x0008:
            r29 = new java.lang.StringBuilder;
            r29.<init>();
            r0 = r29;
            r1 = r32;
            r1.sbToken = r0;
        L_0x0013:
            return;
        L_0x0014:
            r0 = r32;
            r0 = r0.inInit;
            r29 = r0;
            if (r29 == 0) goto L_0x0042;
        L_0x001c:
            r29 = "adcolony";
            r0 = r29;
            r1 = r34;
            r29 = r0.equals(r1);
            if (r29 == 0) goto L_0x0013;
        L_0x0028:
            r0 = r32;
            r0 = com.onelouder.adlib.UpdateAdsConfig.this;
            r29 = r0;
            r29 = r29.mContext;
            r0 = r29;
            r1 = r36;
            com.onelouder.adlib.ProxyAdMarvelView.initAdColony(r0, r1);
            r29 = 1;
            r0 = r29;
            r1 = r32;
            r1.hadInitAdcolony = r0;
            goto L_0x0013;
        L_0x0042:
            r0 = r32;
            r0 = r0.inPlacements;
            r29 = r0;
            if (r29 == 0) goto L_0x0550;
        L_0x004a:
            r29 = "device";
            r0 = r36;
            r1 = r29;
            r6 = r0.getValue(r1);
            if (r6 == 0) goto L_0x0105;
        L_0x0056:
            r29 = "large";
            r0 = r29;
            r29 = r6.equals(r0);
            if (r29 == 0) goto L_0x007f;
        L_0x0060:
            r29 = "UpdateAdsConfig";
            r30 = "device=large found";
            com.onelouder.adlib.Diagnostics.m1951d(r29, r30);
            r0 = r32;
            r0 = com.onelouder.adlib.UpdateAdsConfig.this;
            r29 = r0;
            r29 = r29.mContext;
            r29 = com.onelouder.adlib.Utils.isLargeLayout(r29);
            if (r29 != 0) goto L_0x0105;
        L_0x0077:
            r29 = "UpdateAdsConfig";
            r30 = "device is NOT large";
            com.onelouder.adlib.Diagnostics.m1957w(r29, r30);
            goto L_0x0013;
        L_0x007f:
            r29 = "x-large";
            r0 = r29;
            r29 = r6.equals(r0);
            if (r29 == 0) goto L_0x00a9;
        L_0x0089:
            r29 = "UpdateAdsConfig";
            r30 = "device=x-large found";
            com.onelouder.adlib.Diagnostics.m1951d(r29, r30);
            r0 = r32;
            r0 = com.onelouder.adlib.UpdateAdsConfig.this;
            r29 = r0;
            r29 = r29.mContext;
            r29 = com.onelouder.adlib.Utils.isXLargeLayout(r29);
            if (r29 != 0) goto L_0x0105;
        L_0x00a0:
            r29 = "UpdateAdsConfig";
            r30 = "device is NOT x-large";
            com.onelouder.adlib.Diagnostics.m1957w(r29, r30);
            goto L_0x0013;
        L_0x00a9:
            r29 = "normal";
            r0 = r29;
            r29 = r6.equals(r0);
            if (r29 == 0) goto L_0x00e3;
        L_0x00b3:
            r29 = "UpdateAdsConfig";
            r30 = "device=normal found";
            com.onelouder.adlib.Diagnostics.m1951d(r29, r30);
            r0 = r32;
            r0 = com.onelouder.adlib.UpdateAdsConfig.this;
            r29 = r0;
            r29 = r29.mContext;
            r29 = com.onelouder.adlib.Utils.isLargeLayout(r29);
            if (r29 != 0) goto L_0x00da;
        L_0x00ca:
            r0 = r32;
            r0 = com.onelouder.adlib.UpdateAdsConfig.this;
            r29 = r0;
            r29 = r29.mContext;
            r29 = com.onelouder.adlib.Utils.isXLargeLayout(r29);
            if (r29 == 0) goto L_0x0105;
        L_0x00da:
            r29 = "UpdateAdsConfig";
            r30 = "device is NOT normal";
            com.onelouder.adlib.Diagnostics.m1957w(r29, r30);
            goto L_0x0013;
        L_0x00e3:
            r29 = r6.length();
            if (r29 <= 0) goto L_0x0105;
        L_0x00e9:
            r29 = "UpdateAdsConfig";
            r30 = new java.lang.StringBuilder;
            r30.<init>();
            r31 = "invalid device type, device=";
            r30 = r30.append(r31);
            r0 = r30;
            r30 = r0.append(r6);
            r30 = r30.toString();
            com.onelouder.adlib.Diagnostics.m1957w(r29, r30);
            goto L_0x0013;
        L_0x0105:
            r29 = "density";
            r0 = r36;
            r1 = r29;
            r5 = r0.getValue(r1);
            if (r5 == 0) goto L_0x0205;
        L_0x0111:
            r29 = "default";
            r0 = r29;
            r29 = r5.equals(r0);
            if (r29 == 0) goto L_0x013b;
        L_0x011b:
            r29 = "UpdateAdsConfig";
            r30 = "density=default found";
            com.onelouder.adlib.Diagnostics.m1951d(r29, r30);
            r0 = r32;
            r0 = com.onelouder.adlib.UpdateAdsConfig.this;
            r29 = r0;
            r29 = r29.mContext;
            r29 = com.onelouder.adlib.Utils.isLargeLayout(r29);
            if (r29 != 0) goto L_0x0205;
        L_0x0132:
            r29 = "UpdateAdsConfig";
            r30 = "density is NOT default";
            com.onelouder.adlib.Diagnostics.m1957w(r29, r30);
            goto L_0x0013;
        L_0x013b:
            r29 = "low";
            r0 = r29;
            r29 = r5.equals(r0);
            if (r29 == 0) goto L_0x0165;
        L_0x0145:
            r29 = "UpdateAdsConfig";
            r30 = "density=low found";
            com.onelouder.adlib.Diagnostics.m1951d(r29, r30);
            r0 = r32;
            r0 = com.onelouder.adlib.UpdateAdsConfig.this;
            r29 = r0;
            r29 = r29.mContext;
            r29 = com.onelouder.adlib.Utils.isLowDensity(r29);
            if (r29 != 0) goto L_0x0205;
        L_0x015c:
            r29 = "UpdateAdsConfig";
            r30 = "density is NOT low";
            com.onelouder.adlib.Diagnostics.m1957w(r29, r30);
            goto L_0x0013;
        L_0x0165:
            r29 = "medium";
            r0 = r29;
            r29 = r5.equals(r0);
            if (r29 == 0) goto L_0x018f;
        L_0x016f:
            r29 = "UpdateAdsConfig";
            r30 = "density=medium found";
            com.onelouder.adlib.Diagnostics.m1951d(r29, r30);
            r0 = r32;
            r0 = com.onelouder.adlib.UpdateAdsConfig.this;
            r29 = r0;
            r29 = r29.mContext;
            r29 = com.onelouder.adlib.Utils.isMediumDensity(r29);
            if (r29 != 0) goto L_0x0205;
        L_0x0186:
            r29 = "UpdateAdsConfig";
            r30 = "density is NOT medium";
            com.onelouder.adlib.Diagnostics.m1957w(r29, r30);
            goto L_0x0013;
        L_0x018f:
            r29 = "tv";
            r0 = r29;
            r29 = r5.equals(r0);
            if (r29 == 0) goto L_0x01b9;
        L_0x0199:
            r29 = "UpdateAdsConfig";
            r30 = "density=tv found";
            com.onelouder.adlib.Diagnostics.m1951d(r29, r30);
            r0 = r32;
            r0 = com.onelouder.adlib.UpdateAdsConfig.this;
            r29 = r0;
            r29 = r29.mContext;
            r29 = com.onelouder.adlib.Utils.isTvDensity(r29);
            if (r29 != 0) goto L_0x0205;
        L_0x01b0:
            r29 = "UpdateAdsConfig";
            r30 = "density is NOT tv";
            com.onelouder.adlib.Diagnostics.m1957w(r29, r30);
            goto L_0x0013;
        L_0x01b9:
            r29 = "high";
            r0 = r29;
            r29 = r5.equals(r0);
            if (r29 == 0) goto L_0x01e3;
        L_0x01c3:
            r29 = "UpdateAdsConfig";
            r30 = "density=high found";
            com.onelouder.adlib.Diagnostics.m1951d(r29, r30);
            r0 = r32;
            r0 = com.onelouder.adlib.UpdateAdsConfig.this;
            r29 = r0;
            r29 = r29.mContext;
            r29 = com.onelouder.adlib.Utils.isHighDensity(r29);
            if (r29 != 0) goto L_0x0205;
        L_0x01da:
            r29 = "UpdateAdsConfig";
            r30 = "density is NOT high";
            com.onelouder.adlib.Diagnostics.m1957w(r29, r30);
            goto L_0x0013;
        L_0x01e3:
            r29 = r5.length();
            if (r29 <= 0) goto L_0x0205;
        L_0x01e9:
            r29 = "UpdateAdsConfig";
            r30 = new java.lang.StringBuilder;
            r30.<init>();
            r31 = "invalid density type, density=";
            r30 = r30.append(r31);
            r0 = r30;
            r30 = r0.append(r5);
            r30 = r30.toString();
            com.onelouder.adlib.Diagnostics.m1957w(r29, r30);
            goto L_0x0013;
        L_0x0205:
            r9 = new java.lang.StringBuilder;
            r0 = r34;
            r9.<init>(r0);
            r29 = "-";
            r0 = r29;
            r9.append(r0);
            r28 = new java.lang.StringBuilder;
            r28.<init>();
            r29 = "type";
            r0 = r36;
            r1 = r29;
            r27 = r0.getValue(r1);
            if (r27 != 0) goto L_0x022d;
        L_0x0224:
            r29 = "UpdateAdsConfig";
            r30 = "type not definfed.";
            com.onelouder.adlib.Diagnostics.m1957w(r29, r30);
            goto L_0x0013;
        L_0x022d:
            r29 = "banner";
            r0 = r27;
            r1 = r29;
            r29 = r0.equals(r1);
            if (r29 != 0) goto L_0x0251;
        L_0x0239:
            r29 = "square";
            r0 = r27;
            r1 = r29;
            r29 = r0.equals(r1);
            if (r29 != 0) goto L_0x0251;
        L_0x0245:
            r29 = "interstitial";
            r0 = r27;
            r1 = r29;
            r29 = r0.equals(r1);
            if (r29 == 0) goto L_0x02c4;
        L_0x0251:
            r0 = r27;
            r9.append(r0);
            if (r6 == 0) goto L_0x0268;
        L_0x0258:
            r29 = r6.length();
            if (r29 <= 0) goto L_0x0268;
        L_0x025e:
            r29 = "device=";
            r28.append(r29);
            r0 = r28;
            r0.append(r6);
        L_0x0268:
            if (r5 == 0) goto L_0x0285;
        L_0x026a:
            r29 = r5.length();
            if (r29 <= 0) goto L_0x0285;
        L_0x0270:
            r29 = r28.length();
            if (r29 <= 0) goto L_0x027b;
        L_0x0276:
            r29 = ",";
            r28.append(r29);
        L_0x027b:
            r29 = "density=";
            r28.append(r29);
            r0 = r28;
            r0.append(r5);
        L_0x0285:
            r29 = "pubid";
            r0 = r36;
            r1 = r29;
            r15 = r0.getValue(r1);
            r29 = "siteid";
            r0 = r36;
            r1 = r29;
            r21 = r0.getValue(r1);
            r29 = "network";
            r0 = r36;
            r1 = r29;
            r11 = r0.getValue(r1);
            if (r15 == 0) goto L_0x030d;
        L_0x02a5:
            if (r21 == 0) goto L_0x030d;
        L_0x02a7:
            if (r11 == 0) goto L_0x030d;
        L_0x02a9:
            r29 = r15.length();
            if (r29 == 0) goto L_0x02bb;
        L_0x02af:
            r29 = r21.length();
            if (r29 == 0) goto L_0x02bb;
        L_0x02b5:
            r29 = r11.length();
            if (r29 != 0) goto L_0x02e2;
        L_0x02bb:
            r29 = "UpdateAdsConfig";
            r30 = "pubid/siteid/network not fully specified.";
            com.onelouder.adlib.Diagnostics.m1957w(r29, r30);
            goto L_0x0013;
        L_0x02c4:
            r29 = "UpdateAdsConfig";
            r30 = new java.lang.StringBuilder;
            r30.<init>();
            r31 = "unsupported type value speicified, type=";
            r30 = r30.append(r31);
            r0 = r30;
            r1 = r27;
            r30 = r0.append(r1);
            r30 = r30.toString();
            com.onelouder.adlib.Diagnostics.m1957w(r29, r30);
            goto L_0x0013;
        L_0x02e2:
            r29 = r28.length();
            if (r29 <= 0) goto L_0x02ed;
        L_0x02e8:
            r29 = ",";
            r28.append(r29);
        L_0x02ed:
            r29 = "pubid=";
            r28.append(r29);
            r0 = r28;
            r0.append(r15);
            r29 = ",siteid=";
            r28.append(r29);
            r0 = r28;
            r1 = r21;
            r0.append(r1);
            r29 = ",network=";
            r28.append(r29);
            r0 = r28;
            r0.append(r11);
        L_0x030d:
            r29 = "rollover";
            r0 = r36;
            r1 = r29;
            r19 = r0.getValue(r1);
            if (r19 == 0) goto L_0x0325;
        L_0x0319:
            r29 = ",rollover=";
            r28.append(r29);
            r0 = r28;
            r1 = r19;
            r0.append(r1);
        L_0x0325:
            r29 = "close_button";
            r0 = r36;
            r1 = r29;
            r4 = r0.getValue(r1);
            if (r4 == 0) goto L_0x033b;
        L_0x0331:
            r29 = ",close_button=";
            r28.append(r29);
            r0 = r28;
            r0.append(r4);
        L_0x033b:
            r29 = "onclosead";
            r0 = r36;
            r1 = r29;
            r12 = r0.getValue(r1);
            if (r12 == 0) goto L_0x0351;
        L_0x0347:
            r29 = ",onclosead=";
            r28.append(r29);
            r0 = r28;
            r0.append(r12);
        L_0x0351:
            r29 = "str_title";
            r0 = r36;
            r1 = r29;
            r26 = r0.getValue(r1);
            if (r26 == 0) goto L_0x0369;
        L_0x035d:
            r29 = ",str_title=";
            r28.append(r29);
            r0 = r28;
            r1 = r26;
            r0.append(r1);
        L_0x0369:
            r29 = "str_text";
            r0 = r36;
            r1 = r29;
            r25 = r0.getValue(r1);
            if (r25 == 0) goto L_0x0381;
        L_0x0375:
            r29 = ",str_text=";
            r28.append(r29);
            r0 = r28;
            r1 = r25;
            r0.append(r1);
        L_0x0381:
            r29 = "str_ok";
            r0 = r36;
            r1 = r29;
            r24 = r0.getValue(r1);
            if (r24 == 0) goto L_0x0399;
        L_0x038d:
            r29 = ",str_ok=";
            r28.append(r29);
            r0 = r28;
            r1 = r24;
            r0.append(r1);
        L_0x0399:
            r29 = "str_cancel";
            r0 = r36;
            r1 = r29;
            r22 = r0.getValue(r1);
            if (r22 == 0) goto L_0x03b1;
        L_0x03a5:
            r29 = ",str_cancel=";
            r28.append(r29);
            r0 = r28;
            r1 = r22;
            r0.append(r1);
        L_0x03b1:
            r29 = "str_nojoy";
            r0 = r36;
            r1 = r29;
            r23 = r0.getValue(r1);
            if (r22 == 0) goto L_0x03c9;
        L_0x03bd:
            r29 = ",str_nojoy=";
            r28.append(r29);
            r0 = r28;
            r1 = r23;
            r0.append(r1);
        L_0x03c9:
            r29 = "onetime";
            r0 = r36;
            r1 = r29;
            r13 = r0.getValue(r1);
            if (r13 == 0) goto L_0x03df;
        L_0x03d5:
            r29 = ",onetime=";
            r28.append(r29);
            r0 = r28;
            r0.append(r13);
        L_0x03df:
            r29 = "recycle";
            r0 = r36;
            r1 = r29;
            r16 = r0.getValue(r1);
            if (r16 == 0) goto L_0x03f7;
        L_0x03eb:
            r29 = ",recycle=";
            r28.append(r29);
            r0 = r28;
            r1 = r16;
            r0.append(r1);
        L_0x03f7:
            r29 = "clone";
            r0 = r36;
            r1 = r29;
            r3 = r0.getValue(r1);
            if (r3 == 0) goto L_0x040d;
        L_0x0403:
            r29 = ",clone=";
            r28.append(r29);
            r0 = r28;
            r0.append(r3);
        L_0x040d:
            r29 = "click_redirect";
            r0 = r36;
            r1 = r29;
            r2 = r0.getValue(r1);
            if (r2 == 0) goto L_0x0423;
        L_0x0419:
            r29 = ",click_redirect=";
            r28.append(r29);
            r0 = r28;
            r0.append(r2);
        L_0x0423:
            r29 = "refresh_rate";
            r0 = r36;
            r1 = r29;
            r17 = r0.getValue(r1);
            if (r17 == 0) goto L_0x043b;
        L_0x042f:
            r29 = ",refresh_rate=";
            r28.append(r29);
            r0 = r28;
            r1 = r17;
            r0.append(r1);
        L_0x043b:
            r29 = "reset";
            r0 = r36;
            r1 = r29;
            r18 = r0.getValue(r1);
            if (r18 == 0) goto L_0x0453;
        L_0x0447:
            r29 = ",reset=";
            r28.append(r29);
            r0 = r28;
            r1 = r18;
            r0.append(r1);
        L_0x0453:
            r29 = "interstitial";
            r0 = r27;
            r1 = r29;
            r29 = r0.equals(r1);
            if (r29 == 0) goto L_0x04bb;
        L_0x045f:
            r29 = "sc";
            r0 = r36;
            r1 = r29;
            r20 = r0.getValue(r1);
            if (r20 == 0) goto L_0x0477;
        L_0x046b:
            r29 = ",sc=";
            r28.append(r29);
            r0 = r28;
            r1 = r20;
            r0.append(r1);
        L_0x0477:
            r29 = "freq";
            r0 = r36;
            r1 = r29;
            r7 = r0.getValue(r1);
            if (r7 == 0) goto L_0x048d;
        L_0x0483:
            r29 = ",freq=";
            r28.append(r29);
            r0 = r28;
            r0.append(r7);
        L_0x048d:
            r29 = "int_type";
            r0 = r36;
            r1 = r29;
            r8 = r0.getValue(r1);
            if (r8 == 0) goto L_0x04a3;
        L_0x0499:
            r29 = ",int_type=";
            r28.append(r29);
            r0 = r28;
            r0.append(r8);
        L_0x04a3:
            r29 = "reset_on_cancel";
            r0 = r36;
            r1 = r29;
            r18 = r0.getValue(r1);
            if (r18 == 0) goto L_0x04bb;
        L_0x04af:
            r29 = ",reset_on_cancel=";
            r28.append(r29);
            r0 = r28;
            r1 = r18;
            r0.append(r1);
        L_0x04bb:
            r0 = r32;
            r0 = r0.all_placements;
            r29 = r0;
            r29 = r29.length();
            if (r29 <= 0) goto L_0x04d2;
        L_0x04c7:
            r0 = r32;
            r0 = r0.all_placements;
            r29 = r0;
            r30 = ",";
            r29.append(r30);
        L_0x04d2:
            r29 = new java.lang.StringBuilder;
            r29.<init>();
            r0 = r29;
            r1 = r34;
            r29 = r0.append(r1);
            r30 = "-";
            r29 = r29.append(r30);
            r0 = r29;
            r1 = r27;
            r29 = r0.append(r1);
            r14 = r29.toString();
            r0 = r32;
            r0 = r0.all_placements;
            r29 = r0;
            r0 = r29;
            r0.append(r14);
            r0 = r32;
            r0 = com.onelouder.adlib.UpdateAdsConfig.this;
            r29 = r0;
            r29 = r29.previousPlacements;
            if (r29 == 0) goto L_0x0517;
        L_0x0508:
            r0 = r32;
            r0 = com.onelouder.adlib.UpdateAdsConfig.this;
            r29 = r0;
            r29 = r29.previousPlacements;
            r0 = r29;
            r0.remove(r14);
        L_0x0517:
            r29 = "UpdateAdsConfig";
            r30 = new java.lang.StringBuilder;
            r30.<init>();
            r0 = r30;
            r30 = r0.append(r9);
            r31 = "=";
            r30 = r30.append(r31);
            r0 = r30;
            r1 = r28;
            r30 = r0.append(r1);
            r30 = r30.toString();
            com.onelouder.adlib.Diagnostics.m1951d(r29, r30);
            r0 = r32;
            r0 = com.onelouder.adlib.UpdateAdsConfig.this;
            r29 = r0;
            r29 = r29.mContext;
            r30 = r9.toString();
            r31 = r28.toString();
            com.onelouder.adlib.Preferences.setSimplePref(r29, r30, r31);
            goto L_0x0013;
        L_0x0550:
            r29 = "placements";
            r0 = r34;
            r1 = r29;
            r29 = r0.equals(r1);
            if (r29 == 0) goto L_0x0566;
        L_0x055c:
            r29 = 1;
            r0 = r29;
            r1 = r32;
            r1.inPlacements = r0;
            goto L_0x0013;
        L_0x0566:
            r29 = "strings";
            r0 = r34;
            r1 = r29;
            r29 = r0.equals(r1);
            if (r29 == 0) goto L_0x059e;
        L_0x0572:
            r29 = "lang";
            r0 = r36;
            r1 = r29;
            r10 = r0.getValue(r1);
            if (r10 == 0) goto L_0x0594;
        L_0x057e:
            r0 = r32;
            r0 = com.onelouder.adlib.UpdateAdsConfig.this;
            r29 = r0;
            r29 = r29.mContext;
            r29 = com.onelouder.adlib.Utils.getLanguage(r29);
            r0 = r29;
            r29 = r0.equals(r10);
            if (r29 == 0) goto L_0x0013;
        L_0x0594:
            r29 = 1;
            r0 = r29;
            r1 = r32;
            r1.inStrings = r0;
            goto L_0x0013;
        L_0x059e:
            r29 = "refresh_rate";
            r0 = r34;
            r1 = r29;
            r29 = r0.equals(r1);
            if (r29 == 0) goto L_0x05b7;
        L_0x05aa:
            r29 = new java.lang.StringBuilder;
            r29.<init>();
            r0 = r29;
            r1 = r32;
            r1.sbToken = r0;
            goto L_0x0013;
        L_0x05b7:
            r29 = "ads_enabled";
            r0 = r34;
            r1 = r29;
            r29 = r0.equals(r1);
            if (r29 == 0) goto L_0x05d0;
        L_0x05c3:
            r29 = new java.lang.StringBuilder;
            r29.<init>();
            r0 = r29;
            r1 = r32;
            r1.sbToken = r0;
            goto L_0x0013;
        L_0x05d0:
            r29 = "mss_callback";
            r0 = r34;
            r1 = r29;
            r29 = r0.equals(r1);
            if (r29 == 0) goto L_0x05e9;
        L_0x05dc:
            r29 = new java.lang.StringBuilder;
            r29.<init>();
            r0 = r29;
            r1 = r32;
            r1.sbToken = r0;
            goto L_0x0013;
        L_0x05e9:
            r29 = "close_button";
            r0 = r34;
            r1 = r29;
            r29 = r0.equals(r1);
            if (r29 == 0) goto L_0x0602;
        L_0x05f5:
            r29 = new java.lang.StringBuilder;
            r29.<init>();
            r0 = r29;
            r1 = r32;
            r1.sbToken = r0;
            goto L_0x0013;
        L_0x0602:
            r29 = "ola_id";
            r0 = r34;
            r1 = r29;
            r29 = r0.equals(r1);
            if (r29 == 0) goto L_0x061b;
        L_0x060e:
            r29 = new java.lang.StringBuilder;
            r29.<init>();
            r0 = r29;
            r1 = r32;
            r1.sbToken = r0;
            goto L_0x0013;
        L_0x061b:
            r29 = "init";
            r0 = r34;
            r1 = r29;
            r29 = r0.equals(r1);
            if (r29 == 0) goto L_0x0013;
        L_0x0627:
            r29 = 1;
            r0 = r29;
            r1 = r32;
            r1.inInit = r0;
            goto L_0x0013;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.onelouder.adlib.UpdateAdsConfig.ConfigHandler.startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes):void");
        }

        public void endElement(String uri, String name, String qName) throws SAXException {
            if (this.inPlacements && name.equals("placements")) {
                this.inPlacements = false;
            } else if (this.inStrings && name.equals("strings")) {
                this.inStrings = false;
            } else if (this.inInit && name.equals("init")) {
                this.inInit = false;
            }
            if (this.sbToken != null) {
                if (this.inStrings) {
                    if (Diagnostics.getInstance().isEnabled(4)) {
                        Diagnostics.m1952e(UpdateAdsConfig.TAG, "string=" + this.sbToken.toString());
                    }
                    Preferences.setSimplePref(UpdateAdsConfig.this.mContext, "1Louder-string-" + name, this.sbToken.toString());
                } else if (name.equals("refresh_rate")) {
                    if (Diagnostics.getInstance().isEnabled(4)) {
                        Diagnostics.m1952e(UpdateAdsConfig.TAG, "refresh_rate=" + this.sbToken.toString());
                    }
                    try {
                        Preferences.setSimplePref(UpdateAdsConfig.this.mContext, "ads_refresh_rate", Integer.parseInt(this.sbToken.toString()));
                    } catch (Exception e) {
                    }
                } else if (name.equals("ads_enabled")) {
                    if (Diagnostics.getInstance().isEnabled(4)) {
                        Diagnostics.m1952e(UpdateAdsConfig.TAG, "ads_enabled=" + this.sbToken.toString());
                    }
                    Preferences.setSimplePref(UpdateAdsConfig.this.mContext, "ads_enabled", this.sbToken.toString().equals(Stomp.TRUE));
                } else if (name.equals("close_button")) {
                    if (Diagnostics.getInstance().isEnabled(4)) {
                        Diagnostics.m1952e(UpdateAdsConfig.TAG, "close_button=" + this.sbToken.toString());
                    }
                    Preferences.setSimplePref(UpdateAdsConfig.this.mContext, "ads_close_button", this.sbToken.toString());
                } else if (name.equals("ola_id")) {
                    if (Diagnostics.getInstance().isEnabled(4)) {
                        Diagnostics.m1952e(UpdateAdsConfig.TAG, "ola_id=" + this.sbToken.toString());
                    }
                    Preferences.setSimplePref(UpdateAdsConfig.this.mContext, "ola_id", this.sbToken.toString());
                } else if (name.equals("mss_callback")) {
                    try {
                        if (Diagnostics.getInstance().isEnabled(4)) {
                            Diagnostics.m1952e(UpdateAdsConfig.TAG, "mss_callback=" + this.sbToken.toString());
                        }
                        long nextUpdate = Preferences.getAdTargetingUpdate(UpdateAdsConfig.this.mContext);
                        long mssUpdate = Utils.ParseTimestamp(this.sbToken.toString()).getTime();
                        if (nextUpdate > 0) {
                            if (mssUpdate > 0 && mssUpdate > System.currentTimeMillis() && mssUpdate < nextUpdate) {
                                Preferences.setAdTargetingUpdate(UpdateAdsConfig.this.mContext, mssUpdate);
                            }
                        } else if (mssUpdate > 0) {
                            if (mssUpdate > System.currentTimeMillis()) {
                                Preferences.setAdTargetingUpdate(UpdateAdsConfig.this.mContext, mssUpdate);
                            }
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            this.sbToken = null;
        }

        public void characters(char[] ch, int start, int length) {
            if (this.sbToken != null) {
                this.sbToken.append(ch, start, length);
            }
        }
    }

    class ConfigRequest extends ServerBase {
        public ConfigRequest(String url) {
            this.mUrl = url;
            this.mZeroBytesAllowed = false;
            this.mDo_post = false;
        }

        protected String TAG() {
            return "ConfigRequest";
        }

        public boolean ProcessResponse() throws EOFException {
            try {
                InputStream stm = new ByteArrayInputStream(this.data);
                InputStream is = stm;
                if (this.mResponseGzipped) {
                    is = new GZIPInputStream(stm);
                }
                XMLReader xr = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
                xr.setContentHandler(new ConfigHandler());
                xr.parse(new InputSource(is));
                stm.close();
                Preferences.setSimplePref(UpdateAdsConfig.this.mContext, "last-adconfig-update", System.currentTimeMillis());
                PlacementManager.getInstance().resetPlacementCache(UpdateAdsConfig.this.mContext);
                UpdateAdsConfig.this.mContext.sendBroadcast(new Intent(UpdateAdsConfig.ACTION_PLACMENTS_AVAILABLE));
            } catch (Throwable e) {
                Diagnostics.m1953e(TAG(), e);
            }
            return true;
        }

        public void ProcessFailure() {
            Diagnostics.m1957w(UpdateAdsConfig.TAG, "onError (" + this.mResponseCode + ") " + this.mResponse);
            Preferences.setSimplePref(UpdateAdsConfig.this.mContext, "last-adconfig-update", 0);
            if (UpdateAdsConfig.this.mVTry > 1) {
                UpdateAdsConfig.this.mVTry = UpdateAdsConfig.this.mVTry - 1;
                UpdateAdsConfig.this.tryUpdateConfig(UpdateAdsConfig.this.mContext, Preferences.getSimplePref(UpdateAdsConfig.this.mContext, "ads-product-name", Stomp.EMPTY));
            }
        }
    }

    public UpdateAdsConfig(Context context, String appname) {
        this.mVersion = 4;
        this.mVTry = this.mVersion;
        this.mContext = context;
        String allIds = Preferences.getSimplePref(context, "all-placement-ids", Stomp.EMPTY);
        if (allIds.length() > 0) {
            this.previousPlacements = new ArrayList(Arrays.asList(allIds.split(Stomp.COMMA)));
        }
        tryUpdateConfig(context, appname);
    }

    private void tryUpdateConfig(Context context, String appname) {
        String url = null;
        if (Preferences.isProdEnv(context)) {
            url = ENDPOINT_PROD;
        } else if (Preferences.isQaEnv(context)) {
            url = ENDPOINT_QA;
        } else if (Preferences.isDevEnv(context)) {
            url = ENDPOINT_DEV;
        } else if (Preferences.isStageEnv(context)) {
            url = ENDPOINT_STAGE;
        }
        if (url != null) {
            StringBuilder sb = new StringBuilder(appname);
            if (this.mVTry > 1) {
                sb.append("-v");
                sb.append(this.mVTry);
            }
            new Thread(new ConfigRequest(url.replace("APPNAME", sb.toString()))).start();
        }
    }
}
