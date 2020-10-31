package com.tyche.auth.authentication.domainobject

import org.hibernate.annotations.Where
import javax.persistence.*

/**
 * @author Ali Wassouf
 */

@Entity
@Table(name = "users")
@Where(clause = "deleted = false")
class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Column(nullable = false)
        var username: String,

        @Column(nullable = false)
        var password: String,

        @Column(nullable = false)
        var email: String,

        @Column(nullable = false)
        var deleted: Boolean = false,

        @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
        @JoinTable(name = "USER_ROLES", joinColumns = [JoinColumn(name = "USER_ID")], inverseJoinColumns = [JoinColumn(name = "ROLE_ID")])
        var roles: Set<Role>
)
