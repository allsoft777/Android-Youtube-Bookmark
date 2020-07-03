package com.owllife.youtubebookmark.presentation.editbookmark

import com.owllife.youtubebookmark.BaseTest
import com.owllife.youtubebookmark.core.empty
import com.owllife.youtubebookmark.domain.resp.*
import org.amshove.kluent.shouldEqualTo
import org.junit.Test

/**
 * @author owllife.dev
 * @since 20. 7. 3
 */
class EditBookMarkViewModelHelperTest : BaseTest() {

    @Test
    fun `extract_youtube_video-id_with_empty_url`() {
        val result = extractYouTubeVideoIdFromUrl("")
        result shouldEqualTo String.empty()
    }

    @Test
    fun `extract_youtube_video-id_with_null_url`() {
        val result = extractYouTubeVideoIdFromUrl(null)
        result shouldEqualTo String.empty()
    }

    @Test
    fun `extract_youtube_video-id_with_invalid_url`() {
        val url = "abcdefh"
        val result = extractYouTubeVideoIdFromUrl(url)
        result shouldEqualTo String.empty()
    }

    @Test
    fun `extract_youtube_video-id_with_invalid_url2`() {
        val url = "https://youtu.be/abcd/44d"
        val result = extractYouTubeVideoIdFromUrl(url)
        result shouldEqualTo String.empty()
    }

    @Test
    fun `extract_youtube_video-id_with_valid_url`() {
        val url = "https://youtu.be/abcd"
        val result = extractYouTubeVideoIdFromUrl(url)
        result shouldEqualTo "abcd"
    }

    @Test
    fun `extract_youtube_video-id_with_valid_url_with_last_slash`() {
        val url = "https://youtu.be/abcd/"
        val result = extractYouTubeVideoIdFromUrl(url)
        result shouldEqualTo "abcd"
    }

    @Test
    fun `extract_youtube_video-id_with_invalid_url_with_last_slash`() {
        val url = "https://youtu.be/abcd/efg/"
        val result = extractYouTubeVideoIdFromUrl(url)
        result shouldEqualTo String.empty()
    }

    @Test
    fun `extract_youtube_video-id_with_valid_watch_url`() {
        val url = "https://www.youtube.com/watch?v=H6WMt_n3c8w"
        val result = extractYouTubeVideoIdFromUrl(url)
        result shouldEqualTo "H6WMt_n3c8w"
    }

    @Test
    fun `extract_youtube_video-id_with_valid_watch_url_with_last_slash`() {
        val url = "https://www.youtube.com/watch?v=H6WMt_n3c8w/"
        val result = extractYouTubeVideoIdFromUrl(url)
        result shouldEqualTo "H6WMt_n3c8w"
    }

    @Test
    fun `extract_youtube_video-id_with_invalid_watch_url`() {
        val url = "https://www.youtube.com/watch?v=H6WMt_n3c8w/abcdee"
        val result = extractYouTubeVideoIdFromUrl(url)
        result shouldEqualTo String.empty()
    }

    @Test
    fun `extract_youtube_video-id_with_invalid_watch_url_with_last_slash`() {
        val url = "https://www.youtube.com/watch?v=H6WMt_n3c8w/abcdee/"
        val result = extractYouTubeVideoIdFromUrl(url)
        result shouldEqualTo String.empty()
    }

    @Test
    fun `map_youtube_video_resp_to_db_entity_should_return_valid_item`() {
        val snippet = Snippet(
            title = "video1_temp_title",
            channelId = "temp_channel",
            thumbnails = Thumbnails(
                default = Default(250, "http://www.aaa.jpg", 200)
            )
        )
        val item = Item("tag", "temp_id1", "", snippet)
        val list = ArrayList<Item>()
        list.add(item)
        val resp = YoutubeVideoResp("", list, "", PageInfo(1, 1))
        val mappedItem = mapYoutubeVideoRespToBookMarkEntity(resp)
        mappedItem.id shouldEqualTo 0
        mappedItem.title shouldEqualTo "video1_temp_title"
        mappedItem.thumbnailUrl shouldEqualTo "http://www.aaa.jpg"
        mappedItem.channelId shouldEqualTo "temp_channel"
    }
}