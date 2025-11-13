package com.example.uvgestradasarah.states

import com.example.uvgestradasarah.Assets

data class AssetsState(
    val isLoading: Boolean = true,
    val assets: List<Assets> = emptyList(),
    val error: String? = null,
    val success: String? = null
)