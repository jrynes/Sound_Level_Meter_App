package com.admarvel.android.admarvelfacebookadapter;

import android.view.View;
import com.admarvel.android.ads.AdMarvelAdapterListener;
import com.admarvel.android.ads.AdMarvelUtils;
import com.admarvel.android.ads.Constants;
import com.admarvel.android.ads.nativeads.AdMarvelNativeAd;
import com.admarvel.android.ads.nativeads.AdMarvelNativeCta;
import com.admarvel.android.ads.nativeads.AdMarvelNativeImage;
import com.admarvel.android.ads.nativeads.AdMarvelNativeMetadata;
import com.admarvel.android.ads.nativeads.AdMarvelNativeRating;
import com.admarvel.android.ads.nativeads.AdMarvelNativeVideoView;
import com.admarvel.android.util.Logging;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAd.Rating;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;

public class InternalFacebookNativeListener implements AdListener {
    final AdMarvelNativeAd adMarvelNativeAd;
    final AdMarvelAdapterListener adMarvelNativeAdAdapterListener;

    public InternalFacebookNativeListener(AdMarvelAdapterListener adMarvelAdapterListener, AdMarvelNativeAd adMarvelNativeAd) {
        this.adMarvelNativeAdAdapterListener = adMarvelAdapterListener;
        this.adMarvelNativeAd = adMarvelNativeAd;
    }

