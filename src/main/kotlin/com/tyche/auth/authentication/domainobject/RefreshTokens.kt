package com.tyche.auth.authentication.domainobject

import javax.persistence.*

/**
 * @author Ali Wassouf
 */
@Entity
@Table(name = "refresh_tokens")
class RefreshTokens(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Column(nullable = false)
        var revoked: Boolean = false,

        @Column(nullable = false)
        var value: String
)