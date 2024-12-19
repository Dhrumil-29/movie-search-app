package com.spidy.moviesearchapp.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//object RetrofitClient {
//
//    private val retrofitClient by lazy {
//        Retrofit.Builder()
//            .baseUrl(ApiConstant.BASE_URL)
//            .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
//                level = HttpLoggingInterceptor.Level.BODY
//            }).build())
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    val apiService: ApiService by lazy { retrofitClient.create(ApiService::class.java) }
//}