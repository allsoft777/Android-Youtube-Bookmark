package com.owllife.youtubebookmark.data.server

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author owllife.dev
 * @since 20. 6. 15
 */
object YoutubeRetrofit {

    fun getService(): YoutubeService = retrofit.create(YoutubeService::class.java)

    private val retrofit =
        Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/youtube/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}