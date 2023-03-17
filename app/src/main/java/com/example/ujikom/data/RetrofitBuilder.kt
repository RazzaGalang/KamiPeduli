package com.example.ujikom.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext

object RetrofitBuilder {

    private const val BASE_URL = "https://d143-125-164-22-67.ap.ngrok.io"

    fun getRetrofit(): ApiService{
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .sslSocketFactory(SSLContext.getDefault().socketFactory, TrustAllCerts())
            .hostnameVerifier(HostnameVerifier { _, _ -> true })
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}