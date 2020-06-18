package com.owllife.youtubebookmark.domain.resp

data class Item(
    val etag: String,
    val id: String,
    val kind: String,
    val snippet: Snippet
)