package kz.nrgteam.leetread.ui.zhetistikter

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.FragmentZhetistikterBinding
import kz.nrgteam.leetread.ui.profile.adapter.ZhetistikterAdapter
import kz.nrgteam.leetread.utils.navigate
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ZhetistikterFragment: Fragment(R.layout.fragment_zhetistikter),
    StatisticsViewHolder.StatisticsAdapterListener {

    private val viewModel:ZhetistikterViewModel by viewModels()
    private var _binding:FragmentZhetistikterBinding? = null
    private val binding get() = _binding!!
    private lateinit var statisticsAdapter: StatisticsAdapter
    private lateinit var zhetistikterAdapter: ZhetistikterAdapter
    private lateinit var zhetistikterAdapterContainer: ZhetistikterAdapterContainer


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentZhetistikterBinding.bind(view)
        initUI()
        setObservers()
    }

    private fun setObservers() {
        viewModel.statisticsLiveData.observe(viewLifecycleOwner){
            statisticsAdapter.submitList(it)
        }
        viewModel.zhetistikterLiveData.observe(viewLifecycleOwner){
            zhetistikterAdapter.submitList(it)
        }
    }

    private fun initUI() {
        initStatisticsAdapter()
        initZhetistikterAdapter()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val concatAdapter = ConcatAdapter(statisticsAdapter, zhetistikterAdapterContainer)
        binding.zhetistikterRecyclerView.apply {
            adapter = concatAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initZhetistikterAdapter() {
        zhetistikterAdapter = ZhetistikterAdapter()
        zhetistikterAdapterContainer = ZhetistikterAdapterContainer(zhetistikterAdapter)
    }

    private fun initStatisticsAdapter() {
        statisticsAdapter = StatisticsAdapter(this)
    }


    override fun readDayNumberClicked() {
        navigateToClickOquProgress()
    }

    override fun totalScoreClicked() {

    }

    override fun readBookNumberClicked() {

    }

    override fun readBookNumberClicked2() {

    }

    private fun navigateToClickOquProgress() {
        val n = ZhetistikterFragmentDirections.actionZhetistikterFragmentToOquProgressFragment()
        navigate(n)
    }
}