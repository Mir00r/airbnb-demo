package com.airbnb.authenticator.common.mappers

import com.airbnb.authenticator.common.dtos.BaseDto
import com.airbnb.authenticator.common.entities.BaseEntity

/**
 * @project IntelliJ IDEA
 * @author mir00r on 21/6/21
 */
interface BaseMapper<T : BaseEntity, S : BaseDto> {
    fun map(entity: T): S
    fun map(dto: S, exEntity: T?): T
}
