package com.data.json.mapper

import android.os.Environment
import com.data.json.model.MaterialFileJson
import com.domain.common.model.FileStatus
import com.domain.common.model.FileType
import com.domain.common.model.MaterialFile
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class MaterialFileMapper @Inject constructor() : Mapper<MaterialFileJson?, MaterialFile> {

    override fun mapFromJson(
        type: MaterialFileJson?,
    ): MaterialFile {
        val file = File(type?.url ?: "")
        val fileName = "${file.nameWithoutExtension} (${type?.name}).${file.extension}"
        val target = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
                    + "/Nagwa/" + fileName)
        val isDownloaded = target.exists() && target.isFile

        return MaterialFile(
            id = type?.id ?: 0,
            title = type?.name ?: "",
            url = if (isDownloaded) target.absolutePath else type?.url ?: "",
            type = when (type?.type) {
                "VIDEO" -> FileType.VIDEO
                "PDF" -> FileType.PDF
                else -> FileType.UNKNOWN
            },
            status = if (isDownloaded) FileStatus.Downloaded else FileStatus.Idle
        )
    }
}
