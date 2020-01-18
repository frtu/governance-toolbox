package com.github.frtu.proto.metadata;

import com.github.frtu.proto.metadata.sla.DataSLA;
import com.github.frtu.proto.metadata.sla.Sla;
import com.google.protobuf.Descriptors;

import java.util.Optional;

public class SlaMetadataHelper extends BaseMessageMetadataHelper<DataSLA> {
    public SlaMetadataHelper() {
        super(Sla.dataSla);
    }

    public boolean hasSLA(Descriptors.Descriptor messageDescriptor) {
        return super.hasExtension(messageDescriptor);
    }

    public Optional<DataSLA> getSLA(Descriptors.Descriptor messageDescriptor) {
        return super.getExtension(messageDescriptor);
    }
}
