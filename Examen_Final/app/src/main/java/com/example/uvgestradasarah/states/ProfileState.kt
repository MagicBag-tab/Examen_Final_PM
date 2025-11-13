package com.example.uvgestradasarah.states

import com.example.uvgestradasarah.Assets

data class ProfileState(
    val isLoading: Boolean = true,
    val profile: Assets? = null,
    val error: String? = null,
    val success: String? = null
)