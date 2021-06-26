package com.example.astha.domains.reviews.models.enums

enum class ActionPoints constructor(
    public val points: Int
) {
    ADD_TITLE(1),
    ADD_CONTENT(1),
    ADD_PROS(1),
    ADD_CONS(1),
    ADD_PHOTOS(2),
    VERIFY_REVIEW(4),
}
