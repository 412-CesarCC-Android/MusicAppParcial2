package com.example.cesarcazaresmusicapp.data.api

import com.example.cesarcazaresmusicapp.data.model.Album
import com.example.cesarcazaresmusicapp.data.model.AlbumDetail
import retrofit2.http.GET
import retrofit2.http.Path

interface MusicApi {
    @GET("api/albums")
    suspend fun getAlbums(): List<Album>

    @GET("api/albums/{id}")
    suspend fun getAlbumDetail(@Path("id") id: String): AlbumDetail
}
