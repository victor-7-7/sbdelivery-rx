package ru.skillbranch.sbdelivery.repository.models

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("categoryId") val categoryId: String,
    @SerializedName("name") val name: String
)