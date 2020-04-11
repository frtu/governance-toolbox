package ${groupId}.graphql;

import ${groupId}.${artifactId}.${DatamodelClassName};
import ${groupId}.${artifactId}.QueryType;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphql_java_generator.exception.GraphQLRequestExecutionException;
import com.graphql_java_generator.exception.GraphQLRequestPreparationException;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.*;
import javax.ws.rs.client.ClientBuilder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;

@Slf4j
public class TestQueryMain {
    static final String graphqlBaseUrl = "https://localhost:8080/graphql";

    public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, GraphQLRequestExecutionException, GraphQLRequestPreparationException {
        final ClientBuilder clientBuilder = ClientBuilder.newBuilder();

        final SSLContext sslContext = getNoCheckSslContext();
        clientBuilder.sslContext(sslContext);
        LOGGER.debug("Adding skip sslContext");

        final HostnameVerifier hostnameVerifier = getHostnameVerifier();
        clientBuilder.hostnameVerifier(hostnameVerifier);
        LOGGER.debug("Adding skip hostname");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        LOGGER.debug("Add ACCEPT_SINGLE_VALUE_AS_ARRAY to avoid single and array mix from GraphQL returned json");

        QueryType queryType = new QueryType(graphqlBaseUrl, clientBuilder.build(), objectMapper);
        final List<${DatamodelClassName}> list = queryType.find("{id name value eventTime}", "");
        System.out.println(list);
    }

    public static SSLContext getNoCheckSslContext() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLSv1");

        // Very, very bad. Don't do that in production !
        KeyManager[] keyManagers = null;
        TrustManager[] trustManager = {new NoOpTrustManager()};
        SecureRandom secureRandom = new SecureRandom();

        sslContext.init(keyManagers, trustManager, secureRandom);

        return sslContext;
    }

    public static HostnameVerifier getHostnameVerifier() {
        // Very, very bad. Don't do that in production !
        return new NoOpHostnameVerifier();
    }

    public static class NoOpTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    public static class NoOpHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    }
}
