package com.disgust.sereda.utils.di

import android.content.Context
import androidx.room.Room
import com.disgust.sereda.utils.Constants.API_KEY
import com.disgust.sereda.utils.Constants.BASE_URL
import com.disgust.sereda.utils.db.SerEdaDatabase
import com.disgust.sereda.utils.firebase.FirebaseAuthHelper
import com.disgust.sereda.utils.firebase.FirebaseDatabaseHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
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

    @Singleton
    @Provides
    fun provideFirebaseAuthHelper(): FirebaseAuthHelper = FirebaseAuthHelper()

    @Singleton
    @Provides
    fun provideFirebaseDatabaseHelper(): FirebaseDatabaseHelper = FirebaseDatabaseHelper()

    private fun getOkHttpClient(): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()
        okHttpBuilder.addInterceptor { chain ->
            val requestBuilder = chain.request()
                .newBuilder()
                .addHeader("x-api-key", API_KEY)

            val request = requestBuilder.build()
            val response = chain.proceed(request)

            response
        }

//        val logging = HttpLoggingInterceptor()
//        logging.level = HttpLoggingInterceptor.Level.BODY
//        okHttpBuilder.addInterceptor(logging)

        return okHttpBuilder.build()
    }

    @Singleton
    @Provides
    fun provideLocalDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        SerEdaDatabase::class.java,
        "ser_eda_db"
    ).build()

}