    private boolean updateAdMarvelNatvieAd(NativeAd nativeAd) {
        Map hashMap = new HashMap();
        if (!(this.adMarvelNativeAd == null || nativeAd == null)) {
            String url;
            int height;
            int width;
            AdMarvelNativeImage adMarvelNativeImage;
            if (nativeAd.getAdTitle() != null) {
                hashMap.put(AdMarvelNativeAd.FIELD_DISPLAYNAME, nativeAd.getAdTitle());
            }
            if (nativeAd.getAdBody() != null) {
                hashMap.put(AdMarvelNativeAd.FIELD_SHORTMESSAGE, nativeAd.getAdBody());
            }
            if (nativeAd.getAdIcon() != null) {
                try {
                    url = nativeAd.getAdIcon().getUrl();
                    height = nativeAd.getAdIcon().getHeight();
                    width = nativeAd.getAdIcon().getWidth();
                    adMarvelNativeImage = new AdMarvelNativeImage();
                    if (url != null) {
                        adMarvelNativeImage.setImageUrl(url);
                    }
                    if (height > 0) {
                        adMarvelNativeImage.setHeight(height);
                    }
                    if (width > 0) {
                        adMarvelNativeImage.setWidth(width);
                    }
                    hashMap.put(SettingsJsonConstants.APP_ICON_KEY, adMarvelNativeImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                AdMarvelNativeMetadata adMarvelNativeMetadata;
                Map hashMap2;
                if (nativeAd.getAdCoverImage() != null) {
                    try {
                        url = nativeAd.getAdCoverImage().getUrl();
                        height = nativeAd.getAdCoverImage().getHeight();
                        width = nativeAd.getAdCoverImage().getWidth();
                        adMarvelNativeImage = new AdMarvelNativeImage();
                        if (url != null) {
                            adMarvelNativeImage.setImageUrl(url);
                        }
                        if (height > 0) {
                            adMarvelNativeImage.setHeight(height);
                        }
                        if (width > 0) {
                            adMarvelNativeImage.setWidth(width);
                        }
                        hashMap.put(AdMarvelNativeAd.FIELD_CAMPAIGNIMAGE, new Object[]{adMarvelNativeImage});
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                Rating adStarRating = nativeAd.getAdStarRating();
                if (adStarRating != null) {
                    try {
                        double scale = adStarRating.getScale();
                        double value = adStarRating.getValue();
                        AdMarvelNativeRating adMarvelNativeRating = new AdMarvelNativeRating();
                        adMarvelNativeRating.setBase(String.valueOf(scale));
                        adMarvelNativeRating.setValue(String.valueOf(value));
                        hashMap.put(AdMarvelNativeAd.FIELD_RATING, adMarvelNativeRating);
                    } catch (Exception e3) {
                        Logging.log("Error in setting Rating fields");
                    }
                }
                url = nativeAd.getAdCallToAction();
                if (url != null) {
                    try {
                        AdMarvelNativeCta adMarvelNativeCta = new AdMarvelNativeCta();
                        adMarvelNativeCta.setTitle(url);
                        hashMap.put(AdMarvelNativeAd.FIELD_CTA, adMarvelNativeCta);
                    } catch (Exception e22) {
                        e22.printStackTrace();
                    }
                }
                url = nativeAd.getAdSocialContext();
                if (url != null) {
                    try {
                        adMarvelNativeMetadata = new AdMarvelNativeMetadata();
                        adMarvelNativeMetadata.setType("string");
                        adMarvelNativeMetadata.setValue(url);
                        hashMap2 = new HashMap();
                        hashMap2.put("availabilityString", adMarvelNativeMetadata);
                        hashMap.put(Constants.NATIVE_AD_METADATAS_ELEMENT, hashMap2);
                    } catch (Exception e222) {
                        e222.printStackTrace();
                    }
                }
                adMarvelNativeMetadata = new AdMarvelNativeMetadata();
                adMarvelNativeMetadata.setType("string");
                adMarvelNativeMetadata.setValue(Stomp.TRUE);
                hashMap2 = (Map) hashMap.get(Constants.NATIVE_AD_METADATAS_ELEMENT);
                if (hashMap2 == null) {
                    hashMap2 = new HashMap();
                }
                hashMap2.put("click_clientonly", adMarvelNativeMetadata);
                hashMap2.put("impression_clientonly", adMarvelNativeMetadata);
                adMarvelNativeMetadata = new AdMarvelNativeMetadata();
                adMarvelNativeMetadata.setType("string");
                adMarvelNativeMetadata.setValue("3600000");
                hashMap2.put("ttl_in_ms", adMarvelNativeMetadata);
                hashMap.put(Constants.NATIVE_AD_METADATAS_ELEMENT, hashMap2);
                try {
                    AdMarvelNativeVideoView adMarvelNativeVideoView = new AdMarvelNativeVideoView(this.adMarvelNativeAd.getmContext(), this.adMarvelNativeAd);
                    View mediaView = new MediaView(this.adMarvelNativeAd.getmContext());
                    mediaView.setNativeAd(nativeAd);
                    adMarvelNativeVideoView.setNativeVideoView(mediaView);
                    hashMap.put(AdMarvelNativeAd.FIELD_NATIVE_VIDEO_AD_VIEW, adMarvelNativeVideoView);
                } catch (Exception e2222) {
                    e2222.printStackTrace();
                }
                this.adMarvelNativeAd.updateNativeAdFromAdapter(hashMap);
            } catch (Exception e22222) {
                e22222.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public void onAdClicked(Ad ad) {
        if (this.adMarvelNativeAdAdapterListener != null) {
            this.adMarvelNativeAdAdapterListener.onClickAd(Stomp.EMPTY);
            Logging.log("Facebook SDK : onAdClicked");
            return;
        }
        Logging.log("Facebook SDK : onAdClicked .. No listener Found");
    }

    public void onAdLoaded(Ad ad) {
        if (ad != null && (ad instanceof NativeAd)) {
            boolean updateAdMarvelNatvieAd = updateAdMarvelNatvieAd((NativeAd) ad);
            if (updateAdMarvelNatvieAd && this.adMarvelNativeAdAdapterListener != null) {
                this.adMarvelNativeAdAdapterListener.onReceiveNativeAd(this.adMarvelNativeAd);
                Logging.log("Facebook SDK : onAdLoaded");
            } else if (updateAdMarvelNatvieAd || this.adMarvelNativeAdAdapterListener == null) {
                Logging.log("Facebook SDK : onAdLoaded No listenr found");
            } else {
                this.adMarvelNativeAdAdapterListener.onFailedToReceiveAd(205, AdMarvelUtils.getErrorReason(205));
                Logging.log("Facebook SDK : onError");
            }
        }
    }

    public void onError(Ad ad, AdError adError) {
        if (this.adMarvelNativeAdAdapterListener != null) {
            this.adMarvelNativeAdAdapterListener.onFailedToReceiveAd(205, AdMarvelUtils.getErrorReason(205));
            Logging.log("Facebook SDK : onError - " + adError.getErrorMessage());
            return;
        }
        Logging.log("Facebook SDK : onError .. No listener Found - " + adError.getErrorMessage());
    }
}
