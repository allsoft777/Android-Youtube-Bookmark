package com.owllife.youtubebookmark.data.server

import com.owllife.youtubebookmark.domain.resp.YoutubeMovieResp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author owllife.dev
 * @since 20. 6. 15
 */
interface YoutubeService {

    @GET("videos?part=snippet")
    fun getVideoInfo(@Query("id") id: String, @Query("key") key: String): Call<YoutubeMovieResp>
}