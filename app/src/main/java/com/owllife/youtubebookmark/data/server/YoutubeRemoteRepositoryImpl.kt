package com.owllife.youtubebookmark.data.server

import android.content.Context
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.domain.ResultData
import com.owllife.youtubebookmark.domain.YoutubeRemoteRepository
import com.owllife.youtubebookmark.domain.resp.YoutubeVideoResp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author owllife.dev
 * @since 20. 6. 15
 */
class YoutubeRemoteRepositoryImpl(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : YoutubeRemoteRepository {

    companion object {

        @Volatile
        private var INSTANCE: YoutubeRemoteRepository? = null

        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): YoutubeRemoteRepository {
            if (INSTANCE == null) {
                INSTANCE =
                    YoutubeRemoteRepositoryImpl(context)
            }
            return INSTANCE!!
        }
    }

    override suspend fun getMovieInfo(movieId: String): ResultData<YoutubeVideoResp> =
        withContext(ioDispatcher) {
            val key = context.getString(R.string.youtube_api_key)
            val resp = YoutubeRetrofit.getService().getVideoInfo(movieId, key).execute()
            if (resp.isSuccessful) {
                return@withContext ResultData.Success(resp.body() as YoutubeVideoResp)
            }
            return@withContext ResultData.Failure(Exception(resp.errorBody().toString()))
        }
}