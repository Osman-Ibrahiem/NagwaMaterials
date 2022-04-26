package com.data.json.model

import com.google.gson.annotations.SerializedName

data class MaterialFileJson(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("name")
    val name: String?,
)