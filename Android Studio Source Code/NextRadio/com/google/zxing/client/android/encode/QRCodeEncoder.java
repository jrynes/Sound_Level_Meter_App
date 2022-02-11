package com.google.zxing.client.android.encode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.AutoScrollHelper;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.Contents;
import com.google.zxing.client.android.Contents.Type;
import com.google.zxing.client.android.Intents.Encode;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.common.BitMatrix;
import com.nextradioapp.androidSDK.C1136R;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.transport.stomp.Stomp;

public final class QRCodeEncoder {
    private static final int BLACK = -16777216;
    private static final String TAG;
    private static final int WHITE = -1;
    private final Activity activity;
    private String contents;
    private final int dimension;
    private String displayContents;
    private BarcodeFormat format;
    private String title;
    private final boolean useVCard;

    static {
        TAG = QRCodeEncoder.class.getSimpleName();
    }

    public QRCodeEncoder(Activity activity, Intent intent, int dimension, boolean useVCard) throws WriterException {
        this.activity = activity;
        this.dimension = dimension;
        this.useVCard = useVCard;
        String action = intent.getAction();
        if (action.equals(Encode.ACTION)) {
            encodeContentsFromZXingIntent(intent);
        } else if (action.equals("android.intent.action.SEND")) {
            encodeContentsFromShareIntent(intent);
        }
    }

    public String getContents() {
        return this.contents;
    }

    public String getDisplayContents() {
        return this.displayContents;
    }

    public String getTitle() {
        return this.title;
    }

    boolean isUseVCard() {
        return this.useVCard;
    }

    private boolean encodeContentsFromZXingIntent(Intent intent) {
        String formatString = intent.getStringExtra(Encode.FORMAT);
        this.format = null;
        if (formatString != null) {
            try {
                this.format = BarcodeFormat.valueOf(formatString);
            } catch (IllegalArgumentException e) {
            }
        }
        if (this.format == null || this.format == BarcodeFormat.QR_CODE) {
            String type = intent.getStringExtra(Encode.TYPE);
            if (type == null || type.length() == 0) {
                return false;
            }
            this.format = BarcodeFormat.QR_CODE;
            encodeQRCodeContents(intent, type);
        } else {
            String data = intent.getStringExtra(Encode.DATA);
            if (data != null && data.length() > 0) {
                this.contents = data;
                this.displayContents = data;
                this.title = this.activity.getString(C1136R.string.contents_text);
            }
        }
        if (this.contents == null || this.contents.length() <= 0) {
            return false;
        }
        return true;
    }

    private void encodeContentsFromShareIntent(Intent intent) throws WriterException {
        if (intent.hasExtra("android.intent.extra.TEXT")) {
            encodeContentsFromShareIntentPlainText(intent);
        } else {
            encodeContentsFromShareIntentDefault(intent);
        }
    }

    private void encodeContentsFromShareIntentPlainText(Intent intent) throws WriterException {
        String theContents = ContactEncoder.trim(intent.getStringExtra("android.intent.extra.TEXT"));
        if (theContents == null || theContents.length() == 0) {
            throw new WriterException("Empty EXTRA_TEXT");
        }
        this.contents = theContents;
        this.format = BarcodeFormat.QR_CODE;
        if (intent.hasExtra("android.intent.extra.SUBJECT")) {
            this.displayContents = intent.getStringExtra("android.intent.extra.SUBJECT");
        } else if (intent.hasExtra("android.intent.extra.TITLE")) {
            this.displayContents = intent.getStringExtra("android.intent.extra.TITLE");
        } else {
            this.displayContents = this.contents;
        }
        this.title = this.activity.getString(C1136R.string.contents_text);
    }

