package com.airbnb.authenticator.common.controllers

import com.airbnb.authenticator.common.dtos.BaseDto
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity

/**
 * @project IntelliJ IDEA
 * @author mir00r on 21/6/21
 */
interface BaseCrudController<T : BaseDto> {
    fun search(query: String, page: Int, size: Int): ResponseEntity<Page<T>>
    fun find(id: Long): ResponseEntity<T>
    fun create(dto: T): ResponseEntity<T>
    fun update(id: Long, dto: T): ResponseEntity<T>
    fun delete(id: Long): ResponseEntity<Any>
}
