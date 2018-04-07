package com.example.springboot.demo.httpclient.asyn.entity;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
/***
 * @Date 2018/4/3
 * @Description  SSL证书
 * @author zhanghesheng
 * */
public class TrueX509TrustManager implements X509TrustManager {

    private X509TrustManager standardTrustManager = null;

    public TrueX509TrustManager(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException {
        String algo = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory factory = TrustManagerFactory.getInstance(algo);
        factory.init(keystore);
        TrustManager[] trustmanagers = factory.getTrustManagers();
        if (trustmanagers.length == 0) {
            throw new NoSuchAlgorithmException(algo + " trust manager not supported");
        } else {
            this.standardTrustManager = (X509TrustManager)trustmanagers[0];
        }
    }

    public boolean isClientTrusted(X509Certificate[] certificates) {
        return true;
    }

    public boolean isServerTrusted(X509Certificate[] certificates) {
        return true;
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return this.standardTrustManager.getAcceptedIssuers();
    }
    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    }
    @Override
    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    }
}
