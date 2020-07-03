package com.owllife.youtubebookmark.domain.resp

data class Thumbnails(
    val default: Default,
    val high: High? = null,
    val maxres: Maxres? = null,
    val medium: Medium? = null,
    val standard: Standard? = null
)