package com.osman.materials.di.remote

import com.domain.core.di.annotations.qualifiers.AppRemoteUrl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.osman.materials.di.ApplicationModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module(includes = [OkHttpClientModule::class, ApplicationModule::class])
class NetworkModule {

    @Provides
    fun retrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        @AppRemoteUrl baseUrl: String,
    ): Retrofit {
        return Retrofit.Builder().client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    fun gson(): Gson {
        val gsonBuilder = GsonBuilder()
            .setLenient()
        return gsonBuilder.create()
    }

    @Provides
    fun gsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }


}