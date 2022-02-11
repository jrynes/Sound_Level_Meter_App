package com.nextradioapp.nextradio.ottos;

import com.nextradioapp.core.objects.StationInfo;
import java.util.ArrayList;

public class NRStationList {
    public int errorCode;
    public ArrayList<StationInfo> stationList;
    public boolean updatedFromNetwork;
    public boolean wasForced;

    public String toString() {
        if (this.stationList == null) {
            return "NRStationList stationList NULL errorCode " + this.errorCode + "updatedFromNetwork " + this.updatedFromNetwork;
        }
        return "NRStationList stationList size:" + this.stationList.size() + " errorCode " + this.errorCode + "updatedFromNetwork " + this.updatedFromNetwork;
    }
}
