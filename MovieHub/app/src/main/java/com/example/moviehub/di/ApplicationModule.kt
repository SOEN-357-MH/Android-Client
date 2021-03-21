package com.example.moviehub.di

import com.example.moviehub.misc.Constants
import com.example.moviehub.repository.DefaultMainRepository
import com.example.moviehub.repository.DefaultMediaRepository
import com.example.moviehub.repository.MainRepository
import com.example.moviehub.repository.MediaRepository
import com.example.moviehub.retrofit.MainApiService
import com.example.moviehub.retrofit.MediaApiService
import com.example.moviehub.retrofit.OAuthInterceptor
import com.example.moviehub.utils.DispatcherProvider
import com.example.moviehub.utils.MainRetrofit
import com.example.moviehub.utils.MediaRetrofit
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        try {
            val builder = OkHttpClient.Builder()
            val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(interceptor)
                .addInterceptor(OAuthInterceptor())
                .build()
            return builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)

        }
    }

    @Provides
    @Singleton
    @MainRetrofit
    fun provideRetrofitMain(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_ACCOUNT)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()

    @Provides
    @Singleton
    @MediaRetrofit
    fun provideRetrofitMedia(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_MEDIA)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()

    @Provides
    @Singleton
    fun provideMainApiService(@MainRetrofit retrofit: Retrofit): MainApiService = retrofit.create(MainApiService::class.java)

    @Provides
    @Singleton
    fun provideMediaApiService(@MediaRetrofit retrofit: Retrofit): MediaApiService = retrofit.create(MediaApiService::class.java)

    @Provides
    @Singleton
    fun provideMainRepository(api: MainApiService): MainRepository = DefaultMainRepository(api)

    @Provides
    @Singleton
    fun provideMediaRepositoryRepository(api: MediaApiService): MediaRepository = DefaultMediaRepository(api)

    @Provides
    @Singleton
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}