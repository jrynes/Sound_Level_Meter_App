package com.nextradioapp.core.serialization;

import com.nextradioapp.core.objects.NextRadioEventInfo;
import java.util.ArrayList;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class RadioEvents {
    @ElementList(inline = true)
    public ArrayList<NextRadioEventInfo> radioEvents;
    @Attribute(required = false)
    public String version;
}
