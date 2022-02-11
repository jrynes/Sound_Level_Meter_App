package com.nextradioapp.androidSDK.adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nextradioapp.androidSDK.C1136R;
import java.util.List;

public class ShareAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mLayoutInflator;
    PackageManager packageManager;
    List<ResolveInfo> resolveInfoList;

    class ViewHolder {
        ImageView imageView;
        TextView name;

        ViewHolder() {
        }
    }

    public ShareAdapter(Context context, List<ResolveInfo> resolveInfos) {
        this.packageManager = context.getPackageManager();
        this.mLayoutInflator = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mContext = context;
        this.resolveInfoList = resolveInfos;
    }

    public int getCount() {
        return this.resolveInfoList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.mLayoutInflator.inflate(C1136R.layout.item_share_layout, null);
            holder.name = (TextView) convertView.findViewById(C1136R.id.name_textView);
            holder.imageView = (ImageView) convertView.findViewById(C1136R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(((ResolveInfo) this.resolveInfoList.get(position)).loadLabel(this.packageManager));
        holder.imageView.setImageDrawable(((ResolveInfo) this.resolveInfoList.get(position)).loadIcon(this.packageManager));
        return convertView;
    }
}
