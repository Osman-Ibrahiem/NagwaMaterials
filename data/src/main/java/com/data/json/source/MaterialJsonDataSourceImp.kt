package com.data.json.source

import android.content.Context
import com.data.core.datasource.json.MaterialJsonDataSource
import com.data.json.mapper.MaterialFileMapper
import com.data.json.model.MaterialFileJson
import com.domain.common.model.MaterialFile
import com.domain.core.di.annotations.qualifiers.AppContext
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MaterialJsonDataSourceImp @Inject constructor(
    @AppContext private val context: Context,
    private val materialFileMapper: MaterialFileMapper,
) : MaterialJsonDataSource {

    override suspend fun getAllFiles(): List<MaterialFile> {
        val jsonString = context.assets.open("getListOfFilesResponse.json")
            .bufferedReader()
            .use { it.readText() }
        val listFilesType = object : TypeToken<List<MaterialFileJson>>() {}.type
        val response = Gson().fromJson<List<MaterialFileJson>>(jsonString, listFilesType)
        return response.map(materialFileMapper::mapFromJson)
    }
}