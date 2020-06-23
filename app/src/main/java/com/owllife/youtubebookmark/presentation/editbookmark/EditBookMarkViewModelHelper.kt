package com.owllife.youtubebookmark.presentation.editbookmark

import com.owllife.youtubebookmark.core.empty
import com.owllife.youtubebookmark.domain.entity.BookMarkEntity
import com.owllife.youtubebookmark.domain.resp.YoutubeVideoResp

/**
 * @author owllife.dev
 * @since 20. 6. 23
 */
fun extractYouTubeVideoIdFromUrl(movieUrl: String): String {
    var videoId = String.empty()
    if (movieUrl.contains("youtu.be/")) {
        val split = movieUrl.split("/")
        videoId = split[split.size - 1]
    } else if (movieUrl.contains("watch?v=")) {
        val split = movieUrl.split("watch?v=")
        if (split.size == 2) {
            videoId = split[split.size - 1]
        }
    }
    return videoId
}

fun mapYoutubeVideoRespToBookMarkEntity(youtubeVideoResp: YoutubeVideoResp): BookMarkEntity {
    var thumbnailUrl = ""
    if (youtubeVideoResp.items[0].snippet.thumbnails.maxres != null) {
        thumbnailUrl = youtubeVideoResp.items[0].snippet.thumbnails.maxres!!.url
    }
    if (thumbnailUrl.isEmpty() && youtubeVideoResp.items[0].snippet.thumbnails.high != null) {
        thumbnailUrl = youtubeVideoResp.items[0].snippet.thumbnails.high!!.url
    }
    if (thumbnailUrl.isEmpty()) {
        thumbnailUrl = youtubeVideoResp.items[0].snippet.thumbnails.default.url
    }

    val title = youtubeVideoResp.items[0].snippet.title
    val desc = youtubeVideoResp.items[0].snippet.description
    val tagsList = youtubeVideoResp.items[0].snippet.tags
    val sb = StringBuilder()
    for (i in tagsList.indices) {
        if (i != 0) {
            sb.append(",")
        }
        sb.append(tagsList[i])
    }
    val tagsStr = sb.toString()
    val channelId = youtubeVideoResp.items[0].snippet.channelId
    val publishedAt = youtubeVideoResp.items[0].snippet.publishedAt
    return BookMarkEntity(
        thumbnailUrl = thumbnailUrl,
        title = title,
        description = desc,
        tags = tagsStr,
        channelId = channelId,
        publishedAt = publishedAt,
        bookmarked_at = System.currentTimeMillis()
    )
}