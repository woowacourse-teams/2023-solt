package com.teamtripdraw.android.ui.allPosts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamtripdraw.android.databinding.FragmentAllPostsBinding
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.ui.post.detail.PostDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllPostsFragment : Fragment() {

    private var _binding: FragmentAllPostsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AllPostsAdapter
    private val viewModel: AllPostsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAllPostsBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        bindViewModel()
        initObserver()
        setAdapter()
        addScrollListener()

        return binding.root
    }

    private fun bindViewModel() {
        binding.allPostsViewModel = viewModel
    }

    private fun initObserver() {
        initPostsObserve()
        initOpenPostDetailEventObserve()
    }

    private fun initPostsObserve() {
        viewModel.posts.observe(viewLifecycleOwner) {
            adapter.submitList(it.postItems)
        }
    }

    private fun initOpenPostDetailEventObserve() {
        viewModel.openPostDetailEvent.observe(
            viewLifecycleOwner,
            EventObserver(this@AllPostsFragment::onPostClick),
        )
    }

    private fun onPostClick(postId: Long) {
        startActivity(PostDetailActivity.getIntent(requireContext(), postId))
    }

    private fun setAdapter() {
        adapter = AllPostsAdapter(viewModel::openPostDetail)
        binding.rvAllPosts.adapter = adapter
    }

    private fun addScrollListener() {
        binding.rvAllPosts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = binding.rvAllPosts.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                checkFetchPostsCondition(layoutManager, lastPosition)
            }
        })
    }

    private fun checkFetchPostsCondition(layoutManager: LinearLayoutManager, lastPosition: Int) {
        if (viewModel.hasNextPage &&
            !viewModel.isAddLoading &&
            layoutManager.itemCount <= lastPosition + LOAD_THRESHOLD &&
            !binding.rvAllPosts.canScrollVertically(DOWNWARD_DIRECTION)
        ) {
            viewModel.fetchPosts()
        }
    }

    override fun onResume() {
        super.onResume()
        fetchPosts()
    }

    private fun fetchPosts() = viewModel.fetchPosts()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val LOAD_THRESHOLD = 3
        private const val DOWNWARD_DIRECTION = 1
    }
}
