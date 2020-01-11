package com.github.frtu.proto.metadata.utils;

import com.github.frtu.proto.metadata.Person;
import com.google.protobuf.Descriptors;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProtoUtilTest {
    @Test
    public void buildFieldDescriptorMap() {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        final Descriptors.Descriptor descriptor = Person.getDescriptor();

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        final Map<String, Descriptors.FieldDescriptor> fieldDescriptorMap = ProtoUtil.buildFieldsMap(descriptor);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        assertNotNull(fieldDescriptorMap.get("name"));
        assertEquals(0, fieldDescriptorMap.get("name").getIndex());

        assertNotNull(fieldDescriptorMap.get("id"));
        assertEquals(1, fieldDescriptorMap.get("id").getIndex());

        assertNotNull(fieldDescriptorMap.get("email"));
        assertEquals(2, fieldDescriptorMap.get("email").getIndex());

        assertNotNull(fieldDescriptorMap.get("phones"));
        assertEquals(3, fieldDescriptorMap.get("phones").getIndex());
    }
}