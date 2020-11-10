package com.tyche.auth.authentication.security

import com.tyche.auth.authentication.domainobject.Role
import com.tyche.auth.authentication.exception.TokenValidationException
import com.tyche.auth.authentication.security.SecurityConstants.*
import com.tyche.auth.authentication.service.CustomUserDetailsService
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*
import javax.servlet.http.HttpServletRequest
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import mu.KotlinLogging
import org.apache.commons.lang3.time.DateUtils
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}
/**
 * @author Ali Wassouf
 */
@Component
class JwtProvider(val customUserDetailsService: CustomUserDetailsService){


    fun createAccessToken(username: String, role: Role): Pair<String, Long> {
        logger.info { "Creating access token in system" }
        val claims = Jwts.claims().setSubject(username)
        claims["auth"] = SimpleGrantedAuthority(role.name)

        val date = Date()
        val validity = Date(date.time + EXPIRATION_TIME)
        val accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET).compact()
        logger.info { "Created access token $accessToken" }
        return Pair(accessToken, validity.time)
    }

    fun createRefreshToken(username: String): String {
        logger.info { "Creating refresh token " }
        val claims = Jwts.claims().setSubject(username + "URefresh")
        val date = Date()
        val validity = DateUtils.addYears(date,1)
        val refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET).compact()
        logger.info { "Created refresh token $refreshToken" }
        return refreshToken
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails = customUserDetailsService.loadUserByUsername(getUsername(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    private fun getUsername(token: String): String {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).body.subject
    }

    fun resolveToken(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("$TOKEN_PREFIX")) {
            bearerToken.substring(7)
        } else null
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token)
            return true
        } catch (e: JwtException) {
            logger.error { "JWTEXC ${e.stackTrace}" }
            throw TokenValidationException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: IllegalArgumentException) {
            logger.info { "Illegal argument $e" }
            throw TokenValidationException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }
}