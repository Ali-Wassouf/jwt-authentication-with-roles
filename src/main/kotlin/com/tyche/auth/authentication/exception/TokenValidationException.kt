package com.tyche.auth.authentication.exception

import org.springframework.http.HttpStatus

data class TokenValidationException(override var message: String, var httpStatus: HttpStatus) : RuntimeException()