    private void encodeContentsFromShareIntentDefault(Intent intent) throws WriterException {
        this.format = BarcodeFormat.QR_CODE;
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            throw new WriterException("No extras");
        }
        Uri uri = (Uri) bundle.getParcelable("android.intent.extra.STREAM");
        if (uri == null) {
            throw new WriterException("No EXTRA_STREAM");
        }
        try {
            InputStream stream = this.activity.getContentResolver().openInputStream(uri);
            int length = stream.available();
            if (length <= 0) {
                throw new WriterException("Content stream is empty");
            }
            byte[] vcard = new byte[length];
            int bytesRead = stream.read(vcard, 0, length);
            if (bytesRead < length) {
                throw new WriterException("Unable to fully read available bytes from content stream");
            }
            String vcardString = new String(vcard, 0, bytesRead, HttpRequest.CHARSET_UTF8);
            Log.d(TAG, "Encoding share intent content:");
            Log.d(TAG, vcardString);
            ParsedResult parsedResult = ResultParser.parseResult(new Result(vcardString, vcard, null, BarcodeFormat.QR_CODE));
            if (parsedResult instanceof AddressBookParsedResult) {
                encodeQRCodeContents((AddressBookParsedResult) parsedResult);
                if (this.contents == null || this.contents.length() == 0) {
                    throw new WriterException("No content to encode");
                }
                return;
            }
            throw new WriterException("Result was not an address");
        } catch (Throwable ioe) {
            throw new WriterException(ioe);
        }
    }

    private void encodeQRCodeContents(Intent intent, String type) {
        String data;
        if (type.equals(Type.TEXT)) {
            data = intent.getStringExtra(Encode.DATA);
            if (data != null && data.length() > 0) {
                this.contents = data;
                this.displayContents = data;
                this.title = this.activity.getString(C1136R.string.contents_text);
                return;
            }
            return;
        }
        if (type.equals(Type.EMAIL)) {
            data = ContactEncoder.trim(intent.getStringExtra(Encode.DATA));
            if (data != null) {
                this.contents = "mailto:" + data;
                this.displayContents = data;
                this.title = this.activity.getString(C1136R.string.contents_email);
                return;
            }
            return;
        }
        if (type.equals(Type.PHONE)) {
            data = ContactEncoder.trim(intent.getStringExtra(Encode.DATA));
            if (data != null) {
                this.contents = "tel:" + data;
                this.displayContents = PhoneNumberUtils.formatNumber(data);
                this.title = this.activity.getString(C1136R.string.contents_phone);
                return;
            }
            return;
        }
        if (type.equals(Type.SMS)) {
            data = ContactEncoder.trim(intent.getStringExtra(Encode.DATA));
            if (data != null) {
                this.contents = "sms:" + data;
                this.displayContents = PhoneNumberUtils.formatNumber(data);
                this.title = this.activity.getString(C1136R.string.contents_sms);
                return;
            }
            return;
        }
        Bundle bundle;
        if (type.equals(Type.CONTACT)) {
            bundle = intent.getBundleExtra(Encode.DATA);
            if (bundle != null) {
                ContactEncoder mecardEncoder;
                String name = bundle.getString("name");
                String organization = bundle.getString("company");
                String address = bundle.getString("postal");
                Collection<String> phones = new ArrayList(Contents.PHONE_KEYS.length);
                for (String string : Contents.PHONE_KEYS) {
                    phones.add(bundle.getString(string));
                }
                Collection<String> emails = new ArrayList(Contents.EMAIL_KEYS.length);
                for (String string2 : Contents.EMAIL_KEYS) {
                    emails.add(bundle.getString(string2));
                }
                String url = bundle.getString(Contents.URL_KEY);
                String note = bundle.getString(Contents.NOTE_KEY);
                if (this.useVCard) {
                    mecardEncoder = new VCardContactEncoder();
                } else {
                    mecardEncoder = new MECARDContactEncoder();
                }
                String[] encoded = mecardEncoder.encode(Collections.singleton(name), organization, Collections.singleton(address), phones, emails, url, note);
                if (encoded[1].length() > 0) {
                    this.contents = encoded[0];
                    this.displayContents = encoded[1];
                    this.title = this.activity.getString(C1136R.string.contents_contact);
                    return;
                }
                return;
            }
            return;
        }
        if (type.equals(Type.LOCATION)) {
            bundle = intent.getBundleExtra(Encode.DATA);
            if (bundle != null) {
                float latitude = bundle.getFloat("LAT", AutoScrollHelper.NO_MAX);
                float longitude = bundle.getFloat("LONG", AutoScrollHelper.NO_MAX);
                if (latitude != AutoScrollHelper.NO_MAX && longitude != AutoScrollHelper.NO_MAX) {
                    this.contents = "geo:" + latitude + ActiveMQDestination.COMPOSITE_SEPERATOR + longitude;
                    this.displayContents = latitude + Stomp.COMMA + longitude;
                    this.title = this.activity.getString(C1136R.string.contents_location);
                }
            }
        }
    }

    private void encodeQRCodeContents(AddressBookParsedResult contact) {
        String[] encoded = (this.useVCard ? new VCardContactEncoder() : new MECARDContactEncoder()).encode(toIterable(contact.getNames()), contact.getOrg(), toIterable(contact.getAddresses()), toIterable(contact.getPhoneNumbers()), toIterable(contact.getEmails()), contact.getURL(), null);
        if (encoded[1].length() > 0) {
            this.contents = encoded[0];
            this.displayContents = encoded[1];
            this.title = this.activity.getString(C1136R.string.contents_contact);
        }
    }

    private static Iterable<String> toIterable(String[] values) {
        return values == null ? null : Arrays.asList(values);
    }

    public Bitmap encodeAsBitmap() throws WriterException {
        String contentsToEncode = this.contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        BitMatrix result = new MultiFormatWriter().encode(contentsToEncode, this.format, this.dimension, this.dimension, hints);
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[(width * height)];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > '\u00ff') {
                return HttpRequest.CHARSET_UTF8;
            }
        }
        return null;
    }
}
