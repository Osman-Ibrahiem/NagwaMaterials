package com.domain.common.viewstate

import com.domain.common.model.MaterialFile
import com.domain.core.viewstate.BaseVS

class DownloadFileStateResult(
    val file: MaterialFile,
) : BaseVS() {
    companion object {
        const val TYPE = 2001
    }
}