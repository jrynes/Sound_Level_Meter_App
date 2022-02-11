package com.nextradioapp.nextradio.ottos;

import com.nextradioapp.core.objects.NextRadioEventInfo;

public class NRCurrentEvent {
    public NextRadioEventInfo currentEvent;

    public String toString() {
        if (this.currentEvent == null) {
            return "NRCurrentEvent NULL";
        }
        return "NRCurrentEvent " + this.currentEvent.toString();
    }
}
