package com.tyche.auth.authentication.repository

import com.tyche.auth.authentication.domainobject.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * @author Ali Wassouf
 */
@Repository
interface UserRepository: JpaRepository<User, Long> {

    fun findByUsername(username: String): User?
    fun existsByUsername(userName: String): Boolean
}