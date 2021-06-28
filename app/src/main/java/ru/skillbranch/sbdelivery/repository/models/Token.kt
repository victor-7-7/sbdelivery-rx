package ru.skillbranch.sbdelivery.repository.models

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("accessToken") val accessToken: String
)