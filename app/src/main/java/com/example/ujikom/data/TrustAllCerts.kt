package com.example.ujikom.data

import android.annotation.SuppressLint
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

@SuppressLint("CustomX509TrustManager")
class TrustAllCerts : X509TrustManager {

    @SuppressLint("TrustAllX509TrustManager")
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        // Do nothing, trust all certificates
    }

    @SuppressLint("TrustAllX509TrustManager")
    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        // Do nothing, trust all certificates
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return arrayOf()
    }

    companion object {
        private val trustManager: TrustManager
            get() {
                try {
                    val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                    trustManagerFactory.init(null as KeyStore?)
                    val trustManagers = trustManagerFactory.trustManagers
                    if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                        throw IllegalStateException("Unexpected default trust managers: ${trustManagers.contentToString()}")
                    }
                    return trustManagers[0]
                } catch (e: NoSuchAlgorithmException) {
                    throw RuntimeException("Failed to obtain X.509 trust manager", e)
                } catch (e: KeyStoreException) {
                    throw RuntimeException("Failed to obtain X.509 trust manager", e)
                }
            }

        val sslSocketFactory by lazy {
            val sslContext = SSLContext.getInstance("TLS")
            val trustManagers = arrayOf(trustManager)
            val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
            keyManagerFactory.init(null, null)
            sslContext.init(keyManagerFactory.keyManagers, trustManagers, null)
            sslContext.socketFactory
        }

        val hostnameVerifier by lazy { HostnameVerifier { _, _ -> true } }
    }
}
