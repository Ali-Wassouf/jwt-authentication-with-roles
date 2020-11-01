package com.tyche.auth.authentication.datatransferobject

import com.fasterxml.jackson.annotation.JsonProperty

data class RefreshTokenRequest(@JsonProperty("grant_type") val grantType: String,
                               @JsonProperty("refresh_token") val refreshToken: String)
