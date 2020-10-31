package com.tyche.auth.authentication.repository

import com.tyche.auth.authentication.domainobject.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * @author Ali Wassouf
 */
@Repository
interface RoleRepository : JpaRepository<Role, Long> {

    fun findByName(name: String): Role
}