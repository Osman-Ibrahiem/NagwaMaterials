package com.data.core.datasource.json

import com.domain.common.model.MaterialFile

interface MaterialJsonDataSource {

    suspend fun getAllFiles(): List<MaterialFile>
}