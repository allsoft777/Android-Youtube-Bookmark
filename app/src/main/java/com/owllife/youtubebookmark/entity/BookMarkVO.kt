package com.owllife.youtubebookmark.entity

/**
 * @author owllife.dev
 * @since 20. 6. 26
 */
data class BookMarkSimpleVO(
    var categoryId: Int = 0,
    var thumbnailUrl: String,
    var title: String,
    var id: Int
)

data class BookMarkEntireVO(
    var categoryId: Int = 0,
    var videoId: String = "",
    var thumbnailUrl: String,
    var title: String,
    var description: String,
    var tags: String,
    var channelId: String,
    var publishedAt: String,
    var bookmarked_at: Long,
    var id: Int = 0
)