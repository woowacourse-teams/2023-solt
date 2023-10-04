package com.teamtripdraw.android.ui.allTrips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.teamtripdraw.android.databinding.FragmentAllTripsBinding
import com.teamtripdraw.android.ui.filter.FilterSelectionActivity
import com.teamtripdraw.android.ui.filter.FilterType
import com.teamtripdraw.android.ui.history.detail.HistoryDetailActivity
import com.teamtripdraw.android.ui.model.UiPreviewTrip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllTripsFragment : Fragment() {

    private var _binding: FragmentAllTripsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AllTripsAdapter
    private val viewModel: AllTripsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAllTripsBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        bindViewModel()
        initObserver()
        setAdapter()

        return binding.root
    }

    private fun bindViewModel() {
        binding.allTripsViewModel = viewModel
    }

    private fun initObserver() {
        initTripsObserve()
        initOpenPostDetailEventObserve()
        initOpenFilterSelectionEventObserve()
    }

    private fun initTripsObserve() {
        viewModel.trips.observe(viewLifecycleOwner) {
            adapter.submitList(it.tripItems)
        }
    }

    private fun initOpenPostDetailEventObserve() {
        viewModel.openHistoryDetailEvent.observe(
            viewLifecycleOwner,
        ) { onTripClick(it) }
    }

    private fun onTripClick(trip: UiPreviewTrip) {
        startActivity(HistoryDetailActivity.getIntent(requireContext(), trip))
    }

    private fun initOpenFilterSelectionEventObserve() {
        viewModel.openFilterSelectionEvent.observe(
            viewLifecycleOwner,
            this::onFilterSelectionClick,
        )
    }

    private fun onFilterSelectionClick(isClicked: Boolean) {
        if (isClicked) {
            val intent =
                FilterSelectionActivity.getIntent(requireContext(), FilterType.TRIP)
            startActivity(intent)
        }
    }

    private fun setAdapter() {
        adapter = AllTripsAdapter(viewModel)
        binding.rvAllTrips.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        fetchTrips()
    }

    private fun fetchTrips() = viewModel.fetchTrips()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
