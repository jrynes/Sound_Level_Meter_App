package com.nextradioapp.core.serialization;

import com.nextradioapp.core.objects.StationInfo;
import java.util.ArrayList;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Stations {
    @ElementList(inline = true, required = false)
    public ArrayList<StationInfo> stations;
    @Attribute(required = false)
    public String version;

    public int getCount() {
        return this.stations.size();
    }
}
