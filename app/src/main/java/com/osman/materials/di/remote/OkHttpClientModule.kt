package com.osman.materials.di.remote

import android.content.Context
import com.domain.core.di.annotations.qualifiers.AppContext
import com.osman.materials.di.ApplicationModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

@InstallIn(SingletonComponent::class)
@Module(includes = [ApplicationModule::class])
class OkHttpClientModule {

    @Provides
    fun cache(@AppContext context: Context): Cache {
        val cacheSize = (5 * 1024 * 1024).toLong()
        return Cache(context.cacheDir, cacheSize)
    }

    @Provides
    fun okHttpClientAuth(
        @AppContext context: Context,
    ): OkHttpClient {
//        val httpCacheDirectory = File(context.cacheDir, "responses")
//        val cacheSize = 10 * 1024 * 1024 // 10 MiB
//        val cache = Cache(httpCacheDirectory, cacheSize.toLong())
        return OkHttpClient()
            .newBuilder()
//            .cache(cache)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
//                .addNetworkInterceptor (REWRITE_CACHE_CONTROL_INTERCEPTOR)
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()



                request.header("Content-Type", "application/json")
                    .method(original.method(), original.body())
//                    if (!NetworkUtils.isConnected()) {
//                        val cacheControl = CacheControl.Builder()
//                                .maxStale(7, TimeUnit.DAYS)
//                                .build()
//
//                        request.cacheControl(cacheControl)
//
//                    }
                chain.proceed(request.build())
            }
            .build()
    }

}