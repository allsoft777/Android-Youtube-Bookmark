package com.owllife.youtubebookmark.domain

import com.owllife.youtubebookmark.domain.resp.YoutubeMovieResp

/**
 * @author owllife.dev
 * @since 20. 6. 15
 */
interface YoutubeRemoteRepository {

    suspend fun getMovieInfo(movieId: String): ResultData<YoutubeMovieResp>
}