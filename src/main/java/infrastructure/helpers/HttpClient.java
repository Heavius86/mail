package infrastructure.helpers;

import infrastructure.utils.Loggers;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

public class HttpClient {
    private String url;
    private String jsonBody;
    private final HashMap<String, String> propertiesMap = new HashMap<>();

    public String sendPost() throws IOException {
        try (CloseableHttpClient httpclient = httpClientWithoutCheckSsl()) {
            HttpPost httpPost = new HttpPost(url);
            propertiesMap.forEach((key, value) -> httpPost.addHeader(key, value));
            HttpEntity stringEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
            httpPost.setEntity(stringEntity);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            Loggers.FILE_LOGGER.info(httpPost.getMethod() + " " + url + " " + response.getStatusLine().toString());
            Assertions.assertTrue(checkCode(response.getStatusLine().getStatusCode()));

            return EntityUtils.toString(response.getEntity());
        }
    }

    public String sendGet() throws IOException {
        try (CloseableHttpClient httpclient = httpClientWithoutCheckSsl()) {
            HttpGet httpGet = new HttpGet(url);
            propertiesMap.forEach((key, value) -> httpGet.addHeader(key, value));
            CloseableHttpResponse response = httpclient.execute(httpGet);
            Loggers.FILE_LOGGER.info(httpGet.getMethod() + " " + url + " " + response.getStatusLine().toString());
            Assertions.assertTrue(checkCode(response.getStatusLine().getStatusCode()));

            return EntityUtils.toString(response.getEntity());
        }
    }

    public String sendPut() throws IOException {
        try (CloseableHttpClient httpclient = httpClientWithoutCheckSsl()) {
            HttpPut httpPut = new HttpPut(url);
            propertiesMap.forEach((key, value) -> httpPut.addHeader(key, value));
            HttpEntity stringEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
            httpPut.setEntity(stringEntity);
            CloseableHttpResponse response = httpclient.execute(httpPut);
            Loggers.FILE_LOGGER.info(httpPut.getMethod() + " " + url + " " + response.getStatusLine().toString());
            Assertions.assertTrue(checkCode(response.getStatusLine().getStatusCode()));

            return EntityUtils.toString(response.getEntity());
        }
    }

    public String sendDelete() throws IOException {
        try (CloseableHttpClient httpclient = httpClientWithoutCheckSsl()) {
            HttpDelete httpDelete = new HttpDelete(url);
            propertiesMap.forEach((key, value) -> httpDelete.addHeader(key, value));
            CloseableHttpResponse response = httpclient.execute(httpDelete);
            Loggers.FILE_LOGGER.info(httpDelete.getMethod() + " " + url + " " + response.getStatusLine().toString());
            Assertions.assertTrue(checkCode(response.getStatusLine().getStatusCode()));

            return EntityUtils.toString(response.getEntity());
        }
    }

    public HttpClient setUrl(String url) {
        this.url = url;

        return this;
    }

    public HttpClient setJsonBody(String jsonBody) {
        this.jsonBody = jsonBody;

        return this;
    }

    public HttpClient setProperty(String header, String value) {
        propertiesMap.put(header, value);

        return this;
    }

    private boolean checkCode(int code){
        if(code >= 200 && code < 300){
            return true;
        }
        return false;
    }

    public boolean checkCode() throws IOException {
        try (CloseableHttpClient httpclient = httpClientWithoutCheckSsl()) {
            HttpGet httpGet = new HttpGet(url);
            propertiesMap.forEach((key, value) -> httpGet.addHeader(key, value));
            CloseableHttpResponse response = httpclient.execute(httpGet);
            Loggers.FILE_LOGGER.info(httpGet.getMethod() + " " + url + " " + response.getStatusLine().toString());
           return checkCode(response.getStatusLine().getStatusCode());
        }
    }

    //Создаем кастомный CloseableHttpClient который игнорирует SSL
    private static CloseableHttpClient httpClientWithoutCheckSsl() {
        TrustStrategy acceptAll = (X509Certificate[] chain, String authType) -> true;
        SSLContext sslContext = null;
        try {
            sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        return HttpClients.custom()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
                .build();
    }
}
