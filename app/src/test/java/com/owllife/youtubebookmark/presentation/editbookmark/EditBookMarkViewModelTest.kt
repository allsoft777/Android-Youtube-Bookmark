package com.owllife.youtubebookmark.presentation.editbookmark

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.owllife.youtubebookmark.BaseTest
import com.owllife.youtubebookmark.domain.BookmarkRepository
import com.owllife.youtubebookmark.domain.CategoryRepository
import com.owllife.youtubebookmark.domain.YoutubeRemoteRepository
import com.owllife.youtubebookmark.entity.CategoryEntireVO
import com.owllife.youtubebookmark.presentation.util.PresentationConstants
import com.owllife.youtubebookmark.utils.blockingObserve
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.amshove.kluent.shouldEqualTo
import org.junit.Before
import org.junit.Test

/**
 * @author owllife.dev
 * @since 20. 7. 2
 */
@Suppress("ReplaceGetOrSet")
class EditBookMarkViewModelTest : BaseTest() {

    private lateinit var editBookMarkViewModel: EditBookMarkViewModel

    @MockK
    lateinit var categoryRepository: CategoryRepository

    @MockK
    lateinit var bookmarkRepository: BookmarkRepository

    @MockK
    lateinit var youtubeRemoteRepository: YoutubeRemoteRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        editBookMarkViewModel = EditBookMarkViewModel(
            getContext(), categoryRepository, bookmarkRepository, youtubeRemoteRepository
        )
    }

    @Test
    fun `observe_category-list_should_return_live_data`() {
        val list = ArrayList<CategoryEntireVO>()
        val item1 = CategoryEntireVO(1, "sports", 1)
        list.add(item1)

        every { categoryRepository.observeCategories() } returns MutableLiveData(list)
        editBookMarkViewModel.loadCategoryData()
        editBookMarkViewModel.categoryList.blockingObserve()
        editBookMarkViewModel.categoryList.observeForever {
            it.size shouldEqualTo 1
            it.get(0).order shouldEqualTo 1
            it.get(0).id shouldEqualTo 1
            it.get(0).name shouldEqualTo "sports"
        }
    }

    @Test
    fun `find_index_from_selected_category_should_return_valid_idx`() {
        // given
        val list = ArrayList<CategoryEntireVO>()
        val item1 = CategoryEntireVO(1, "sports", 10)
        val item2 = CategoryEntireVO(1, "songs", 20)
        list.add(item1)
        list.add(item2)
        every { categoryRepository.observeCategories() } returns MutableLiveData(list)
        editBookMarkViewModel.loadCategoryData()

        // when
        val args = Bundle().apply {
            putInt(PresentationConstants.KEY_CATEGORY_ID, 20)
        }
        editBookMarkViewModel.loadArgs(args)
        editBookMarkViewModel.selectForwardedCategory()

        // then
        editBookMarkViewModel.forwardedCategoryIndex.value shouldEqualTo 1
    }

    @Test
    fun `find_index_from_selected_category_should_fail`() {
        // given
        val list = ArrayList<CategoryEntireVO>()
        every { categoryRepository.observeCategories() } returns MutableLiveData(list)
        editBookMarkViewModel.loadCategoryData()

        // when
        val args = Bundle().apply {
            putInt(PresentationConstants.KEY_CATEGORY_ID, 20)
        }
        editBookMarkViewModel.loadArgs(args)
        editBookMarkViewModel.selectForwardedCategory()

        // then
        editBookMarkViewModel.forwardedCategoryIndex.value shouldEqualTo -1
    }
}