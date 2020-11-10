package com.tyche.auth.authentication.security

import com.tyche.auth.authentication.domainobject.Role
import com.tyche.auth.authentication.service.CustomUserDetailsService
import io.jsonwebtoken.Jwts
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.security.core.userdetails.User
import javax.servlet.http.HttpServletRequest

@RunWith(MockitoJUnitRunner::class)
internal class JwtProviderTest {

    val customUserDetailsService = mockk<CustomUserDetailsService>()

    val httpServletRequest = mockk<HttpServletRequest>()

    val jwtProvider = JwtProvider(customUserDetailsService)

    val testToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGl3YXNzb3VmIiwiYXV0aCI6eyJhdXRob3JpdHkiOiJBRE1JTiJ9LCJpYXQiOjE2MDQ4NDIwNzMsImV4cCI6MTYwNTcwNjA3M30.IabviEXcdIIZn8ATkoSiNsYBf43jczDEudpINQK-WO0"


    @Test
    fun init() {
        assertNotNull(jwtProvider)
    }

    @Test
    fun `creating an access token succeeds`() {
        val (accessToken, expiryTime) = jwtProvider.createAccessToken("aliwassouf", Role(1L, "ADMIN", ""))
        val subject = getSubjectFromToken(accessToken)
        val auth = getAuthFromToken(accessToken)

        assertNotNull(accessToken)
        assertNotNull(expiryTime)
        assertEquals("aliwassouf", subject)
        assertEquals("ADMIN", (auth as LinkedHashMap<String, String>)["authority"])
    }

    @Test
    fun `creating a refreshToken succeeds`() {
        val refreshToken = jwtProvider.createRefreshToken("aliwassouf")
        val subject = getSubjectFromToken(refreshToken)

        assertNotNull(refreshToken)
        assertEquals("aliwassoufURefresh", subject)
    }

    @Test
    fun `when getting authentication from a token, return correct user`(){
        every { customUserDetailsService.loadUserByUsername("aliwassouf") }.returns(User("aliwassouf", "password", emptyList()))
        val authentication = jwtProvider.getAuthentication(testToken)
        assertEquals("aliwassouf",(authentication.principal as User).username)
    }

    @Test
    fun `resolving a request returns a token`(){
        every { httpServletRequest.getHeader("Authorization") }.returns("${SecurityConstants.TOKEN_PREFIX}$testToken")
        val returnedToken  = jwtProvider.resolveToken(httpServletRequest)
        assertEquals(testToken, returnedToken)
    }

    @Test
    fun `resolving request returns null`(){
        every { httpServletRequest.getHeader("Authorization") }.returns(testToken)
        val returnedToken  = jwtProvider.resolveToken(httpServletRequest)
        assertNull(returnedToken)
    }

    private fun getSubjectFromToken(token: String): String {
        return Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).body.subject
    }

    private fun getAuthFromToken(token: String): Any? {
        return Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).body["auth"]

    }
}