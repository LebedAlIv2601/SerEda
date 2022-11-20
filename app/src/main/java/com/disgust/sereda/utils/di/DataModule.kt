package com.disgust.sereda.utils.di

import android.content.Context
import androidx.room.Room
import com.disgust.sereda.R
import com.disgust.sereda.utils.Constants.API_KEY
import com.disgust.sereda.utils.Constants.BASE_URL
import com.disgust.sereda.utils.Constants.SIGN_IN_REQUEST
import com.disgust.sereda.utils.Constants.SIGN_UP_REQUEST
import com.disgust.sereda.utils.db.SerEdaDatabase
import com.disgust.sereda.utils.firebase.FirebaseAuthHelper
import com.disgust.sereda.utils.firebase.FirebaseDatabaseHelper
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
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
    fun provideFirebaseAuthHelper(
        @ApplicationContext context: Context,
        @Named(SIGN_IN_REQUEST)
        signInRequest: BeginSignInRequest,
        @Named(SIGN_UP_REQUEST)
        signUpRequest: BeginSignInRequest
    ): FirebaseAuthHelper =
        FirebaseAuthHelper(
            oneTapClient = Identity.getSignInClient(context),
            database = FirebaseDatabase.getInstance(),
            signInRequest = signInRequest,
            signUpRequest = signUpRequest
        )

    @Provides
    @Named(SIGN_IN_REQUEST)
    fun provideSignInRequest(
        @ApplicationContext app: Context
    ) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(app.getString(R.string.web_client_id))
                .setFilterByAuthorizedAccounts(true)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()

    @Provides
    @Named(SIGN_UP_REQUEST)
    fun provideSignUpRequest(
        @ApplicationContext app: Context
    ) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(app.getString(R.string.web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()

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