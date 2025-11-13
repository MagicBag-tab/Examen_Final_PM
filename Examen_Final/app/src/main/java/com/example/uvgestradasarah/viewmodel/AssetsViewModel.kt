package com.example.uvgestradasarah.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgestradasarah.repository.AssetsRepository
import com.example.uvgestradasarah.states.AssetsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AssetsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AssetsRepository(application)

    private val _state = MutableStateFlow(AssetsState())
    val state: StateFlow<AssetsState> = _state.asStateFlow()

    private val _lastSavedDate = MutableStateFlow<String?>(null)
    val lastSavedDate: StateFlow<String?> = _lastSavedDate.asStateFlow()

    init {
        loadAssets()
        observeLastSavedDate()
    }

    private fun observeLastSavedDate() {
        viewModelScope.launch {
            repository.getLastSavedDate().collectLatest { date ->
                _lastSavedDate.value = date
            }
        }
    }

    fun loadAssets() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            val result = repository.getAssets()

            if (result.isSuccess) {
                val assets = result.getOrNull() ?: emptyList()
                val hasLocalData = repository.hasLocalData()

                val isOnline = try {
                    repository.getAssets().isSuccess
                } catch (e: Exception) {
                    false
                }

                _state.value = _state.value.copy(
                    isLoading = false,
                    assets = assets,
                    success = if (isOnline) "online" else "offline",
                    error = null
                )
            } else {
                val localAssets = repository.getLocalAssets()

                if (localAssets.isNotEmpty()) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        assets = localAssets,
                        success = "offline",
                        error = null
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        assets = emptyList(),
                        error = result.exceptionOrNull()?.message ?: "Error al cargar datos"
                    )
                }
            }
        }
    }

    fun saveOffline() {
        viewModelScope.launch {
            try {
                if (_state.value.assets.isNotEmpty()) {
                    repository.saveAssetsLocally(_state.value.assets)
                    _state.value = _state.value.copy(
                        success = "saved"
                    )
                    loadAssets()
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Error al guardar datos offline"
                )
            }
        }
    }
}