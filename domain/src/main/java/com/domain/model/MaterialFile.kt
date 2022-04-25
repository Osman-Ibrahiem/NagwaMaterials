package com.domain.model

data class MaterialFile(
    var id: Int = 0,
    var title: String = "",
    var url: String? = null,
    var type: FileType = FileType.UNKNOWN,
    var status: FileStatus = FileStatus.Ready,
)