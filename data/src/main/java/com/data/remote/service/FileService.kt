package com.data.remote.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface FileService {

    companion object {
        operator fun invoke(retrofit: Retrofit) = retrofit.create<FileService>()
    }

    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): Response<ResponseBody>
}