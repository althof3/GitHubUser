package com.altop.githubuserapp.api

import com.altop.githubuserapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
  companion object {
    fun getApiService(): ApiService {
      val loggingInterceptor = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
      } else {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
      }
      
      val headerInterceptor = Interceptor { chain ->
        var request = chain.request()
        request = request.newBuilder()
          .addHeader("Authorization", "token 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").build()
        
        chain.proceed(request)
      }
      
      val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor)
          .addInterceptor(headerInterceptor)
        .build()
      val retrofit = Retrofit.Builder().baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create()).client(client).build()
      return retrofit.create(ApiService::class.java)
    }
  }
}