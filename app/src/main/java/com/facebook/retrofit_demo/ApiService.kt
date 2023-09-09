package com.facebook.retrofit_demo

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class ApiService (context: Context) {

    companion object {
        private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }

    private val retrofit: Retrofit

    init {
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB cache size (adjust as needed)
        val cacheDirectory = File(context.cacheDir, "http-cache")
        val cache = Cache(cacheDirectory, cacheSize)

        val okHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getService(): JsonPlaceHolderApi {
        return retrofit.create(JsonPlaceHolderApi::class.java)
    }
}