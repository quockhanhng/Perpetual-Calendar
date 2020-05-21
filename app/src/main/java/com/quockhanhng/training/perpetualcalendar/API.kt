package com.quockhanhng.training.perpetualcalendar

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class API {
    companion object {
        private const val API_KEY = "255960def76b8ffd82c058d733acd0c8"
        private const val BASE_URL = "http://api.openweathermap.org/"

        fun create(): OpenWeatherMapServices {
            val clientBuilder = OkHttpClient.Builder()
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(httpLoggingInterceptor)

            val apiAuthInterceptor = Interceptor { chain ->
                var request = chain.request()
                val originalUrl = request.url()
                val url = originalUrl.newBuilder()
                    .addQueryParameter("units", "metric")
                    .addQueryParameter("lang", "vi")
                    .addQueryParameter("appid", API_KEY)
                    .build()
                val requestBuilder = request.newBuilder().url(url)
                request = requestBuilder.build()
                chain.proceed(request)
            }

            clientBuilder.addInterceptor(apiAuthInterceptor)
                .addInterceptor(httpLoggingInterceptor)

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(clientBuilder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(OpenWeatherMapServices::class.java)
        }
    }
}
