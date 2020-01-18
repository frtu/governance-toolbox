package com.github.frtu.proto.metadata;

import com.github.frtu.proto.metadata.security.Security;
import com.google.protobuf.Descriptors;

public class SecurityMetadataHelper extends BaseFieldMetadataHelper<Boolean> {
    public SecurityMetadataHelper() {
        super(Security.securedField);
    }

    public Boolean isSecured(Descriptors.FieldDescriptor fieldDescriptor) {
        if (hasExtension(fieldDescriptor)) {
            return getExtension(fieldDescriptor).get();
        }
        return false;
    }
}
