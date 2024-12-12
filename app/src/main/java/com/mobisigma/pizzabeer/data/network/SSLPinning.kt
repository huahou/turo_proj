package com.mobisigma.pizzabeer.data.network

import android.content.Context
import com.mobisigma.pizzabeer.R
import okhttp3.OkHttpClient
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

fun OkHttpClient.Builder.sslPinning(context: Context): OkHttpClient.Builder{
    try {
        val cf = CertificateFactory.getInstance("X.509")
        val caInput = context.resources.openRawResource(R.raw.yelp)
        val ca: Certificate

        try {
            ca = cf.generateCertificate(caInput)
        } finally {
            caInput.close()
        }

        // Create a KeyStore containing our trusted CAs
        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", ca)

        // Create a TrustManager that trusts the CAs in our KeyStore
        val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
        tmf.init(keyStore)

        // Create an SSLContext that uses our TrustManager
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tmf.trustManagers, null)

        sslSocketFactory(
            sslContext.socketFactory,
            tmf.trustManagers[0] as X509TrustManager
        )

        return this
    } catch (e: Exception) {
        throw RuntimeException(e)
    }
}
