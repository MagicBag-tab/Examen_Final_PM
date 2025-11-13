package com.example.uvgestradasarah.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgestradasarah.repository.AssetsRepository
import com.example.uvgestradasarah.states.ProfileState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AssetsRepository(application)

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _lastSavedDate = MutableStateFlow<String?>(null)
    val lastSavedDate: StateFlow<String?> = _lastSavedDate.asStateFlow()

    init {
        observeLastSavedDate()
    }

    private fun observeLastSavedDate() {
        viewModelScope.launch {
            repository.getLastSavedDate().collectLatest { date ->
                _lastSavedDate.value = date
            }
        }
    }

    fun loadAsset(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            val result = repository.getAssetById(id)

            if (result.isSuccess) {
                val asset = result.getOrNull()

                val isOnline = try {
                    repository.getAssets().isSuccess
                } catch (e: Exception) {
                    false
                }

                _state.value = _state.value.copy(
                    isLoading = false,
                    profile = asset,
                    success = if (isOnline) "online" else "offline",
                    error = null
                )
            } else {
                _state.value = _state.value.copy(
                    isLoading = false,
                    profile = null,
                    error = result.exceptionOrNull()?.message ?: "Error al cargar el asset"
                )
            }
        }
    }
}