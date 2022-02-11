package com.nextradioapp.nextradio.ottos;

public class NRDialogEvent {
    public String UFID;

    public String toString() {
        if (this.UFID == null) {
            return "NRCurrentEvent UFID:NULL";
        }
        return "NRCurrentEvent UFID:" + this.UFID;
    }
}
