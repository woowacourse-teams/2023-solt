package com.teamtripdraw.android.ui.post.viewer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiPostItem
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import kotlinx.coroutines.launch

class PostViewerViewModel(
    private val tripRepository: TripRepository,
    private val postRepository: PostRepository
) : ViewModel() {

    private val _posts: MutableLiveData<List<UiPostItem>> = MutableLiveData()
    val posts: LiveData<List<UiPostItem>> = _posts

    private val _postClickedEvent = MutableLiveData<Event<Long>>()
    val postClickedEvent: LiveData<Event<Long>> = _postClickedEvent


    fun getPosts() {
        viewModelScope.launch {
            val tripId = tripRepository.getCurrentTripId()
            postRepository.getAllPosts(tripId)
                .onSuccess { posts ->
                    _posts.value = posts.map { post -> post.toPresentation() }
                }
                .onFailure {
                    // todo : 에러 발생 시 스낵바 보이기
                }
        }
    }

    fun showDetailPost(id: Long) {
        _postClickedEvent.value = Event(id)
    }
}
