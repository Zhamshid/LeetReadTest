package kz.nrgteam.leetread.ui.zharystar

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.common.InternalDeepLink
import kz.nrgteam.leetread.databinding.FragmentZharystarBinding
import kz.nrgteam.leetread.dto.user.UserOnline
import kz.nrgteam.leetread.ui.home.adapters.OnlineViewHolder
import kz.nrgteam.leetread.ui.home.adapters.UsersAdapter
import kz.nrgteam.leetread.ui.zharystar.adapter.UserReaderAdapter
import kz.nrgteam.leetread.utils.Constants
import kz.nrgteam.leetread.utils.navigate
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ZharystarFragment : Fragment(R.layout.fragment_zharystar),
    OnlineViewHolder.UserAdapterListener {
    private var _binding: FragmentZharystarBinding? = null
    private val binding get() = _binding!!
    private lateinit var userDailyReaderAdapter: UserReaderAdapter
    private lateinit var exUsersDailyAdapter: UsersAdapter
    private lateinit var exUsersWeeklyAdapter: UsersAdapter
    private lateinit var exUsersMonthlyAdapter: UsersAdapter
    private lateinit var userWeeklyReaderAdapter: UserReaderAdapter
    private lateinit var userMonthlyReaderAdapter: UserReaderAdapter
    private val viewModel: ZharystarViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentZharystarBinding.bind(view)
        initView()
//        setObservers()
        setOnClickListeners()
    }

    private fun initView() {
//        //Init user daily readers photo adapter
//        userDailyReaderAdapter = UserReaderAdapter()
//        exUsersDailyAdapter =
//            UsersAdapter(this, USERS_EX_WINNERS_LIST_HOR_MARGIN, USERS_EX_WINNERS_LIST_ITEM_SIZE)
//        exUsersMonthlyAdapter =
//            UsersAdapter(this, USERS_EX_WINNERS_LIST_HOR_MARGIN, USERS_EX_WINNERS_LIST_ITEM_SIZE)
//        exUsersWeeklyAdapter =
//            UsersAdapter(this, USERS_EX_WINNERS_LIST_HOR_MARGIN, USERS_EX_WINNERS_LIST_ITEM_SIZE)
//        binding.readersRecyclerviewDaily.adapter = userDailyReaderAdapter
//        binding.exWinnersRecyclerviewDay.adapter = exUsersDailyAdapter
//        binding.exWinnersRecyclerviewWeek.adapter = exUsersWeeklyAdapter
//        binding.exWinnersRecyclerviewMonth.adapter = exUsersMonthlyAdapter
//
//        //Init user weekly readers photo adapter
//        userWeeklyReaderAdapter = UserReaderAdapter()
//        binding.readersRecyclerviewWeekly.adapter = userWeeklyReaderAdapter
//
//        //Init user monthly readers photo adapter
//        userMonthlyReaderAdapter = UserReaderAdapter()
//        binding.readersRecyclerviewMonthly.adapter = userMonthlyReaderAdapter
    }

    private fun setObservers() {
        viewModel.usersDaily.observe(viewLifecycleOwner) { list ->
            userDailyReaderAdapter.submitList(list)
            exUsersDailyAdapter.submitList(list.take(3).map { UserOnline(0,it.avatar, false) })
            setTranslationForRV(list.size - 1, binding.readersRecyclerviewDaily)
            binding.exWinnersTitleDay.isVisible = list.isNotEmpty()
        }
        viewModel.usersWeekly.observe(viewLifecycleOwner) { list ->
            userWeeklyReaderAdapter.submitList(list)
            exUsersWeeklyAdapter.submitList(list.map { UserOnline(0,it.avatar, false) })
            binding.exWinnersTitleWeek.isVisible = list.isNotEmpty()

            setTranslationForRV(list.size - 1, binding.readersRecyclerviewWeekly)
        }
        viewModel.usersMonthly.observe(viewLifecycleOwner) { list ->
            userMonthlyReaderAdapter.submitList(list)
            exUsersMonthlyAdapter.submitList(list.map { UserOnline(0,it.avatar, false) })
            setTranslationForRV(list.size - 1, binding.readersRecyclerviewMonthly)
            binding.exWinnersTitleMonth.isVisible = list.isNotEmpty()
        }
    }

    private fun setTranslationForRV(size: Int, view: View) {
        view.translationX =
            getTranslationFloatForRV(size)
    }

    private fun getTranslationFloatForRV(size: Int): Float {
        return size * (resources.getDimension(R.dimen.zharystar_user_reader_image_height_width) / 1.8f)
    }

    private fun setOnClickListeners() {
        binding.zharystarLayoutDaily.setOnClickListener {
            val action = ZharystarFragmentDirections.toZharystarListFragment(Constants.DAILY_NUMBER)
            navigate(action)
        }
        binding.zharystarLayoutWeekly.setOnClickListener {
            val action =
                ZharystarFragmentDirections.toZharystarListFragment(Constants.WEEKLY_NUMBER)
            navigate(action)
        }
        binding.zharystarLayoutMonthly.setOnClickListener {
            val action =
                ZharystarFragmentDirections.toZharystarListFragment(Constants.MONTHLY_NUMBER)
            navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onUserClicked(id: Int) {
        val navigation = (InternalDeepLink.PROFILE + "/${id}").toUri()
        findNavController().navigate(navigation)
    }
}