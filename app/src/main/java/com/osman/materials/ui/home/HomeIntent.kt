package com.osman.materials.ui.home

import com.domain.common.model.MaterialFile
import com.osman.materials.base.BaseIntent

sealed class HomeIntent : BaseIntent {

    object GetAllFiles : HomeIntent()
    class DownloadFile(val materialFile: MaterialFile) : HomeIntent()
}
