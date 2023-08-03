package com.teamtripdraw.android.data.dataSource.post

import com.teamtripdraw.android.data.model.DataPost
import com.teamtripdraw.android.data.model.DataPrePost

interface PostDataSource {

    interface Local

    interface Remote {

        suspend fun addPost(
            dataPrePost: DataPrePost
        ): Result<Long>

        suspend fun getPost(postId: Long): Result<DataPost>

        suspend fun getAllPosts(tripId: Long): Result<List<DataPost>>

        suspend fun deletePost(postId: Long): Result<Unit>
    }
}