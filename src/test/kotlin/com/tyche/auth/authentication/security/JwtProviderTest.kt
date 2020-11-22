package com.tyche.auth.authentication.security

import com.tyche.auth.authentication.domainobject.Role
import com.tyche.auth.authentication.exception.TokenValidationException
import com.tyche.auth.authentication.service.CustomUserDetailsService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.testcontainers.shaded.org.apache.commons.lang.time.DateUtils
import java.util.*
import javax.servlet.http.HttpServletRequest


@RunWith(MockitoJUnitRunner::class)
internal class JwtProviderTest {

    val customUserDetailsService = mockk<CustomUserDetailsService>()

    val httpServletRequest = mockk<HttpServletRequest>()

    val jwtProvider = JwtProvider(customUserDetailsService)

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
    fun `when getting authentication from a token, return correct user`() {
        every { customUserDetailsService.loadUserByUsername("aliwassouf") }.returns(User("aliwassouf", "password", emptyList()))
        val authentication = jwtProvider.getAuthentication(createValidToken())
        assertEquals("aliwassouf", (authentication.principal as User).username)
    }

    @Test
    fun `resolving a request returns a token`() {
        val testToken = createValidToken()
        every { httpServletRequest.getHeader("Authorization") }.returns("${SecurityConstants.TOKEN_PREFIX}$testToken")
        val returnedToken = jwtProvider.resolveToken(httpServletRequest)
        assertEquals(testToken, returnedToken)
    }

    @Test
    fun `resolving request returns null`() {
        every { httpServletRequest.getHeader("Authorization") }.returns(createValidToken())
        val returnedToken = jwtProvider.resolveToken(httpServletRequest)
        assertNull(returnedToken)
    }


    @Test
    fun `when validating a valid token return true`() {
        assertTrue(jwtProvider.validateToken(createValidToken()))
    }

    @Test
    fun `when validating a non-valid token throws an excption`() {
        assertThrows(TokenValidationException::class.java) { jwtProvider.validateToken(createExpiredToken()) }
    }


    private fun getSubjectFromToken(token: String): String {
        return Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).body.subject
    }

    private fun getAuthFromToken(token: String): Any? {
        return Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).body["auth"]

    }

    private fun createValidToken(): String {
        return createToken(Date())

    }

    private fun createExpiredToken(): String {
        return createToken(DateUtils.addYears(Date(), -2))
    }

    private fun createToken(date: Date): String {
        val claims = Jwts.claims().setSubject("aliwassouf")
        claims["auth"] = SimpleGrantedAuthority("USER")

        val validity = Date(date.time + SecurityConstants.EXPIRATION_TIME)
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET).compact()
    }
}