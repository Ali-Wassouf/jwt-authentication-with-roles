package com.tyche.auth.authentication.mapper

import com.tyche.auth.authentication.datatransferobject.SigunpDTO
import com.tyche.auth.authentication.domainobject.Device
import com.tyche.auth.authentication.domainobject.User

fun makeUser(signupDTO: SigunpDTO): User {
    return User(id = 1L, username = signupDTO.username, password = signupDTO.password, email = signupDTO.email, roles = emptySet(), devices = setOf(Device(id = signupDTO.deviceId, os = signupDTO.deviceOS)), refreshTokens = setOf())
}
