package kz.nrgteam.leetread.ui.settings

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.FragmentSettingsBinding
import kz.nrgteam.leetread.ui.settings.dialog.DialogLeaveYesNo
import kz.nrgteam.leetread.utils.base.BaseBindingFragment
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseBindingFragment<FragmentSettingsBinding>(R.layout.fragment_settings) {
    private val dialogLeaveYesNo = DialogLeaveYesNo()
    override fun initViews(savedInstanceState: Bundle?) {
        setClickListeners()
    }

    private fun setClickListeners() {
        with(binding) {
            logout.setOnClickListener {
                childFragmentManager.beginTransaction().add(dialogLeaveYesNo, "Hello").commit()
            }
        }
    }

}