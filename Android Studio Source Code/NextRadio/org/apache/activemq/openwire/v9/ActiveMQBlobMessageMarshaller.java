package org.apache.activemq.openwire.v9;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.ActiveMQBlobMessage;
import org.apache.activemq.command.CommandTypes;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class ActiveMQBlobMessageMarshaller extends ActiveMQMessageMarshaller {
    public byte getDataStructureType() {
        return CommandTypes.ACTIVEMQ_BLOB_MESSAGE;
    }

    public DataStructure createObject() {
        return new ActiveMQBlobMessage();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        ActiveMQBlobMessage info = (ActiveMQBlobMessage) o;
        info.setRemoteBlobUrl(tightUnmarshalString(dataIn, bs));
        info.setMimeType(tightUnmarshalString(dataIn, bs));
        info.setDeletedByBroker(bs.readBoolean());
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        ActiveMQBlobMessage info = (ActiveMQBlobMessage) o;
        int rc = (super.tightMarshal1(wireFormat, o, bs) + tightMarshalString1(info.getRemoteBlobUrl(), bs)) + tightMarshalString1(info.getMimeType(), bs);
        bs.writeBoolean(info.isDeletedByBroker());
        return rc + 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        ActiveMQBlobMessage info = (ActiveMQBlobMessage) o;
        tightMarshalString2(info.getRemoteBlobUrl(), dataOut, bs);
        tightMarshalString2(info.getMimeType(), dataOut, bs);
        bs.readBoolean();
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        ActiveMQBlobMessage info = (ActiveMQBlobMessage) o;
        info.setRemoteBlobUrl(looseUnmarshalString(dataIn));
        info.setMimeType(looseUnmarshalString(dataIn));
        info.setDeletedByBroker(dataIn.readBoolean());
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        ActiveMQBlobMessage info = (ActiveMQBlobMessage) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalString(info.getRemoteBlobUrl(), dataOut);
        looseMarshalString(info.getMimeType(), dataOut);
        dataOut.writeBoolean(info.isDeletedByBroker());
    }
}
