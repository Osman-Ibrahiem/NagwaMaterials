package com.osman.materials.di

import android.app.Application
import android.content.Context
import com.domain.core.di.annotations.qualifiers.AppContext
import com.domain.core.di.annotations.qualifiers.AppRemoteUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {

    @AppRemoteUrl
    @Provides
    fun serviceURl(): String {
        return "https://www.nagwa.com/"
    }

    @AppContext
    @Provides
    fun context(application: Application): Context {
        return application.applicationContext
    }

}