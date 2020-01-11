package com.github.frtu.proto.metadata;

import com.google.protobuf.Descriptors;

public class SecuredFieldHelper extends BaseAnnotatedHelper<Boolean> {
    public SecuredFieldHelper() {
        super(Security.securedField);
    }

    public Boolean isSecured(Descriptors.FieldDescriptor fieldDescriptor) {
        if (hasExtension(fieldDescriptor)) {
            return getExtension(fieldDescriptor).get();
        }
        return false;
    }
}
