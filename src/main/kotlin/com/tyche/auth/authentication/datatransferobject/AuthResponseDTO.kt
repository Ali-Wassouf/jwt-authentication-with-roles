package com.tyche.auth.authentication.datatransferobject

data class AuthResponseDTO(val accessToken: String,
                           val expiresIn: Long,
                           val refreshToken: String,
                           val isNewUser: Boolean)