package com.nextradioapp.nextradio.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class NowPlayingTextView extends LinearLayout {
    public NowPlayingTextView(Context context) {
        super(context);
        addView(inflate(context, 2130903102, null));
    }

    public NowPlayingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NowPlayingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
