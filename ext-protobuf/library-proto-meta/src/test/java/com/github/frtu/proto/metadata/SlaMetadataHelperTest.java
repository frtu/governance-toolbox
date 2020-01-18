package com.github.frtu.proto.metadata;

import com.github.frtu.proto.metadata.sla.DataSLA;
import com.google.protobuf.Descriptors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Slf4j
public class SlaMetadataHelperTest {
    private SlaMetadataHelper slaMetadataHelper = new SlaMetadataHelper();

    @Test
    public void getSLA() {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        final Descriptors.Descriptor personDescriptor = Person.getDescriptor();

        //--------------------------------------
        // 2 & 3. Execute & Validate
        //--------------------------------------
        assertTrue(slaMetadataHelper.hasSLA(personDescriptor));

        final DataSLA dataSLA = slaMetadataHelper.getSLA(personDescriptor).get();
        LOGGER.debug(dataSLA.toString());
        assertEquals(DataSLA.RELIABLE, dataSLA);

//        final Descriptors.EnumValueDescriptor valueDescriptor = dataSLA.getValueDescriptor();
//        LOGGER.debug(valueDescriptor.getOptions().getExtension(DataSLA));
    }
}