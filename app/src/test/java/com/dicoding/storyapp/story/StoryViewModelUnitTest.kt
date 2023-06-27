package com.dicoding.storyapp.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.liveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.storyapp.MainTestDispatcher
import com.dicoding.storyapp.app.di.StoryViewModel
import com.dicoding.storyapp.app.ui.adapter.StoryDiffCallback
import com.dicoding.storyapp.domain.usecases.StoryRepo
import com.dicoding.storyapp.fakedata.FakeDataStory.fakeStories
import com.dicoding.storyapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class StoryViewModelUnitTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcher = MainTestDispatcher()

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    private val storyRepository = Mockito.mock(StoryRepo::class.java)
    private lateinit var storyViewModel: StoryViewModel

    private val data = fakeStories

    @Before
    fun setUp() {

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `allStory PagingData test success`() = runTest {
        val pagingData = PagingData.from(data)

        Mockito.`when`(storyRepository.getAllStory()).then { liveData { emit(pagingData) } }

        storyViewModel = StoryViewModel(repository = storyRepository)

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryDiffCallback,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = mainDispatcher.mainTestDispatcher
        )

        storyViewModel.allStory.getOrAwaitValue().also {
            differ.submitData(it)
        }

        val testResult = differ.snapshot().items

        // cek data tidak null.
        Assert.assertNotNull(testResult)

        // cek jumlah data sesuai dengan yang diharapkan.
        Assert.assertEquals(data.size, testResult.size)

        // cek data pertama yang dikembalikan sesuai.
        Assert.assertEquals(data.first(), testResult.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `allStory PagingData test failed`() = runTest {
        // Mengembalikan data kosong
        val emptyPagingData = PagingData.from(emptyList())

        Mockito.`when`(storyRepository.getAllStory())
            .then { liveData { emit(emptyPagingData) } }

        storyViewModel = StoryViewModel(repository = storyRepository)

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryDiffCallback,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = mainDispatcher.mainTestDispatcher
        )

        storyViewModel.allStory.getOrAwaitValue().also {
            differ.submitData(it)
        }

        val testResult = differ.snapshot().items

        // cek jumlah data yang dikembalikan nol.
        Assert.assertEquals(0, testResult.size)
    }
}