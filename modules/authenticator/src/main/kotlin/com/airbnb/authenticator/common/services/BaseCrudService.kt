package com.airbnb.authenticator.common.services

import com.airbnb.authenticator.common.entities.BaseEntity
import org.springframework.data.domain.Page
import java.util.*

/**
 * @project IntelliJ IDEA
 * @author mir00r on 21/6/21
 */
interface BaseCrudService<T : BaseEntity> {
    fun search(query: String, page: Int, size: Int): Page<T>
    fun save(entity: T): T
    fun find(id: Long): Optional<T>
    fun delete(id: Long, softDelete: Boolean)
    fun validate(entity: T)
}
