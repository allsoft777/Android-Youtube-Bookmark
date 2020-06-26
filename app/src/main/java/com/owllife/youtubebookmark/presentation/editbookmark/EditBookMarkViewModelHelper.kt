package com.owllife.youtubebookmark.presentation.editbookmark

import com.owllife.youtubebookmark.core.empty
import com.owllife.youtubebookmark.domain.resp.Snippet
import com.owllife.youtubebookmark.domain.resp.YoutubeVideoResp
import com.owllife.youtubebookmark.entity.BookMarkEntireVO

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

fun mapYoutubeVideoRespToBookMarkEntity(youtubeVideoResp: YoutubeVideoResp): BookMarkEntireVO {
    val snippet: Snippet = youtubeVideoResp.items[0].snippet!!
    var thumbnailUrl = String.empty()
    if (snippet.thumbnails.maxres != null) {
        thumbnailUrl = snippet.thumbnails.maxres.url
    }
    if (thumbnailUrl.isEmpty() && snippet.thumbnails.high != null) {
        thumbnailUrl = snippet.thumbnails.high.url
    }
    if (thumbnailUrl.isEmpty()) {
        thumbnailUrl = snippet.thumbnails.default.url
    }

    val title = snippet.title
    val desc = snippet.description
    val tagsList = snippet.tags
    val sb = StringBuilder()
    for (i in tagsList.indices) {
        if (i != 0) {
            sb.append(",")
        }
        sb.append(tagsList[i])
    }
    val tagsStr = sb.toString()
    val channelId = snippet.channelId
    val publishedAt = snippet.publishedAt
    return BookMarkEntireVO(
        thumbnailUrl = thumbnailUrl,
        title = title,
        description = desc,
        tags = tagsStr,
        channelId = channelId,
        publishedAt = publishedAt,
        bookmarked_at = System.currentTimeMillis()
    )
}