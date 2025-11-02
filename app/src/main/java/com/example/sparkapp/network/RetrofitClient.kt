package com.example.sparkapp.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {

    // -----------------------------------------------------------------
    // !! IMPORTANT !!
    // YOU MUST CHANGE THIS URL
    //
    // How to find your IP:
    // - Windows: Open 'cmd' and type 'ipconfig'. Look for 'IPv4 Address'.
    // - Mac/Linux: Open 'Terminal' and type 'ifconfig'. Look for 'inet'.
    //
    // It will look like "http://192.168.1.10/"
    // -----------------------------------------------------------------

    //
    // ▼▼▼ THIS IS THE FIX ▼▼▼
    //
    // 1. It must be in quotes ("...") to be a String.
    // 2. It must start with "http://" or "https://".
    // 3. It must end with a "/" for Retrofit.
    //
    private const val BASE_URL = "http://192.168.31.246/" // <-- 1. ADDED QUOTES, 2. ADDED "http://", 3. ADDED "/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ApiService::class.java)
    }
}