package com.domain.common.model

data class MaterialFile(
    var id: Int = 0,
    var title: String = "",
    var url: String = "",
    var type: FileType = FileType.UNKNOWN,
    var status: FileStatus = FileStatus.Idle,
)