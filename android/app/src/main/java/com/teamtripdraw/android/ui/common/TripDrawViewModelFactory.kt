package com.teamtripdraw.android.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.teamtripdraw.android.TripDrawApplication
import com.teamtripdraw.android.ui.common.dialog.SetTripTitleDialogViewModel
import com.teamtripdraw.android.ui.history.HistoryViewModel
import com.teamtripdraw.android.ui.home.HomeViewModel
import com.teamtripdraw.android.ui.post.detail.PostDetailViewModel
import com.teamtripdraw.android.ui.post.viewer.PostViewerViewModel
import com.teamtripdraw.android.ui.postWriting.PostWritingViewModel
import com.teamtripdraw.android.ui.signUp.NicknameSetupViewModel

private const val UNDEFINED_VIEW_MODEL_ERROR = "ViewModelFactory에 정의되지않은 뷰모델을 생성하였습니다 : %s"

@Suppress("UNCHECKED_CAST")
val tripDrawViewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        with(modelClass) {
            val dependencyContainer = TripDrawApplication.DependencyContainer
            val repositoryContainer = dependencyContainer.repositoryContainer
            when {
                isAssignableFrom(NicknameSetupViewModel::class.java) ->
                    NicknameSetupViewModel(repositoryContainer.nicknameSetupRepository)
                isAssignableFrom(PostWritingViewModel::class.java) ->
                    PostWritingViewModel(
                        repositoryContainer.pointRepository,
                        repositoryContainer.postRepository,
                        repositoryContainer.tripRepository
                    )
                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel(
                        repositoryContainer.tripRepository,
                        repositoryContainer.pointRepository
                    )
                isAssignableFrom(PostDetailViewModel::class.java) ->
                    PostDetailViewModel(repositoryContainer.postRepository)
                isAssignableFrom(PostViewerViewModel::class.java) ->
                    PostViewerViewModel(
                        repositoryContainer.tripRepository,
                        repositoryContainer.postRepository
                    )
                isAssignableFrom(HistoryViewModel::class.java) ->
                    HistoryViewModel() // todo: repository 추가
                isAssignableFrom(SetTripTitleDialogViewModel::class.java) ->
                    SetTripTitleDialogViewModel(repositoryContainer.tripRepository)
                else ->
                    throw IllegalArgumentException(UNDEFINED_VIEW_MODEL_ERROR.format(modelClass.name))
            }
        } as T
}
