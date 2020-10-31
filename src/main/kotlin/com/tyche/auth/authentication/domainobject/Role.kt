package com.tyche.auth.authentication.domainobject

import javax.persistence.*

/**
 * @author Ali Wassouf
 */
@Entity
@Table(name = "roles")
class Role(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,
        @Column
        var name: String? = null,
        @Column
        var description: String? = null
)