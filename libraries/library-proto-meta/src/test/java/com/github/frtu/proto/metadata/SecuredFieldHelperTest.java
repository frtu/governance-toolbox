package com.github.frtu.proto.metadata;

import com.github.frtu.proto.metadata.utils.ProtoUtil;
import com.google.protobuf.Descriptors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Slf4j
public class SecuredFieldHelperTest {
    private SecuredFieldHelper securedFieldHelper = new SecuredFieldHelper();

    @Test
    public void isSecured() {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        final Descriptors.Descriptor descriptor = Person.getDescriptor();
        final Map<String, Descriptors.FieldDescriptor> fieldDescriptorMap = ProtoUtil.buildFieldsMap(descriptor);
        LOGGER.debug(fieldDescriptorMap.toString());

        //--------------------------------------
        // 2 & 3. Execute & Validate
        //--------------------------------------
        assertFalse(securedFieldHelper.isSecured(fieldDescriptorMap.get("name")));
        assertFalse(securedFieldHelper.isSecured(fieldDescriptorMap.get("id")));
        assertTrue(securedFieldHelper.isSecured(fieldDescriptorMap.get("email")));
        assertFalse(securedFieldHelper.isSecured(fieldDescriptorMap.get("phones")));
    }
}