package kz.nrgteam.leetread.ui.profile.btm_sheet_annual_score

import kz.nrgteam.leetread.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnnualScoreChangeFragment: BottomSheetDialogFragment() {

    fun newInstance(): Fragment{
        val args = Bundle()
        
        val fragment = AnnualScoreChangeFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(
            R.layout.dialog_change_annual_score, container,
            false
        )
        val numberPicker: NumberPicker = view.findViewById(R.id.numberPicker)
        val btn_rastau: Button = view.findViewById(R.id.btn_rastau)

        btn_rastau.setOnClickListener {
            dismiss()
        }
        numberPicker.minValue = 0
        numberPicker.maxValue = 10
        return view
    }
}