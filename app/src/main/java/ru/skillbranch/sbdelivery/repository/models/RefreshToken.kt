package ru.skillbranch.sbdelivery.repository.models

import com.google.gson.annotations.SerializedName

data class RefreshToken(
    @SerializedName("refreshToken") val refreshToken: String
)