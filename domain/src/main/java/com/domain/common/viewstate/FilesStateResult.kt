package com.domain.common.viewstate

import com.domain.common.model.MaterialFile
import com.domain.core.viewstate.BaseVS

class FilesStateResult(
    val files: List<MaterialFile>,
) : BaseVS() {
    companion object {
        const val TYPE = 2000
    }
}