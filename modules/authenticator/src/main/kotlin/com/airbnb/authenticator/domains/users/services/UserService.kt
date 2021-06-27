package com.airbnb.authenticator.domains.users.services

import com.airbnb.authenticator.common.services.BaseCrudService
import com.airbnb.authenticator.domains.users.models.entities.AcValidationToken
import com.airbnb.authenticator.domains.users.models.entities.User
import org.springframework.data.domain.Page
import java.util.*
import javax.transaction.Transactional

/**
 * @project IntelliJ IDEA
 * @author mir00r on 22/6/21
 */
interface UserService : BaseCrudService<User> {
    fun findAll(page: Int): Page<User>
    fun findByRole(role: String, page: Int): Page<User>
    fun findByRole(role: String): List<User>

    fun findByUsername(username: String): Optional<User>
    fun findByPhone(phone: String): Optional<User>
    fun findByEmail(email: String): Optional<User>

    fun setPassword(id: Long, newPassword: String): User
    fun setRoles(id: Long, roleIds: List<Long>): User
    fun userExists(username: String): Boolean

    fun search(query: String, role: String, page: Int, size: Int): Page<User>
}
