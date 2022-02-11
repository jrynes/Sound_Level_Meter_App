package org.apache.activemq.openwire.v8;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.BrokerId;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.filter.BooleanExpression;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class ConsumerInfoMarshaller extends BaseCommandMarshaller {
    public byte getDataStructureType() {
        return (byte) 5;
    }

    public DataStructure createObject() {
        return new ConsumerInfo();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        short size;
        short i;
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        ConsumerInfo info = (ConsumerInfo) o;
        info.setConsumerId((ConsumerId) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setBrowser(bs.readBoolean());
        info.setDestination((ActiveMQDestination) tightUnmarsalCachedObject(wireFormat, dataIn, bs));
        info.setPrefetchSize(dataIn.readInt());
        info.setMaximumPendingMessageLimit(dataIn.readInt());
        info.setDispatchAsync(bs.readBoolean());
        info.setSelector(tightUnmarshalString(dataIn, bs));
        info.setSubscriptionName(tightUnmarshalString(dataIn, bs));
        info.setNoLocal(bs.readBoolean());
        info.setExclusive(bs.readBoolean());
        info.setRetroactive(bs.readBoolean());
        info.setPriority(dataIn.readByte());
        if (bs.readBoolean()) {
            size = dataIn.readShort();
            BrokerId[] value = new BrokerId[size];
            for (i = (short) 0; i < size; i++) {
                value[i] = (BrokerId) tightUnmarsalNestedObject(wireFormat, dataIn, bs);
            }
            info.setBrokerPath(value);
        } else {
            info.setBrokerPath(null);
        }
        info.setAdditionalPredicate((BooleanExpression) tightUnmarsalNestedObject(wireFormat, dataIn, bs));
        info.setNetworkSubscription(bs.readBoolean());
        info.setOptimizedAcknowledge(bs.readBoolean());
        info.setNoRangeAcks(bs.readBoolean());
        if (bs.readBoolean()) {
            size = dataIn.readShort();
            ConsumerId[] value2 = new ConsumerId[size];
            for (i = (short) 0; i < size; i++) {
                value2[i] = (ConsumerId) tightUnmarsalNestedObject(wireFormat, dataIn, bs);
            }
            info.setNetworkConsumerPath(value2);
            return;
        }
        info.setNetworkConsumerPath(null);
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        ConsumerInfo info = (ConsumerInfo) o;
        int rc = super.tightMarshal1(wireFormat, o, bs) + tightMarshalCachedObject1(wireFormat, info.getConsumerId(), bs);
        bs.writeBoolean(info.isBrowser());
        rc += tightMarshalCachedObject1(wireFormat, info.getDestination(), bs);
        bs.writeBoolean(info.isDispatchAsync());
        rc = (rc + tightMarshalString1(info.getSelector(), bs)) + tightMarshalString1(info.getSubscriptionName(), bs);
        bs.writeBoolean(info.isNoLocal());
        bs.writeBoolean(info.isExclusive());
        bs.writeBoolean(info.isRetroactive());
        rc = (rc + tightMarshalObjectArray1(wireFormat, info.getBrokerPath(), bs)) + tightMarshalNestedObject1(wireFormat, (DataStructure) info.getAdditionalPredicate(), bs);
        bs.writeBoolean(info.isNetworkSubscription());
        bs.writeBoolean(info.isOptimizedAcknowledge());
        bs.writeBoolean(info.isNoRangeAcks());
        return (rc + tightMarshalObjectArray1(wireFormat, info.getNetworkConsumerPath(), bs)) + 9;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        ConsumerInfo info = (ConsumerInfo) o;
        tightMarshalCachedObject2(wireFormat, info.getConsumerId(), dataOut, bs);
        bs.readBoolean();
        tightMarshalCachedObject2(wireFormat, info.getDestination(), dataOut, bs);
        dataOut.writeInt(info.getPrefetchSize());
        dataOut.writeInt(info.getMaximumPendingMessageLimit());
        bs.readBoolean();
        tightMarshalString2(info.getSelector(), dataOut, bs);
        tightMarshalString2(info.getSubscriptionName(), dataOut, bs);
        bs.readBoolean();
        bs.readBoolean();
        bs.readBoolean();
        dataOut.writeByte(info.getPriority());
        tightMarshalObjectArray2(wireFormat, info.getBrokerPath(), dataOut, bs);
        tightMarshalNestedObject2(wireFormat, (DataStructure) info.getAdditionalPredicate(), dataOut, bs);
        bs.readBoolean();
        bs.readBoolean();
        bs.readBoolean();
        tightMarshalObjectArray2(wireFormat, info.getNetworkConsumerPath(), dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        short i;
        super.looseUnmarshal(wireFormat, o, dataIn);
        ConsumerInfo info = (ConsumerInfo) o;
        info.setConsumerId((ConsumerId) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setBrowser(dataIn.readBoolean());
        info.setDestination((ActiveMQDestination) looseUnmarsalCachedObject(wireFormat, dataIn));
        info.setPrefetchSize(dataIn.readInt());
        info.setMaximumPendingMessageLimit(dataIn.readInt());
        info.setDispatchAsync(dataIn.readBoolean());
        info.setSelector(looseUnmarshalString(dataIn));
        info.setSubscriptionName(looseUnmarshalString(dataIn));
        info.setNoLocal(dataIn.readBoolean());
        info.setExclusive(dataIn.readBoolean());
        info.setRetroactive(dataIn.readBoolean());
        info.setPriority(dataIn.readByte());
        if (dataIn.readBoolean()) {
            short size;
            size = dataIn.readShort();
            BrokerId[] value = new BrokerId[size];
            for (i = (short) 0; i < size; i++) {
                value[i] = (BrokerId) looseUnmarsalNestedObject(wireFormat, dataIn);
            }
            info.setBrokerPath(value);
        } else {
            info.setBrokerPath(null);
        }
        info.setAdditionalPredicate((BooleanExpression) looseUnmarsalNestedObject(wireFormat, dataIn));
        info.setNetworkSubscription(dataIn.readBoolean());
        info.setOptimizedAcknowledge(dataIn.readBoolean());
        info.setNoRangeAcks(dataIn.readBoolean());
        if (dataIn.readBoolean()) {
            size = dataIn.readShort();
            ConsumerId[] value2 = new ConsumerId[size];
            for (i = (short) 0; i < size; i++) {
                value2[i] = (ConsumerId) looseUnmarsalNestedObject(wireFormat, dataIn);
            }
            info.setNetworkConsumerPath(value2);
            return;
        }
        info.setNetworkConsumerPath(null);
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        ConsumerInfo info = (ConsumerInfo) o;
        super.looseMarshal(wireFormat, o, dataOut);
        looseMarshalCachedObject(wireFormat, info.getConsumerId(), dataOut);
        dataOut.writeBoolean(info.isBrowser());
        looseMarshalCachedObject(wireFormat, info.getDestination(), dataOut);
        dataOut.writeInt(info.getPrefetchSize());
        dataOut.writeInt(info.getMaximumPendingMessageLimit());
        dataOut.writeBoolean(info.isDispatchAsync());
        looseMarshalString(info.getSelector(), dataOut);
        looseMarshalString(info.getSubscriptionName(), dataOut);
        dataOut.writeBoolean(info.isNoLocal());
        dataOut.writeBoolean(info.isExclusive());
        dataOut.writeBoolean(info.isRetroactive());
        dataOut.writeByte(info.getPriority());
        looseMarshalObjectArray(wireFormat, info.getBrokerPath(), dataOut);
        looseMarshalNestedObject(wireFormat, (DataStructure) info.getAdditionalPredicate(), dataOut);
        dataOut.writeBoolean(info.isNetworkSubscription());
        dataOut.writeBoolean(info.isOptimizedAcknowledge());
        dataOut.writeBoolean(info.isNoRangeAcks());
        looseMarshalObjectArray(wireFormat, info.getNetworkConsumerPath(), dataOut);
    }
}
