package com.data.remote.source

import com.data.core.datasource.remote.FileRemoteDataSource
import com.data.remote.service.FileService
import com.domain.core.RemoteException
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileRemoteDataSourceImp @Inject constructor(
    private val service: FileService,
) : FileRemoteDataSource {

    override suspend fun downloadFile(url: String): ResponseBody {
        val request = service.downloadFile(url)
        val body = request.body()
        return if (request.isSuccessful && body != null) {
            body
        } else throw RemoteException(request.code(), request.errorBody().toString())
    }
}