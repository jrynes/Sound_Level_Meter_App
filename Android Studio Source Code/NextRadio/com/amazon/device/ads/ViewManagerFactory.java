package com.amazon.device.ads;

import android.view.ViewGroup;

class ViewManagerFactory {
    private ViewGroup viewGroup;

    ViewManagerFactory() {
    }

    public ViewManagerFactory withViewGroup(ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
        return this;
    }

    public ViewManager createViewManager() {
        return new ViewManager(this.viewGroup);
    }
}
