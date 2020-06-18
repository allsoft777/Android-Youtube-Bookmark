package com.owllife.youtubebookmark.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
@Entity(tableName = "category")
data class CategoryEntity(
    @ColumnInfo(name = "order")
    var order: Int,
    @ColumnInfo(name = "name")
    var name: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    companion object {
        fun copy(target: CategoryEntity): CategoryEntity {
            val ret = CategoryEntity(
                target.order,
                target.name
            )
            ret.id = target.id
            return ret
        }
    }
}