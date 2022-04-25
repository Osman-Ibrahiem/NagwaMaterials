package com.domain.model

sealed class FileStatus {
    object Downloaded : FileStatus()
    class Downloading(val progress: Int) : FileStatus()
    object Idle : FileStatus()
    object Ready : FileStatus()
    object Error : FileStatus()
}
