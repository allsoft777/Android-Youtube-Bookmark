package com.owllife.youtubebookmark.domain.resp

data class Snippet(
    val categoryId: String = "",
    val channelId: String = "",
    val channelTitle: String = "",
    val defaultAudioLanguage: String = "",
    val description: String = "",
    val liveBroadcastContent: String = "",
    val localized: Localized? = null,
    val publishedAt: String  = "",
    val tags: List<String> = ArrayList(),
    val thumbnails: Thumbnails,
    val title: String
)