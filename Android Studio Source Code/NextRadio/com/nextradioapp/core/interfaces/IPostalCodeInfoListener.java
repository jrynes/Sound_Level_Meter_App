package com.nextradioapp.core.interfaces;

import com.nextradioapp.core.objects.PostalCodeInfo;

public interface IPostalCodeInfoListener {
    void onInvalidZip();

    void onNetworkUnavailable();

    void onPostalCodeInfoReceived(PostalCodeInfo postalCodeInfo);
}
