package com.nextradioapp.androidSDK.actions;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Parcelable;
import android.provider.MediaStore.Images.Media;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import com.nextradioapp.androidSDK.C1136R;
import com.nextradioapp.androidSDK.adapters.ShareAdapter;
import com.nextradioapp.androidSDK.interfaces.IActivityManager;
import com.nextradioapp.core.dependencies.IDatabaseAdapter;
import com.nextradioapp.core.objects.ActionPayload;
import com.nextradioapp.core.objects.EventAction;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.activemq.transport.stomp.Stomp;

public class ShareEventAction extends EventAction {
    private static final String TAG = "ShareEventAction";
    public static OnStorageRequestPermission mCallBack;
    String dataLink;
    File file;
    String logoLinkHiRes;
    IActivityManager myParent;
    String subject;
    List<ResolveInfo> targetShareIntents;
    private Uri urlPath;

    /* renamed from: com.nextradioapp.androidSDK.actions.ShareEventAction.1 */
    class C11381 implements Runnable {
        final /* synthetic */ GridView val$gridView;
        final /* synthetic */ View val$view;

        /* renamed from: com.nextradioapp.androidSDK.actions.ShareEventAction.1.1 */
        class C11371 implements OnItemClickListener {
            final /* synthetic */ Dialog val$mBottomSheetDialog;

            C11371(Dialog dialog) {
                this.val$mBottomSheetDialog = dialog;
            }

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                this.val$mBottomSheetDialog.dismiss();
                String packageName = ((ResolveInfo) ShareEventAction.this.targetShareIntents.get(position)).activityInfo.packageName;
                String labelName = ((ResolveInfo) ShareEventAction.this.targetShareIntents.get(position)).activityInfo.name;
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, labelName));
                intent.setAction("android.intent.action.SEND");
                intent.setType(Stomp.TEXT_PLAIN);
                if (!packageName.contains("com.instagram.android")) {
                    intent.putExtra("android.intent.extra.SUBJECT", ShareEventAction.this.subject);
                    intent.putExtra("android.intent.extra.TEXT", ShareEventAction.this.dataLink);
                    intent.setPackage(packageName);
                    ShareEventAction.this.myParent.getCurrentActivity().startActivity(intent);
                } else if (ContextCompat.checkSelfPermission(ShareEventAction.this.myParent.getCurrentActivity(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                    intent.putExtra("android.intent.extra.TEXT", ShareEventAction.this.subject);
                    intent.putExtra("android.intent.extra.SUBJECT", ShareEventAction.this.dataLink);
                    try {
                        ShareEventAction.this.getFilePath(ShareEventAction.this.logoLinkHiRes);
                        if (ShareEventAction.this.urlPath != null) {
                            intent.putExtra("android.intent.extra.STREAM", ShareEventAction.this.urlPath);
                        }
                        intent.setType("image/*");
                    } catch (Exception e) {
                        Log.e(ShareEventAction.TAG, "Error:" + e);
                        e.printStackTrace();
                    }
                    intent.setPackage(packageName);
                    ShareEventAction.this.myParent.getCurrentActivity().startActivity(intent);
                } else {
                    ShareEventAction.mCallBack.onPermissionRequired();
                }
            }
        }

        C11381(View view, GridView gridView) {
            this.val$view = view;
            this.val$gridView = gridView;
        }

