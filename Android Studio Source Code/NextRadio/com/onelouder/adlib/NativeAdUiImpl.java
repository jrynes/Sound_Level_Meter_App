package com.onelouder.adlib;

import android.view.ViewGroup;
import android.widget.TextView;

public interface NativeAdUiImpl {
    ViewGroup getSponsorIconViewGroup();

    TextView getTextView(int i);
}
