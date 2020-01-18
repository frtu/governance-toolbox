package com.github.frtu.proto.metadata;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessage;

import java.util.Optional;

public class BaseFieldMetadataHelper<T> {
    protected final GeneratedMessage.GeneratedExtension<DescriptorProtos.FieldOptions, T> fieldExtention;

    public BaseFieldMetadataHelper(GeneratedMessage.GeneratedExtension<DescriptorProtos.FieldOptions, T> fieldExtention) {
        this.fieldExtention = fieldExtention;
    }

    public Optional<T> getExtension(Descriptors.FieldDescriptor fieldDescriptor) {
        if (fieldDescriptor == null) {
            return Optional.empty();
        }
        final DescriptorProtos.FieldOptions options = fieldDescriptor.getOptions();
        return Optional.of(options.getExtension(fieldExtention));
    }

    public boolean hasExtension(Descriptors.FieldDescriptor fieldDescriptor) {
        return getExtension(fieldDescriptor).isPresent();
    }
}
