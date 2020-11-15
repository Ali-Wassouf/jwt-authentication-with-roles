package com.tyche.auth.authentication.domainobject

import java.util.*
import javax.persistence.*

/**
 * @author Ali Wassouf
 */
@Entity
@Table(name = "devices")
class Device(
        @Id
        var id: String,

        @Column
        var os: String
){
        @PrePersist
        fun autoFillUUID(){
                id = UUID.randomUUID().toString()
        }
}