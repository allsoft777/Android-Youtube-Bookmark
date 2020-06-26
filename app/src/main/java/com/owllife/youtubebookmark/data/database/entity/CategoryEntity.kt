package com.owllife.youtubebookmark.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.owllife.youtubebookmark.entity.CategoryEntireVO

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
        fun copy(target: CategoryEntireVO): CategoryEntity {
            val ret = CategoryEntity(
                target.order,
                target.name
            )
            ret.id = target.id
            return ret
        }

        fun newEntityWith(target: CategoryEntireVO): CategoryEntity =
            CategoryEntity(target.order, target.name)
    }

    fun toEntireVO(): CategoryEntireVO = CategoryEntireVO(order, name, id)
}