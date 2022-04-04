package kz.nrgteam.leetread.ui.rating.adapter

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.ui.rating.ratingTable.RatingTableFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class RatingTabLayoutAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    companion object {
        val TAB_TITLES = intArrayOf(R.string.class_, R.string.school, R.string.country)
    }

    override fun getItemCount() =
        TAB_TITLES.size


    override fun createFragment(position: Int): Fragment {
        return RatingTableFragment(position)
    }
}