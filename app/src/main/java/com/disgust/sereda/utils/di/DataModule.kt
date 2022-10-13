package com.disgust.sereda.utils.di

import com.disgust.sereda.utils.Constants.API_KEY
import com.disgust.sereda.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideRetrofitClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(getOkHttpClient())
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()
        okHttpBuilder.addInterceptor(object : Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                val requestBuilder = chain.request()
                    .newBuilder()
                    .addHeader("x-api-key", API_KEY)

                val request = requestBuilder.build()
                val response = chain.proceed(request)

                return response
            }
        })

        return okHttpBuilder.build()
    }

}