        public void run() {
            Dialog mBottomSheetDialog = new Dialog(ShareEventAction.this.myParent.getCurrentActivity(), C1136R.style.MaterialDialogSheet);
            mBottomSheetDialog.setTitle("share");
            mBottomSheetDialog.setContentView(this.val$view);
            mBottomSheetDialog.setCancelable(true);
            mBottomSheetDialog.getWindow().setLayout(-1, -2);
            mBottomSheetDialog.getWindow().setGravity(80);
            mBottomSheetDialog.show();
            this.val$gridView.setOnItemClickListener(new C11371(mBottomSheetDialog));
        }
    }

    /* renamed from: com.nextradioapp.androidSDK.actions.ShareEventAction.2 */
    class C11392 extends SimpleImageLoadingListener {
        C11392() {
        }

        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            try {
                ShareEventAction.this.urlPath = ShareEventAction.this.getImageUri(loadedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnStorageRequestPermission {
        void onPermissionRequired();
    }

    public ShareEventAction(IDatabaseAdapter dbAdapter, ActionPayload payload, IActivityManager myParent, String subject, String link, String logoLinkHiRes) {
        super(dbAdapter, payload);
        this.file = null;
        this.subject = subject;
        this.dataLink = link;
        this.myParent = myParent;
        this.logoLinkHiRes = logoLinkHiRes;
        this.mType = "share";
    }

    public void start_internal(boolean specialExecution) throws Exception {
        if (VERSION.SDK_INT < 23) {
            onShareActionData();
        } else if (specialExecution) {
            shareInstagramData();
        } else {
            onCustomShareActionData();
        }
    }

    public String getActionDescription() {
        return this.myParent.getCurrentApplication().getString(C1136R.string.actions_share);
    }

    public int getReportingAction() {
        return 6;
    }

    public void onCustomShareActionData() {
        try {
            String previousLabelName = Stomp.EMPTY;
            String previousPkgName = Stomp.EMPTY;
            View view = this.myParent.getCurrentActivity().getLayoutInflater().inflate(C1136R.layout.bottom_sheet, null);
            GridView gridView = (GridView) view.findViewById(C1136R.id.gridView);
            this.targetShareIntents = new ArrayList();
            Intent shareIntent = new Intent();
            shareIntent.setType("image/*");
            PackageManager packageManager = this.myParent.getCurrentActivity().getPackageManager();
            List<ResolveInfo> resInfoList = packageManager.queryIntentActivities(shareIntent, 0);
            if (!resInfoList.isEmpty()) {
                for (ResolveInfo resInfo : resInfoList) {
                    String packageName = resInfo.activityInfo.packageName;
                    String labelName = resInfo.loadLabel(packageManager).toString();
                    if (packageName.contains("android.email") || ((labelName.contains("Gmail") && packageName.contains("com.google.android.gm")) || packageName.contains("com.google.android.apps.plus") || ((labelName.contains("Instagram") && packageName.contains("com.instagram.android")) || ((labelName.contains("Facebook") && packageName.contains("com.facebook.katana")) || packageName.contains("mms") || (labelName.contains("Tweet") && packageName.contains("com.twitter.android")))))) {
                        if (!(labelName.equals(previousLabelName) || previousPkgName.equals(packageName))) {
                            this.targetShareIntents.add(resInfo);
                        }
                        previousLabelName = labelName;
                        previousPkgName = packageName;
                    }
                }
            }
            gridView.setAdapter(new ShareAdapter(this.myParent.getCurrentActivity(), this.targetShareIntents));
            this.myParent.getCurrentActivity().runOnUiThread(new C11381(view, gridView));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Uri getFilePath(String imageUrl) {
        ImageLoader.getInstance().loadImage(imageUrl, new C11392());
        return this.urlPath;
    }

    public Uri getImageUri(Bitmap inImage) {
        inImage.compress(CompressFormat.JPEG, 100, new ByteArrayOutputStream());
        return Uri.parse(Media.insertImage(this.myParent.getCurrentActivity().getContentResolver(), inImage, "share_file_path", null));
    }

    private void shareInstagramData() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setType(Stomp.TEXT_PLAIN);
        intent.putExtra("android.intent.extra.TEXT", this.subject);
        intent.putExtra("android.intent.extra.SUBJECT", this.dataLink);
        try {
            getFilePath(this.logoLinkHiRes);
            if (this.urlPath != null) {
                intent.putExtra("android.intent.extra.STREAM", this.urlPath);
            }
            intent.setType("image/*");
            intent.setPackage("com.instagram.android");
            this.myParent.getCurrentActivity().startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error:" + e);
            e.printStackTrace();
        }
    }

    public static void setCallBack(OnStorageRequestPermission onStorageRequestPermission) {
        mCallBack = onStorageRequestPermission;
    }

    public void onShareActionData() {
        List<Intent> targetShareIntents = new ArrayList();
        Intent shareIntent = new Intent();
        shareIntent.setAction("android.intent.action.SEND");
        shareIntent.setType("image/*");
        List<ResolveInfo> resolveInfoList = this.myParent.getCurrentActivity().getPackageManager().queryIntentActivities(shareIntent, 0);
        if (!resolveInfoList.isEmpty()) {
            for (ResolveInfo resInfo : resolveInfoList) {
                String packageName = resInfo.activityInfo.packageName;
                String labelName = resInfo.loadLabel(this.myParent.getCurrentActivity().getPackageManager()).toString();
                if (packageName.contains("android.email") || ((labelName.contains("Gmail") && packageName.contains("com.google.android.gm")) || packageName.contains("com.google.android.apps.plus") || ((labelName.contains("Instagram") && packageName.contains("com.instagram.android")) || ((labelName.contains("Facebook") && packageName.contains("com.facebook.katana")) || packageName.contains("mms") || (labelName.contains("Tweet") && packageName.contains("com.twitter.android")))))) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.setAction("android.intent.action.SEND");
                    intent.setType(Stomp.TEXT_PLAIN);
                    if (packageName.contains("com.instagram.android")) {
                        intent.putExtra("android.intent.extra.TEXT", this.subject);
                        intent.putExtra("android.intent.extra.SUBJECT", this.dataLink);
                        try {
                            getFilePath(this.logoLinkHiRes);
                            intent.putExtra("android.intent.extra.STREAM", this.urlPath);
                            intent.setType("image/*");
                        } catch (Exception e) {
                            Log.e(TAG, "Error:" + e);
                            e.printStackTrace();
                        }
                    } else {
                        intent.putExtra("android.intent.extra.SUBJECT", this.subject);
                        intent.putExtra("android.intent.extra.TEXT", this.dataLink);
                    }
                    intent.setPackage(packageName);
                    targetShareIntents.add(intent);
                }
            }
            if (!targetShareIntents.isEmpty()) {
                Intent chooserIntent = Intent.createChooser((Intent) targetShareIntents.remove(0), this.myParent.getCurrentActivity().getResources().getString(C1136R.string.tell_friends_via) + "...");
                chooserIntent.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) targetShareIntents.toArray(new Parcelable[0]));
                this.myParent.getCurrentActivity().startActivity(chooserIntent);
            }
        }
    }
}
