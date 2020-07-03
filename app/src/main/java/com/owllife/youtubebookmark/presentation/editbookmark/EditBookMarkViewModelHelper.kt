package com.owllife.youtubebookmark.presentation.editbookmark

import com.owllife.youtubebookmark.core.empty
import com.owllife.youtubebookmark.domain.resp.Snippet
import com.owllife.youtubebookmark.domain.resp.YoutubeVideoResp
import com.owllife.youtubebookmark.entity.BookMarkEntireVO

/**
 * @author owllife.dev
 * @since 20. 6. 23
 */
fun extractYouTubeVideoIdFromUrl(movieUrl: String?): String {
    var videoId = String.empty()
    movieUrl?.let {
        var split: List<String>? = null
        if (movieUrl.contains("youtu.be/")) {
            split = movieUrl.split("youtu.be/")
        } else if (movieUrl.contains("watch?v=")) {
            split = movieUrl.split("watch?v=")
        }
        split?.let {
            if (split.size == 2) {
                videoId = split[1]
            }
            videoId = videoId.removeSuffix("/")
            if (videoId.contains('/')) videoId = String.empty()
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