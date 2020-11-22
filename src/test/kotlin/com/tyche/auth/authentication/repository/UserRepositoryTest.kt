package com.tyche.auth.authentication.repository

import com.tyche.auth.authentication.domainobject.Device
import com.tyche.auth.authentication.domainobject.RefreshTokens
import com.tyche.auth.authentication.domainobject.Role
import com.tyche.auth.authentication.domainobject.User
import org.junit.Assert
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.PostgreSQLContainer
import java.time.Duration
import java.util.*

/**
 * @author Ali Wassouf
 */
@RunWith(SpringRunner::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@TestPropertySource(locations = ["classpath:application-test.properties"])
open class UserRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `when saving a user with a user role, succeed`() {
        userRepository.save(User(1L, "ali", "pass", "email@email.com", false,
                setOf(Role(1L, "USER", "")), setOf(RefreshTokens(1L, false, "edljlkdg,kmskdfjfkds.lkksdf")), setOf(Device( id = UUID.randomUUID().toString(), os = "IOS"))))
        val user = userRepository.findByUsername("ali")
        Assert.assertTrue( user != null)
        Assert.assertEquals( 1, user?.roles?.size)
        Assert.assertEquals(1, user?.devices?.size)
        Assert.assertEquals(1, user?.refreshTokens?.size)


    }

    companion object {
        @JvmStatic
        @get:ClassRule
        val postgreSQLContainer = PostgreSQLContainer<Nothing>(
                "postgres:11.6-alpine"
        ).apply {

            withDatabaseName("rainbow_database")
            withUsername("unicorn_user")
            withPassword("magical_password")
            withStartupTimeout(Duration.ofSeconds(600))
        }
    }
}