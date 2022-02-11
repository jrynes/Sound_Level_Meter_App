package com.nextradioapp.core.serialization;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class ListeningSpan {
    @Attribute
    public String FromTime;
    @Attribute
    public String StationID;
    @Attribute
    public String ToTime;

    public ListeningSpan(String publicStationID, String fromTime, String toTime) {
        this.StationID = publicStationID;
        this.FromTime = fromTime;
        this.ToTime = toTime;
    }
}
