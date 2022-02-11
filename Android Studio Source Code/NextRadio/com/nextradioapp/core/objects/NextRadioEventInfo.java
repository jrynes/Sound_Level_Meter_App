package com.nextradioapp.core.objects;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.apache.activemq.transport.stomp.Stomp;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "radioEvent", strict = false)
public class NextRadioEventInfo {
    @Attribute(required = true)
    public String UFIDIdentifier;
    @Element(required = false)
    public String album;
    @Element(required = false)
    public String artist;
    @ElementList(name = "cards", required = false)
    public ArrayList<Card> cards;
    @Attribute(required = false)
    public String category;
    @ElementList(name = "ads", required = false)
    public ArrayList<Enhancement> enhancements;
    public long eventID;
    public long historyID;
    @Element(required = false)
    public String imageURL;
    @Element(required = false)
    public String imageURLHiRes;
    public boolean isFavorite;
    public boolean isFromRecentlyPlayed;
    @Attribute(required = true)
    public int itemType;
    @Attribute(required = false)
    public String primaryAction;
    @Element(required = false)
    public StationInfo stationInfo;
    @Element(required = false)
    public String teID;
    @Element(required = false, type = CustomMatcher.class)
    public Date timePlayed;
    @Element(required = false)
    public String title;
    @Attribute(required = false)
    public String trackingID;

    private static boolean comp(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return true;
        }
        if (s1 == null && s2 != null) {
            return false;
        }
        if (s1 == null || s2 != null) {
            return s1.equals(s2);
        }
        return false;
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.equals(Stomp.EMPTY);
    }

    public NextRadioEventInfo() {
        this.stationInfo = new StationInfo();
        this.enhancements = new ArrayList();
    }

    public NextRadioEventInfo(String Artist, String Album, String Title, String StationShortName, String ImageURL, int Frequency, int Program) {
        this.stationInfo = new StationInfo();
        this.artist = Artist;
        this.album = Album;
        this.title = Title;
        this.imageURL = ImageURL;
        this.stationInfo.callLetters = StationShortName;
        this.stationInfo.frequencyHz = Frequency;
        this.stationInfo.frequencySubChannel = Program;
    }

    private Enhancement getEnhancementForType(String typename) throws Exception {
        Iterator i$ = getEnhancements().iterator();
        while (i$.hasNext()) {
            Enhancement enhancement = (Enhancement) i$.next();
            if (enhancement.type.equals(typename)) {
                return enhancement;
            }
        }
        throw new Exception("Action for enhancement type " + typename + " was not found");
    }

    public boolean isSaveable() {
        if (this.itemType == 4 || this.stationInfo == null || this.stationInfo.publicStationID == 0 || this.UFIDIdentifier == null || this.UFIDIdentifier.length() == 0) {
            return false;
        }
        if (isNullOrEmpty(this.title) && isNullOrEmpty(this.artist)) {
            return false;
        }
        return true;
    }

    public boolean isEqualTo(NextRadioEventInfo src) {
        if (src != null && comp(this.artist, src.artist) && comp(this.UFIDIdentifier, src.UFIDIdentifier) && comp(this.album, src.album) && comp(this.title, src.title)) {
            return true;
        }
        return false;
    }

    public void putEnhancement(String Type, String field0, String field1, String field2, String field3, String field4, String field5) {
        if (getEnhancements() == null) {
            setEnhancements(new ArrayList());
        }
        try {
            if (hasEnhancementTypeOf(Type)) {
                this.enhancements.remove(getEnhancementForType(Type));
            }
            getEnhancements().add(new Enhancement(Type, field0, field1, field2, field3, field4, field5));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("###.#");
        StringBuilder sb = new StringBuilder();
        sb.append(" freq").append(decimalFormat.format((double) (((float) this.stationInfo.frequencyHz) / 1000000.0f))).append(" prog:").append(this.stationInfo.frequencySubChannel);
        sb.append(" ID:'").append(this.UFIDIdentifier).append("' ");
        sb.append(" artist:'").append(this.artist).append("' ");
        sb.append(" album:'").append(this.album).append("' ");
        sb.append(" title:'").append(this.title).append("' ");
        sb.append(" itemType:'").append(this.itemType).append("' ");
        sb.append(" imageURL:'").append(this.imageURL).append("' ");
        sb.append(" UFID0:'").append(this.UFIDIdentifier).append("' ");
        sb.append(" slogan:'").append(this.stationInfo.slogan).append("' ");
        sb.append(" primaryAction:'").append(this.primaryAction).append("' ");
        sb.append(" getPrimaryAction:'").append(getPrimaryAction()).append("' ");
        return sb.toString();
    }

    public ArrayList<Enhancement> getEnhancements() {
        return this.enhancements;
    }

    public void setEnhancements(ArrayList<Enhancement> enhancements) {
        this.enhancements = enhancements;
    }

    public boolean hasEnhancementTypeOf(String enhancementName) {
        boolean foundAnEnhancement = false;
        if (this.enhancements != null) {
            Iterator i$ = this.enhancements.iterator();
            while (i$.hasNext()) {
                if (enhancementName.equals(((Enhancement) i$.next()).type)) {
                    foundAnEnhancement = true;
                }
            }
        }
        return foundAnEnhancement;
    }

    public CharSequence getUILine1() {
        return this.itemType != 1 ? this.artist == null ? Stomp.EMPTY : this.artist : this.title == null ? Stomp.EMPTY : this.title;
    }

    public String getMediaControlString() {
        String returnString = this.stationInfo.frequencyMHz();
        if (this.stationInfo.getStationType() == 0) {
            return returnString;
        }
        if (!getUILine1().equals(Stomp.EMPTY)) {
            returnString = returnString + " - " + getUILine1();
        }
        if (!getUILine2().equals(Stomp.EMPTY)) {
            returnString = returnString + " - " + getUILine2();
        }
        return returnString;
    }

    public CharSequence getUILine2() {
        if (this.itemType != 1) {
            return this.title == null ? Stomp.EMPTY : this.title;
        } else {
            return (this.artist == null ? Stomp.EMPTY : this.artist) + (this.album == null ? Stomp.EMPTY : " / " + this.album);
        }
    }

    public CharSequence getArtistName() {
        if (this.itemType == 1) {
            return this.artist == null ? Stomp.EMPTY : this.artist;
        } else {
            return null;
        }
    }

    public int getDisplayType() {
        if (this.itemType == 1) {
            return 1;
        }
        if (this.itemType == 2) {
            return 2;
        }
        return 3;
    }

    public String getPrimaryAction() {
        if (this.primaryAction != null && !this.primaryAction.equals("none")) {
            return this.primaryAction;
        }
        if (getDisplayType() == 1) {
            return "mp3search";
        }
        if (this.enhancements == null || this.enhancements.size() <= 0 || ((Enhancement) this.enhancements.get(0)).type == null || ((Enhancement) this.enhancements.get(0)).type.equals("none")) {
            return "share";
        }
        return ((Enhancement) this.enhancements.get(0)).type;
    }
}
