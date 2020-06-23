package com.owllife.youtubebookmark.domain.resp

data class YoutubeVideoResp(
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val pageInfo: PageInfo
)