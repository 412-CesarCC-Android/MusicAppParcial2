package com.example.cesarcazaresmusicapp.data.model

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("artist") val artist: String,
    @SerializedName("image") val cover: String,
    @SerializedName("year") val year: String? = null
)
