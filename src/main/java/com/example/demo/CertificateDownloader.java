package com.example.demo;

import javax.net.ssl.*;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class CertificateDownloader {
    public static X509Certificate[] downloadCertificates(String[] hosts, int port) throws Exception {

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) {}
            public void checkServerTrusted(X509Certificate[] chain, String authType) {}
            public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
        }}, new java.security.SecureRandom());

        SSLSocketFactory factory = sslContext.getSocketFactory();

        List<X509Certificate> serverCertsList = new ArrayList<>();

        for(int i=0;i<hosts.length;i++) {
            SSLSocket socket = (SSLSocket) factory.createSocket(hosts[i], port);
            socket.startHandshake();

            X509Certificate[] serverCerts = (X509Certificate[]) socket.getSession().getPeerCertificates();
            Collections.addAll(serverCertsList, serverCerts);
        }



        for (X509Certificate cert : serverCertsList) {
            System.out.println("Subject: " + cert.getSubjectDN());
            System.out.println("Issuer: " + cert.getIssuerDN());
            // You can save the certificate to a file or keystore here
        }
        return serverCertsList.stream().toArray(X509Certificate[]::new);
    }
}
