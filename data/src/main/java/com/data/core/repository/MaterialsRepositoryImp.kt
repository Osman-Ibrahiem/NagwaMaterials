package com.data.core.repository

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.data.core.datasource.json.MaterialJsonDataSource
import com.data.core.datasource.remote.FileRemoteDataSource
import com.domain.common.model.FileStatus
import com.domain.common.model.FileType
import com.domain.common.model.MaterialFile
import com.domain.common.repository.MaterialsRepository
import com.domain.common.viewstate.DownloadFileStateResult
import com.domain.common.viewstate.FilesStateResult
import com.domain.core.Constants
import com.domain.core.di.annotations.qualifiers.AppContext
import com.domain.core.viewstate.BaseVS
import com.domain.core.viewstate.Failure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout
import java.io.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@ExperimentalCoroutinesApi
@Singleton
class MaterialsRepositoryImp @Inject constructor(
    @AppContext private val context: Context,
    private val materialJsonDataSource: MaterialJsonDataSource,
    private val fileRemoteDataSource: FileRemoteDataSource,
) : MaterialsRepository {

    override fun getAllFiles(): Flow<BaseVS> = flow {
        try {
            withTimeout(Constants.CACHE_TIMEOUT) {
                val files = materialJsonDataSource.getAllFiles()
                emit(FilesStateResult(files))
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            println("new Error ${throwable.message}")
            emit(Failure.get(error = throwable))
        }
    }

    override fun downloadFile(
        materialFile: MaterialFile,
    ): Flow<BaseVS> = flow {
        try {

            materialFile.status = FileStatus.Ready
            emit(DownloadFileStateResult(materialFile))

            val response = fileRemoteDataSource.downloadFile(materialFile.url)

            val mimeType = when (materialFile.type) {
                is FileType.PDF -> "application/pdf"
                is FileType.VIDEO -> "video/mp4"
                else -> ""
            }
            if (mimeType.isNotEmpty()) {

                val file = File(materialFile.url)
                val fileName =
                    "${file.nameWithoutExtension} (${materialFile.title}).${file.extension}"
                val lengthOfFile: Long = response.contentLength()
                val inputStream = BufferedInputStream(response.byteStream(), 8192)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                        put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/Nagwa")
                    }

                    val resolver = context.contentResolver

                    val uri =
                        resolver?.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                    if (uri != null) {

                        val outputStream = resolver.openOutputStream(uri)
                        emitAll(saveFile(materialFile, inputStream, outputStream, lengthOfFile))

                        materialFile.status = FileStatus.Downloaded
                        materialFile.url =
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/Nagwa/" + fileName
                        emit(DownloadFileStateResult(materialFile))
                    } else {
                        throw Throwable("uri is null")
                    }

                } else {
                    val target = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
                                + "/Nagwa/" + fileName)

                    val outputStream = FileOutputStream(target)
                    emitAll(saveFile(materialFile, inputStream, outputStream, lengthOfFile))

                    materialFile.status = FileStatus.Downloaded
                    materialFile.url = target.absolutePath
                    emit(DownloadFileStateResult(materialFile))
                }


            } else throw Throwable("mimeType is empty")

        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            println("new Error ${throwable.message}")
            emit(Failure.get(error = throwable))
        }
    }

    private suspend fun saveFile(
        materialFile: MaterialFile,
        inputStream: InputStream,
        outputStream: OutputStream?,
        lengthOfFile: Long,
    ): Flow<DownloadFileStateResult> = flow {
        materialFile.status = FileStatus.Downloading(0)
        emit(DownloadFileStateResult(materialFile))

        inputStream.use { input ->
            outputStream?.use { output ->
                input.copyTo(
                    output,
                    DEFAULT_BUFFER_SIZE,
                ) { totalBytesCopied: Long ->
                    val progress =
                        (totalBytesCopied * 100.0 / lengthOfFile).roundToInt()
                    materialFile.status = FileStatus.Downloading(progress)
                    emit(DownloadFileStateResult(materialFile))
                }
            }
        }
    }

    private suspend fun InputStream.copyTo(
        out: OutputStream,
        bufferSize: Int = DEFAULT_BUFFER_SIZE,
        onCopy: suspend (totalBytesCopied: Long) -> Unit,
    ): Long {

        var bytesCopied: Long = 0
        val buffer = ByteArray(bufferSize)
        var bytes = read(buffer)
        while (bytes >= 0) {
            out.write(buffer, 0, bytes)
            bytesCopied += bytes
            onCopy(bytesCopied)
            bytes = read(buffer)
        }
        return bytesCopied
    }
}