package com.tyche.auth.authentication.service

import com.tyche.auth.authentication.datatransferobject.AuthResponseDTO
import com.tyche.auth.authentication.datatransferobject.SigunpDTO
import com.tyche.auth.authentication.domainobject.RefreshTokens
import com.tyche.auth.authentication.domainobject.Role
import com.tyche.auth.authentication.domainobject.User
import com.tyche.auth.authentication.mapper.makeUser
import com.tyche.auth.authentication.repository.RoleRepository
import com.tyche.auth.authentication.repository.UserRepository
import com.tyche.auth.authentication.security.JwtProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
 * @author Ali Wassouf
 */
@Service
class UserService(val userRepository: UserRepository, val roleRepository: RoleRepository, val passwordEncoder: PasswordEncoder, val jwtProvider: JwtProvider) {


    fun findUserByID(id: Long) = userRepository.findById(id)

    fun createUser(user: User): User {
        user.password = passwordEncoder.encode(user.password)
        return userRepository.save(user)
    }

    fun findByUserName(username: String) = userRepository.findByUsername(username)!!

    fun deleteUserById(id: Long) = userRepository.deleteById(id)

    fun isNewUser(username: String) = !userRepository.existsByUsername(username)

    fun signUserUp(sigunpDTO: SigunpDTO): AuthResponseDTO {
        val user = makeUser(signupDTO = sigunpDTO)
        user.password = passwordEncoder.encode(user.password)
        var role = roleRepository.findByName("USER")
        if (role == null) {
            role = roleRepository.save(Role(1L, "USER", "Normal User role"))
        }
        val (accessToken, expiresIn) = jwtProvider.createAccessToken(user.username, role)
        val refreshToken = jwtProvider.createRefreshToken(user.username)
        user.refreshTokens = setOf(RefreshTokens(id = 1L, value = refreshToken))
        userRepository.save(user)
        return AuthResponseDTO(accessToken, expiresIn, refreshToken, true)

    }

}