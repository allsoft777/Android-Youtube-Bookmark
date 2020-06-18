package com.owllife.youtubebookmark.domain.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * @author owllife.dev
 * @since 20. 6. 11
 */
@Parcelize
@Entity(tableName = "bookmark")
data class BookMarkEntity(

    @ColumnInfo(name = "category_id")
    var categoryId: Int,

    @ColumnInfo(name = "video_id")
    var videoId: String,

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
}