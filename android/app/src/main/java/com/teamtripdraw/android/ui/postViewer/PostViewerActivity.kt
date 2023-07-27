package com.teamtripdraw.android.ui.postViewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityPostViewerBinding

class PostViewerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_viewer)

        binding = ActivityPostViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        val adapter = PostViewerAdapter(::onPostCLick)
//        adapter.submitList(getProductItemList())todo: 서버를 통해 받아온 데이터를 이 함수를 이용하여 리스트에 넣는다
        binding.rvPostViewer.adapter = adapter
    }

    private fun onPostCLick(postId: Long) {
        // todo: 포스트가 클릭되었을 때 실행되는 함수로서 상세 조회 화면으로 넘어가는 작업 필요
    }
}
