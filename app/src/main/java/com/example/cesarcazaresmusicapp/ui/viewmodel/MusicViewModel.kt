package com.example.cesarcazaresmusicapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cesarcazaresmusicapp.data.api.RetrofitClient
import com.example.cesarcazaresmusicapp.data.model.Album
import com.example.cesarcazaresmusicapp.data.model.AlbumDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class MusicViewModel : ViewModel() {

    private val _albumsState = MutableStateFlow<UiState<List<Album>>>(UiState.Loading)
    val albumsState: StateFlow<UiState<List<Album>>> = _albumsState.asStateFlow()

    private val _albumDetailState = MutableStateFlow<UiState<AlbumDetail>>(UiState.Loading)
    val albumDetailState: StateFlow<UiState<AlbumDetail>> = _albumDetailState.asStateFlow()

    init {
        fetchAlbums()
    }

    fun fetchAlbums() {
        viewModelScope.launch {
            _albumsState.value = UiState.Loading
            try {
                val albums = RetrofitClient.api.getAlbums()
                _albumsState.value = UiState.Success(albums)
            } catch (e: Exception) {
                _albumsState.value = UiState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun fetchAlbumDetail(id: String) {
        viewModelScope.launch {
            _albumDetailState.value = UiState.Loading
            try {
                val detail = RetrofitClient.api.getAlbumDetail(id)
                _albumDetailState.value = UiState.Success(detail)
            } catch (e: Exception) {
                _albumDetailState.value = UiState.Error(e.message ?: "An error occurred")
            }
        }
    }
}
