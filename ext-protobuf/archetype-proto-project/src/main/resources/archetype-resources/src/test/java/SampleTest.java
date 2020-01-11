package ${groupId};

import com.google.protobuf.util.JsonFormat;

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
}
