package com.owllife.youtubebookmark.entity

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
data class CategoryEntireVO(
    var order: Int,
    var name: String,
    var id: Int = 0
) {
    companion object {
        fun copy(target: CategoryEntireVO): CategoryEntireVO =
            CategoryEntireVO(target.order, target.name, target.id)
    }
}