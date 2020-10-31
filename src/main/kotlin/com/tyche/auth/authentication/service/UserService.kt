package com.tyche.auth.authentication.service

import com.tyche.auth.authentication.datatransferobject.AuthResponseDTO
import com.tyche.auth.authentication.domainobject.Role
import com.tyche.auth.authentication.domainobject.User
import com.tyche.auth.authentication.repository.RoleRepository
import com.tyche.auth.authentication.repository.UserRepository
import com.tyche.auth.authentication.security.JwtProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
 * @author Ali Wassouf
 */
@Service
class UserService(val userRepository: UserRepository, val roleRepository: RoleRepository,  val passwordEncoder: PasswordEncoder, val jwtProvider: JwtProvider) {


    fun findUserByID(id: Long) = userRepository.findById(id)

    fun createUser(user: User): User {
        user.password = passwordEncoder.encode(user.password)
        return userRepository.save(user)
    }

    fun findByUserName(username: String) = userRepository.findByUsername(username)!!

    fun deleteUserById(id: Long) = userRepository.deleteById(id)

    fun isNewUser(username: String) = !userRepository.existsByUsername(username)

    fun signUserUp(user: User): AuthResponseDTO {
        user.password = passwordEncoder.encode(user.password)
        val role = roleRepository.findByName("USER")
        val (accessToken, expiresIn) = jwtProvider.createAccessToken(user.username, role)
        val refreshToken = jwtProvider.createRefreshToken(user.username)
//        user.refreshToken = refreshToken
        userRepository.save(user)
        return AuthResponseDTO(accessToken, expiresIn, refreshToken, true)

    }

}