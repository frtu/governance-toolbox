package com.github.frtu.proto.metadata.utils;

import com.google.protobuf.Descriptors;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProtoUtil {
    public static Map<String, Descriptors.FieldDescriptor> buildFieldsMap(Descriptors.Descriptor descriptor) {
        final List<Descriptors.FieldDescriptor> fields = descriptor.getFields();
        return fields.stream().collect(
                Collectors.toMap(Descriptors.FieldDescriptor::getName, Function.identity()));
    }
}
