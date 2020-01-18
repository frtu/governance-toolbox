package com.github.frtu.proto.metadata;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessage;

import java.util.Optional;

public class BaseMessageMetadataHelper<T> {
    protected final GeneratedMessage.GeneratedExtension<DescriptorProtos.MessageOptions, T> messageOptions;

    public BaseMessageMetadataHelper(GeneratedMessage.GeneratedExtension<DescriptorProtos.MessageOptions, T> messageOptions) {
        this.messageOptions = messageOptions;
    }

    public Optional<T> getExtension(Descriptors.Descriptor messageDescriptor) {
        if (messageDescriptor == null) {
            return Optional.empty();
        }
        final DescriptorProtos.MessageOptions options = messageDescriptor.getOptions();
        return Optional.of(options.getExtension(messageOptions));
    }

    public boolean hasExtension(Descriptors.Descriptor messageDescriptor) {
        return getExtension(messageDescriptor).isPresent();
    }
}
