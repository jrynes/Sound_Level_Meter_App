package com.admarvel.android.admarvelgoogleplayadapter;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.admarvel.android.ads.AdMarvelAdapterListener;
import com.admarvel.android.ads.AdMarvelUtils;
import com.admarvel.android.ads.Constants;
import com.admarvel.android.ads.nativeads.AdMarvelNativeAd;
import com.admarvel.android.ads.nativeads.AdMarvelNativeAd.AdMarvelNativeAdType;
import com.admarvel.android.ads.nativeads.AdMarvelNativeCta;
import com.admarvel.android.ads.nativeads.AdMarvelNativeImage;
import com.admarvel.android.ads.nativeads.AdMarvelNativeMetadata;
import com.admarvel.android.ads.nativeads.AdMarvelNativeRating;
import com.admarvel.android.util.Logging;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.formats.NativeAd.Image;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAd.OnAppInstallAdLoadedListener;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAd.OnContentAdLoadedListener;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InternalGooglePlayNativeListener extends AdListener implements OnAppInstallAdLoadedListener, OnContentAdLoadedListener {
    final AdMarvelAdapterListener f9a;
    final AdMarvelNativeAd f10b;
    NativeContentAd f11c;
    NativeAppInstallAd f12d;
    final WeakReference f13e;

    public InternalGooglePlayNativeListener(AdMarvelAdapterListener adMarvelAdapterListener, AdMarvelNativeAd adMarvelNativeAd, AdMarvelGooglePlayAdapter adMarvelGooglePlayAdapter) {
        this.f13e = new WeakReference(adMarvelGooglePlayAdapter);
        this.f9a = adMarvelAdapterListener;
        this.f10b = adMarvelNativeAd;
    }

    private boolean updateAdMarvelNatvieAd(NativeAppInstallAd nativeAppInstallAd) {
        Map hashMap = new HashMap();
        if (!(this.f10b == null || nativeAppInstallAd == null)) {
            Uri uri;
            if (nativeAppInstallAd.getHeadline() != null) {
                hashMap.put(AdMarvelNativeAd.FIELD_DISPLAYNAME, nativeAppInstallAd.getHeadline());
            }
            if (nativeAppInstallAd.getBody() != null) {
                hashMap.put(AdMarvelNativeAd.FIELD_SHORTMESSAGE, nativeAppInstallAd.getBody());
            }
            if (nativeAppInstallAd.getIcon() != null) {
                try {
                    Image icon = nativeAppInstallAd.getIcon();
                    AdMarvelNativeImage adMarvelNativeImage = new AdMarvelNativeImage();
                    if (this.f13e != null) {
                        if ((((AdMarvelGooglePlayAdapter) this.f13e.get()).disableImageLoading & (this.f13e.get() != null ? 1 : 0)) != 0) {
                            uri = icon.getUri();
                            if (uri != null) {
                                adMarvelNativeImage.setImageUrl(uri.toString());
                            }
                            hashMap.put(SettingsJsonConstants.APP_ICON_KEY, adMarvelNativeImage);
                        }
                    }
                    adMarvelNativeImage.setDrawableResource(icon.getDrawable());
                    hashMap.put(SettingsJsonConstants.APP_ICON_KEY, adMarvelNativeImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                AdMarvelNativeMetadata adMarvelNativeMetadata;
                Map hashMap2;
                if (nativeAppInstallAd.getImages() != null && nativeAppInstallAd.getImages().size() > 0) {
                    try {
                        List images = nativeAppInstallAd.getImages();
                        if (images != null && images.size() > 0) {
                            Object obj = new AdMarvelNativeImage[images.size()];
                            for (int i = 0; i < images.size(); i++) {
                                AdMarvelNativeImage adMarvelNativeImage2;
                                if (this.f13e != null) {
                                    if ((((AdMarvelGooglePlayAdapter) this.f13e.get()).disableImageLoading & (this.f13e.get() != null ? 1 : 0)) != 0) {
                                        uri = ((Image) images.get(i)).getUri();
                                        if (uri != null) {
                                            adMarvelNativeImage2 = new AdMarvelNativeImage();
                                            adMarvelNativeImage2.setImageUrl(uri.toString());
                                            obj[i] = adMarvelNativeImage2;
                                        }
                                    }
                                }
                                Drawable drawable = ((Image) images.get(i)).getDrawable();
                                if (drawable != null) {
                                    adMarvelNativeImage2 = new AdMarvelNativeImage();
                                    adMarvelNativeImage2.setDrawableResource(drawable);
                                    obj[i] = adMarvelNativeImage2;
                                }
                            }
                            hashMap.put(AdMarvelNativeAd.FIELD_CAMPAIGNIMAGE, obj);
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                String str = (String) nativeAppInstallAd.getCallToAction();
                if (str != null) {
                    try {
                        AdMarvelNativeCta adMarvelNativeCta = new AdMarvelNativeCta();
                        adMarvelNativeCta.setTitle(str);
                        hashMap.put(AdMarvelNativeAd.FIELD_CTA, adMarvelNativeCta);
                    } catch (Exception e22) {
                        e22.printStackTrace();
                    }
                }
                Double starRating = nativeAppInstallAd.getStarRating();
                if (starRating != null) {
                    try {
                        AdMarvelNativeRating adMarvelNativeRating = new AdMarvelNativeRating();
                        adMarvelNativeRating.setBase(String.valueOf(5));
                        adMarvelNativeRating.setValue(String.valueOf(starRating));
                        hashMap.put(AdMarvelNativeAd.FIELD_RATING, adMarvelNativeRating);
                    } catch (Exception e3) {
                        Logging.log("Error in setting Rating fields");
                    }
                }
                str = (String) nativeAppInstallAd.getStore();
                if (str != null) {
                    try {
                        adMarvelNativeMetadata = new AdMarvelNativeMetadata();
                        adMarvelNativeMetadata.setType("string");
                        adMarvelNativeMetadata.setValue(str);
                        hashMap2 = new HashMap();
                        hashMap2.put("availabilityString", adMarvelNativeMetadata);
                        hashMap.put(Constants.NATIVE_AD_METADATAS_ELEMENT, hashMap2);
                    } catch (Exception e222) {
                        e222.printStackTrace();
                    }
                }
                str = (String) nativeAppInstallAd.getPrice();
                if (str != null) {
                    adMarvelNativeMetadata = new AdMarvelNativeMetadata();
                    adMarvelNativeMetadata.setType("string");
                    adMarvelNativeMetadata.setValue(str);
                    hashMap2 = (Map) hashMap.get(Constants.NATIVE_AD_METADATAS_ELEMENT);
                    if (hashMap2 == null) {
                        hashMap2 = new HashMap();
                    }
                    hashMap2.put("appPrice", adMarvelNativeMetadata);
                    hashMap.put(Constants.NATIVE_AD_METADATAS_ELEMENT, hashMap2);
                }
                this.f10b.updateNativeAdFromAdapter(hashMap);
            } catch (Exception e2222) {
                e2222.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private boolean updateAdMarvelNatvieAd(NativeContentAd nativeContentAd) {
        Map hashMap = new HashMap();
        if (!(this.f10b == null || nativeContentAd == null)) {
            Uri uri;
            if (nativeContentAd.getHeadline() != null) {
                hashMap.put(AdMarvelNativeAd.FIELD_DISPLAYNAME, nativeContentAd.getHeadline());
            }
            if (nativeContentAd.getBody() != null) {
                hashMap.put(AdMarvelNativeAd.FIELD_SHORTMESSAGE, nativeContentAd.getBody());
            }
            if (nativeContentAd.getLogo() != null) {
                try {
                    Image logo = nativeContentAd.getLogo();
                    AdMarvelNativeImage adMarvelNativeImage = new AdMarvelNativeImage();
                    if (this.f13e != null) {
                        if ((((AdMarvelGooglePlayAdapter) this.f13e.get()).disableImageLoading & (this.f13e.get() != null ? 1 : 0)) != 0) {
                            uri = logo.getUri();
                            if (uri != null) {
                                adMarvelNativeImage.setImageUrl(uri.toString());
                            }
                            hashMap.put(SettingsJsonConstants.APP_ICON_KEY, adMarvelNativeImage);
                        }
                    }
                    adMarvelNativeImage.setDrawableResource(logo.getDrawable());
                    hashMap.put(SettingsJsonConstants.APP_ICON_KEY, adMarvelNativeImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                if (nativeContentAd.getImages() != null && nativeContentAd.getImages().size() > 0) {
                    try {
                        List images = nativeContentAd.getImages();
                        if (images != null && images.size() > 0) {
                            Object obj = new AdMarvelNativeImage[images.size()];
                            for (int i = 0; i < images.size(); i++) {
                                AdMarvelNativeImage adMarvelNativeImage2;
                                if (this.f13e != null) {
                                    if ((((AdMarvelGooglePlayAdapter) this.f13e.get()).disableImageLoading & (this.f13e.get() != null ? 1 : 0)) != 0) {
                                        uri = ((Image) images.get(i)).getUri();
                                        if (uri != null) {
                                            adMarvelNativeImage2 = new AdMarvelNativeImage();
                                            adMarvelNativeImage2.setImageUrl(uri.toString());
                                            obj[i] = adMarvelNativeImage2;
                                        }
                                    }
                                }
                                Drawable drawable = ((Image) images.get(i)).getDrawable();
                                if (drawable != null) {
                                    adMarvelNativeImage2 = new AdMarvelNativeImage();
                                    adMarvelNativeImage2.setDrawableResource(drawable);
                                    obj[i] = adMarvelNativeImage2;
                                }
                            }
                            hashMap.put(AdMarvelNativeAd.FIELD_CAMPAIGNIMAGE, obj);
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                String str = (String) nativeContentAd.getCallToAction();
                if (str != null) {
                    try {
                        AdMarvelNativeCta adMarvelNativeCta = new AdMarvelNativeCta();
                        adMarvelNativeCta.setTitle(str);
                        hashMap.put(AdMarvelNativeAd.FIELD_CTA, adMarvelNativeCta);
                    } catch (Exception e22) {
                        e22.printStackTrace();
                    }
                }
                str = (String) nativeContentAd.getAdvertiser();
                if (str != null) {
                    try {
                        AdMarvelNativeMetadata adMarvelNativeMetadata = new AdMarvelNativeMetadata();
                        adMarvelNativeMetadata.setType("string");
                        adMarvelNativeMetadata.setValue(str);
                        Map hashMap2 = new HashMap();
                        hashMap2.put("appPublisherName", adMarvelNativeMetadata);
                        hashMap.put(Constants.NATIVE_AD_METADATAS_ELEMENT, hashMap2);
                    } catch (Exception e222) {
                        e222.printStackTrace();
                    }
                }
                this.f10b.updateNativeAdFromAdapter(hashMap);
            } catch (Exception e2222) {
                e2222.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public void onAdFailedToLoad(int i) {
        super.onAdFailedToLoad(i);
        if (this.f9a != null) {
            this.f9a.onFailedToReceiveAd(205, AdMarvelUtils.getErrorReason(205));
            Logging.log("GooglePlay SDK Adapter - onNativeRequestFailed - " + i);
            return;
        }
        Logging.log("GooglePlay SDK Adapter - onNativeRequestFailed no listener found ... - " + i);
    }

    public void onAppInstallAdLoaded(NativeAppInstallAd nativeAppInstallAd) {
        if (nativeAppInstallAd != null) {
            this.f12d = nativeAppInstallAd;
            boolean updateAdMarvelNatvieAd = updateAdMarvelNatvieAd(nativeAppInstallAd);
            if (updateAdMarvelNatvieAd && this.f9a != null) {
                this.f10b.setNativeAdType(AdMarvelNativeAdType.ADMARVEL_NATIVEAD_TYPE_APPINSTALL);
                this.f9a.onReceiveNativeAd(this.f10b);
                Logging.log("GooglePlay SDK : onAppInstallAdLoaded");
            } else if (updateAdMarvelNatvieAd || this.f9a == null) {
                Logging.log("GooglePlay SDK : onAppInstallAdLoaded No listenr found");
            } else {
                this.f9a.onFailedToReceiveAd(205, AdMarvelUtils.getErrorReason(205));
                Logging.log("GooglePlay SDK : onError");
            }
        }
    }

    public void onContentAdLoaded(NativeContentAd nativeContentAd) {
        if (nativeContentAd != null) {
            this.f11c = nativeContentAd;
            boolean updateAdMarvelNatvieAd = updateAdMarvelNatvieAd(nativeContentAd);
            if (updateAdMarvelNatvieAd && this.f9a != null) {
                this.f10b.setNativeAdType(AdMarvelNativeAdType.ADMARVEL_NATIVEAD_TYPE_CONTENT);
                this.f9a.onReceiveNativeAd(this.f10b);
                Logging.log("GooglePlay SDK : onContentAdLoaded");
            } else if (updateAdMarvelNatvieAd || this.f9a == null) {
                Logging.log("GooglePlay SDK : onContentAdLoaded No listenr found");
            } else {
                this.f9a.onFailedToReceiveAd(205, AdMarvelUtils.getErrorReason(205));
                Logging.log("GooglePlay SDK : onError");
            }
        }
    }
}
