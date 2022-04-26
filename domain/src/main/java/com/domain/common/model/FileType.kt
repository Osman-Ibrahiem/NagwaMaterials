package com.domain.common.model

sealed class FileType {
    object PDF : FileType()
    object VIDEO : FileType()
    object UNKNOWN : FileType()
}
