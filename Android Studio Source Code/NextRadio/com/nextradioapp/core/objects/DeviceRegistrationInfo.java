package com.nextradioapp.core.objects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "deviceRegistration", strict = false)
public class DeviceRegistrationInfo {
    @Attribute(required = true)
    public String TSD;
    @Attribute(required = true)
    public String adGroup;
    @Attribute(required = true)
    public String cachingGroup;
}
