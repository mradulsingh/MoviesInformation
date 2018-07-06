package com.android.example.github.api;

import com.android.example.github.viewmodel.TLSSocketFactory;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CertificatePinner;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
//import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by pdubey on 22/03/18.
 */

public class TrustingNetworkClient {

    private static TrustingNetworkClient trustingNetworkClient = null;

    private OkHttpClient client;
    private OkHttpClient.Builder clientBuilder;
    private TrustManager[] trustManagers;
    private HttpLoggingInterceptor httpLoggingInterceptor;
    private ConnectionSpec spec;
    private CertificatePinner certificatePinner;

    private TrustingNetworkClient(String hostname) throws KeyStoreException, CertificateException,
            NoSuchAlgorithmException, IOException, KeyManagementException,
            UnrecoverableKeyException {

        trustManagers = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
                            throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
                            throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };

        httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_1, TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();

        certificatePinner = new CertificatePinner.Builder()
                .add(hostname, "sha1/mBN/TTGneHe2Hq0yFG+SRt5nMZQ=")
                .add(hostname, "sha1/6CgvsAgBlX3PYiYRGedC0NZw7ys=")
                .build();

        clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .readTimeout(180, TimeUnit.SECONDS)
                .connectTimeout(180, TimeUnit.SECONDS)
                .connectionSpecs(Collections.singletonList(spec))
                .certificatePinner(certificatePinner)
                .sslSocketFactory(new TLSSocketFactory(trustManagers), new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
                            throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
                            throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                })
                .hostnameVerifier((s, sslSession) -> true);

        client = clientBuilder.build();
    }

    public static OkHttpClient getInstace(String hostname) throws CertificateException,
            UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException,
            KeyManagementException, IOException {

        if (trustingNetworkClient == null) {
            trustingNetworkClient = new TrustingNetworkClient(hostname);
        }
        return trustingNetworkClient.client;
    }
}
