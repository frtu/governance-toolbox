package com.github.frtu.data.security.protobuf;

import java.util.Iterator;
import java.util.Map;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.protobuf.Descriptors;
import com.paypal.ppcn.data.protobuf.JSONProtobufDataSerializer;
import com.paypal.ppcn.demo.withdrawal.enums.Hello;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ProtobufSanitationPrinterTest {

    @Test
    public void toJSON() throws JsonProcessingException {
        final JSONProtobufDataSerializer jsonProtobufDataSerializer = new JSONProtobufDataSerializer();

        final Hello.HelloWorld helloWorld = Hello.HelloWorld.newBuilder().setMessage("msg").setPassword("password").build();
//        final String json = jsonProtobufDataSerializer.toJSON(helloWorld);
//        System.out.println(json);


        Iterator var3 = helloWorld.getAllFields().entrySet().iterator();
        while (var3.hasNext()) {
            Map.Entry<Descriptors.FieldDescriptor, Object> field = (Map.Entry) var3.next();

            Descriptors.FieldDescriptor key = field.getKey();
            Object value = field.getValue();
            final Boolean extension = key.getOptions().getExtension(Hello.securedField);
            LOGGER.info("BackendListener=key:{}, value:{} secured:{}", key, value, extension);
        }

//        final Descriptors.Descriptor descriptor = Hello.HelloWorld.getDescriptor();
//        final Object extension = descriptor.getOptions().getExtension(Hello.securedField);

        //        HelloWorld.getDescriptor().getOptions().getExtension(Hello.securedField);
//        //---------------------------------------
//        // 1. Prepare
//        //---------------------------------------
//        final String sessionId = "session-id";
//        final String externalAccountId = "acct-id";
//
//        final GetBalanceRequest getBalanceRequest = GetBalanceRequest.newBuilder().setSessionId(sessionId)
//                .setExternalAccountId(externalAccountId).build();
//
//        //---------------------------------------
//        // 2. Execute
//        //---------------------------------------
//        final ProtobufDataSerializer jsonProtobufDataSerializer = new ProtobufSanitationPrinter();
//        final String json = jsonProtobufDataSerializer.toJSON(getBalanceRequest);
//        LOGGER.debug(json);
//
//        //---------------------------------------
//        // 3. Test
//        //---------------------------------------
//        ObjectMapper objectMapper = new ObjectMapper();
//        final HashMap<String, String> resultMap = objectMapper
//                .readValue(json, new TypeReference<HashMap<String, String>>() {
//                });
//
//        assertEquals(sessionId, resultMap.get("session_id"));
//        assertEquals(externalAccountId, resultMap.get("external_account_id"));
    }
}
