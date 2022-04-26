package com.data.core.datasource.remote

import okhttp3.ResponseBody

interface FileRemoteDataSource {

    suspend fun downloadFile(url: String): ResponseBody
}