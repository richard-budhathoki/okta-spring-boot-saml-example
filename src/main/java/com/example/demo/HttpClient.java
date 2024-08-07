package com.example.demo;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

public class HttpClient {
    public static void main(String[] args) throws Exception {
        String[] hosts = {"okta.com"};
        int port = 443;

        X509Certificate[] certificates = CertificateDownloader.downloadCertificates(hosts, port);
        KeyStore trustStore = TrustStoreManager.createTrustStore(certificates);
        SSLContext sslContext = SSLContextInitializer.createSSLContext(trustStore);

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        URL url = new URL("https://" + hosts[0]);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.connect();

        InputStream response = connection.getInputStream();
        // Process the response...
    }
}
