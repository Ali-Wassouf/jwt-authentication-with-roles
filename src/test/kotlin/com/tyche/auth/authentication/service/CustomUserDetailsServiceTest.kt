package com.tyche.auth.authentication.service

import com.tyche.auth.authentication.domainobject.Role
import com.tyche.auth.authentication.domainobject.User
import com.tyche.auth.authentication.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.security.core.userdetails.UsernameNotFoundException

@RunWith(MockitoJUnitRunner::class)
internal class CustomUserDetailsServiceTest {

    private val userRepository = mockk<UserRepository>()

    private val customUserDetailsService = CustomUserDetailsService(userRepository)

    @Test
    fun init() {
        assertNotNull(customUserDetailsService)
    }

    @Test
    fun `when loading a user we return a correct user`() {

        every { userRepository.findByUsername("aliwassouf") }
                .returns(User(1L, "aliwassouf", "password", "email@domain.com", false,
                        setOf(Role(1L, "ADMIN", null)), emptySet(), emptySet()))

        val userDetails = customUserDetailsService.loadUserByUsername("aliwassouf")

        assertNotNull(userDetails)
    }


    @Test
    fun `trying to load non-existing user throws an exception`() {

        every { userRepository.findByUsername("aliwassouf") }.returns(null)

        assertThrows(UsernameNotFoundException::class.java) { customUserDetailsService.loadUserByUsername("aliwassouf") }
    }
}