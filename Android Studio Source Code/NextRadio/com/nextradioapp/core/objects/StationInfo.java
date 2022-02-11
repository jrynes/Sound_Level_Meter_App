package com.nextradioapp.core.objects;

import java.text.DecimalFormat;
import java.util.Date;
import org.apache.activemq.transport.stomp.Stomp;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class StationInfo {
    @Attribute
    public String callLetters;
    @Attribute(required = false)
    public String country;
    @Attribute(name = "artistList", required = false)
    public String description;
    @Attribute(required = false)
    public String endpoint;
    @Attribute
    public int frequencyHz;
    @Attribute(name = "hdSubChannel", required = false)
    public int frequencySubChannel;
    public boolean fromNoData;
    @Attribute(name = "format", required = false)
    public String genre;
    @Attribute(required = false)
    public String headline;
    @Attribute(required = false)
    public String headlineText;
    @Attribute(required = false)
    public String imageURL;
    @Attribute(required = false)
    public String imageURLHiRes;
    public boolean isFavorited;
    public boolean isValid;
    public long lastListened;
    @Attribute(required = false)
    public String market;
    @Attribute(required = false)
    public String piCode;
    @Attribute
    public int publicStationID;
    @Attribute(required = false)
    public String slogan;
    @Attribute(required = false)
    public String trackingID;

    public StationInfo() {
        this.callLetters = Stomp.EMPTY;
        this.isValid = true;
        this.fromNoData = false;
    }

    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("###.#");
        StringBuilder sb = new StringBuilder();
        sb.append(decimalFormat.format((double) (((float) this.frequencyHz) / 1000000.0f))).append("-").append(this.frequencySubChannel);
        sb.append(" " + this.callLetters + "' ");
        sb.append(" - " + this.slogan + "' ");
        sb.append(" - '" + this.genre + "' ");
        sb.append(" - '" + this.description + "' ");
        return sb.toString();
    }

    public void setStationFavorite(boolean favorite) {
        this.isFavorited = favorite;
    }

    public String frequencyMHz() {
        if (this.frequencyHz == 0) {
            return Stomp.EMPTY;
        }
        return new DecimalFormat("###.#").format((double) (((float) this.frequencyHz) / 1000000.0f));
    }

    public String frequencyMHzAndProgram() {
        return frequencyMHz() + " HD" + (this.frequencySubChannel + 1);
    }

    public Date getLastListened() {
        if (this.lastListened == 0) {
            return null;
        }
        return new Date(this.lastListened);
    }

    public String frequencyAndCallLetters() {
        if (this.callLetters == null) {
            return frequencyMHz() + " ";
        }
        return frequencyMHz() + " " + this.callLetters;
    }

    public String frequencyAndHDChannel() {
        return frequencyMHz() + " " + getHDChannelDescription();
    }

    public String frequencyCallLettersSlogan() {
        if (this.frequencyHz <= 0) {
            return Stomp.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(frequencyMHz()).append(" ").append(this.callLetters);
        if (this.slogan != null && !this.slogan.equals(Stomp.EMPTY)) {
            return sb.append(" - ").append(this.slogan).toString();
        }
        if (this.genre == null || this.genre.equals(Stomp.EMPTY)) {
            return sb.toString();
        }
        return sb.append(" - ").append(this.genre).toString();
    }

    public String UITitle() {
        if (this.headline == null || this.headline.length() <= 0) {
            return frequencyAndCallLetters();
        }
        return this.headline;
    }

    public String UIDescription() {
        if (this.headlineText != null && !this.headlineText.equals(Stomp.EMPTY)) {
            return this.headlineText;
        }
        if (this.slogan == null || this.slogan.length() <= 0) {
            return this.genre;
        }
        return this.slogan;
    }

    public String getHDChannelDescription() {
        if (this.frequencySubChannel <= 0) {
            return "FM";
        }
        return "HD" + (this.frequencySubChannel + 1);
    }

    public boolean hasHDChannel() {
        if (this.frequencySubChannel <= 0) {
            return false;
        }
        return true;
    }

    public boolean hasEndpoint() {
        return this.endpoint != null && this.endpoint.length() > 0;
    }

    public CharSequence getSlogan() {
        if (this.slogan == null || this.slogan.length() <= 0) {
            return this.genre;
        }
        return this.slogan;
    }

    public String getCallLetters() {
        if (this.callLetters == null) {
            return Stomp.EMPTY;
        }
        return this.callLetters;
    }

    public int getStationType() {
        if (this.publicStationID <= 0) {
            return 0;
        }
        if (this.endpoint != null && this.endpoint.length() > 0) {
            return 3;
        }
        if (this.slogan == null || this.slogan.length() <= 0) {
            return 1;
        }
        return 2;
    }

    public boolean areSame(StationInfo compare) {
        if (compare != null && this.publicStationID == compare.publicStationID && this.isFavorited == compare.isFavorited) {
            return true;
        }
        return false;
    }
}
