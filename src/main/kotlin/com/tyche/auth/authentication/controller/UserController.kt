package com.tyche.auth.authentication.controller

import com.tyche.auth.authentication.datatransferobject.AuthResponseDTO
import com.tyche.auth.authentication.datatransferobject.RefreshTokenRequest
import com.tyche.auth.authentication.datatransferobject.SigunpDTO
import com.tyche.auth.authentication.domainobject.User
import com.tyche.auth.authentication.service.UserService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Ali Wassouf
 */
@RestController
@RequestMapping("/api/users")
class UserController(val userService: UserService){


    @PostMapping("/token/issue")
    fun issueToken(@RequestBody user: SigunpDTO): AuthResponseDTO {
        return userService.signUserUp(user)
    }

    @PostMapping("/token/refresh", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun refreshToken(@RequestBody refreshTokenRequest: RefreshTokenRequest){

        //todo
        /**
         * Make sure the refresh token was issued to this user on this device
         * If these conditions hold, refresh the token`
         * */
    }
}