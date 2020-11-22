package com.tyche.auth.authentication.service

import com.tyche.auth.authentication.domainobject.User
import com.tyche.auth.authentication.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.HashSet

/**
 * @author Ali Wassouf
 */

@Service
class CustomUserDetailsService(val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
                ?: throw UsernameNotFoundException("User '$username' not found")
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.password)
                .authorities(getAuthority(user))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build()
    }

    private fun getAuthority(user: User): Set<SimpleGrantedAuthority>? {
        val authorities: MutableSet<SimpleGrantedAuthority> = HashSet()
        user.roles.forEach { role -> authorities.add(SimpleGrantedAuthority("ROLE_" + role.name)) }
        return authorities
    }
}