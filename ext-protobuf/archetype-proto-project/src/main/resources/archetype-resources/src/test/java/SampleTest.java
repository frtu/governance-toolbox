package ${groupId};

import static org.junit.Assert.*;
import org.junit.Test;

import com.github.frtu.proto.metadata.SecuredFieldHelper;
import com.github.frtu.proto.metadata.utils.ProtoUtil;
import com.google.protobuf.Descriptors;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import java.util.Map;

public class SampleTest {
    @Test
    public void printJSON() throws InvalidProtocolBufferException {
        ${DatamodelClassName} sample = ${DatamodelClassName}.newBuilder()
                .setId(1234)
                .setName("fred")
                .build();

        String json = JsonFormat.printer()
                .preservingProtoFieldNames()
                .includingDefaultValueFields()
                .print(sample);
        System.out.println(json);
    }

    @Test
    public void isSecured() {
        SecuredFieldHelper securedFieldHelper = new SecuredFieldHelper();

        final Descriptors.Descriptor descriptor = SampleData.getDescriptor();
        final Map<String, Descriptors.FieldDescriptor> fieldDescriptorMap = ProtoUtil.buildFieldsMap(descriptor);

        assertFalse(securedFieldHelper.isSecured(fieldDescriptorMap.get("name")));
        assertTrue(securedFieldHelper.isSecured(fieldDescriptorMap.get("value")));
    }
}
