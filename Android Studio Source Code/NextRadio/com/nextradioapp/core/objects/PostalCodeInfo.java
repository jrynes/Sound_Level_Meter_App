package com.nextradioapp.core.objects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "postalCodeInfo", strict = false)
public class PostalCodeInfo {
    @Attribute(required = false)
    public String city;
    @Attribute(required = false)
    public String country;
    @Attribute(required = false)
    public float latitude;
    @Attribute(required = false)
    public float longitude;
    @Attribute(required = false)
    public String postalCode;
    @Attribute(required = false)
    public String state;

    public Location getLocation() {
        Location loc = new Location("TagStation");
        loc.setLatitude(this.latitude);
        loc.setLongitude(this.longitude);
        return loc;
    }

    public String toString() {
        if (this.state == null || this.city == null) {
            return "Invalid zip code";
        }
        return this.city + ", " + this.state;
    }
}
