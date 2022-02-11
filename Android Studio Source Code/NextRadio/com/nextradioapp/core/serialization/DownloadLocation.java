package com.nextradioapp.core.serialization;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class DownloadLocation {
    @Attribute
    public String DownloadTime;
    @Attribute
    public String Latitude;
    @Attribute
    public String Longitude;

    public DownloadLocation(String latitude, String longitude, String downloadTime) {
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.DownloadTime = downloadTime;
    }
}
