package com.nextradioapp.nextradio.ottos;

public class NRImpression {
    public String cardTrackingID;
    public String debugDescription;
    public int stationID;
    public String teID;
    public int trackingContext;
    public String trackingID;

    public String toString() {
        try {
            return "NRImpression desc: " + this.debugDescription + " context:" + this.trackingContext + " id:" + this.trackingID + " stationID:" + this.stationID + " cardTrackingID:" + this.cardTrackingID + " teID:" + this.teID;
        } catch (Exception e) {
            return "error:" + super.toString();
        }
    }
}
