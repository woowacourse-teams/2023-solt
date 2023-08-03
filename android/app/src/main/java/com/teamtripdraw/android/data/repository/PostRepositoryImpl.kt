package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.data.dataSource.post.PostDataSource
import com.teamtripdraw.android.data.model.mapper.toData
import com.teamtripdraw.android.data.model.mapper.toDomain
import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.model.post.PrePost
import com.teamtripdraw.android.domain.repository.PostRepository

class PostRepositoryImpl(
    private val remotePostDataSource: PostDataSource.Remote,
) : PostRepository {

    override suspend fun addPost(
        prePost: PrePost
    ): Result<Long> {
        return remotePostDataSource.addPost(
            prePost.toData()
        )
    }

    override suspend fun getPost(postId: Long): Result<Post> {
        return remotePostDataSource.getPost(postId).map { it.toDomain() }
    }

    override suspend fun getAllPosts(tripId: Long): Result<List<Post>> {
        return remotePostDataSource.getAllPosts(tripId)
            .map { dataPosts -> dataPosts.map { it.toDomain() } }
    }

    override suspend fun deletePost(postId: Long): Result<Unit> {
        return remotePostDataSource.deletePost(postId)
    }
}