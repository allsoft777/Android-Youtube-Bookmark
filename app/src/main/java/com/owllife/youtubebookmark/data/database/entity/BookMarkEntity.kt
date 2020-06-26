package com.owllife.youtubebookmark.data.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.owllife.youtubebookmark.entity.BookMarkEntireVO
import com.owllife.youtubebookmark.entity.BookMarkSimpleVO
import kotlinx.android.parcel.Parcelize

/**
 * @author owllife.dev
 * @since 20. 6. 11
 */
@Parcelize
@Entity(tableName = "bookmark")
data class BookMarkEntity(

    @ColumnInfo(name = "category_id")
    var categoryId: Int = 0,

    @ColumnInfo(name = "video_id")
    var videoId: String = "",

    @ColumnInfo(name = "thumbnail_url")
    var thumbnailUrl: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "tags")
    var tags: String,

    @ColumnInfo(name = "channel_id")
    var channelId: String,

    @ColumnInfo(name = "published_at")
    var publishedAt: String,

    @ColumnInfo(name = "bookmarked_at")
    var bookmarked_at: Long
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    companion object {
        fun newEntityWith(target: BookMarkEntireVO): BookMarkEntity =
            BookMarkEntity(
                target.categoryId,
                target.videoId,
                target.thumbnailUrl,
                target.title,
                target.description,
                target.tags,
                target.channelId,
                target.publishedAt,
                target.bookmarked_at
            )
    }

    fun toEntireVO(): BookMarkEntireVO {
        return BookMarkEntireVO(
            categoryId,
            videoId,
            thumbnailUrl,
            title,
            description,
            tags,
            channelId,
            publishedAt,
            bookmarked_at,
            id
        )
    }

    fun toSimpleVO(): BookMarkSimpleVO {
        return BookMarkSimpleVO(
            categoryId,
            thumbnailUrl,
            title,
            id
        )
    }
}