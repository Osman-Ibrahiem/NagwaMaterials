package com.domain.common.repository

import com.domain.common.model.MaterialFile
import com.domain.core.viewstate.BaseVS
import kotlinx.coroutines.flow.Flow

interface MaterialsRepository {

    fun getAllFiles(): Flow<BaseVS>
    fun downloadFile(materialFile: MaterialFile): Flow<BaseVS>

}
