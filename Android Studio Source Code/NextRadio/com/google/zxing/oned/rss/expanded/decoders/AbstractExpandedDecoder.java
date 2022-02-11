package com.google.zxing.oned.rss.expanded.decoders;

import com.google.android.gms.location.places.Place;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;
import com.rabbitmq.client.impl.AMQImpl.Basic;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;

public abstract class AbstractExpandedDecoder {
    private final GeneralAppIdDecoder generalDecoder;
    private final BitArray information;

    public abstract String parseInformation() throws NotFoundException;

    AbstractExpandedDecoder(BitArray information) {
        this.information = information;
        this.generalDecoder = new GeneralAppIdDecoder(information);
    }

    protected BitArray getInformation() {
        return this.information;
    }

    protected GeneralAppIdDecoder getGeneralDecoder() {
        return this.generalDecoder;
    }

    public static AbstractExpandedDecoder createDecoder(BitArray information) {
        if (information.get(1)) {
            return new AI01AndOtherAIs(information);
        }
        if (!information.get(2)) {
            return new AnyAIDecoder(information);
        }
        switch (GeneralAppIdDecoder.extractNumericValueFromBitArray(information, 1, 4)) {
            case Type.MF /*4*/:
                return new AI013103decoder(information);
            case Service.RJE /*5*/:
                return new AI01320xDecoder(information);
            default:
                switch (GeneralAppIdDecoder.extractNumericValueFromBitArray(information, 1, 5)) {
                    case Protocol.PUP /*12*/:
                        return new AI01392xDecoder(information);
                    case Service.DAYTIME /*13*/:
                        return new AI01393xDecoder(information);
                    default:
                        switch (GeneralAppIdDecoder.extractNumericValueFromBitArray(information, 1, 7)) {
                            case Place.TYPE_LIQUOR_STORE /*56*/:
                                return new AI013x0x1xDecoder(information, "310", "11");
                            case Place.TYPE_LOCAL_GOVERNMENT_OFFICE /*57*/:
                                return new AI013x0x1xDecoder(information, "320", "11");
                            case Place.TYPE_LOCKSMITH /*58*/:
                                return new AI013x0x1xDecoder(information, "310", "13");
                            case Place.TYPE_LODGING /*59*/:
                                return new AI013x0x1xDecoder(information, "320", "13");
                            case Basic.INDEX /*60*/:
                                return new AI013x0x1xDecoder(information, "310", "15");
                            case Service.NI_MAIL /*61*/:
                                return new AI013x0x1xDecoder(information, "320", "15");
                            case Protocol.CFTP /*62*/:
                                return new AI013x0x1xDecoder(information, "310", "17");
                            case Service.VIA_FTP /*63*/:
                                return new AI013x0x1xDecoder(information, "320", "17");
                            default:
                                throw new IllegalStateException("unknown decoder: " + information);
                        }
                }
        }
    }
}
