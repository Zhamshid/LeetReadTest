package kz.nrgteam.leetread.ui.settings.dialog

import kz.nrgteam.leetread.LoginActivity
import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.ui.settings.SettingsViewModel
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogLeaveYesNo: DialogFragment() {
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setMessage("Аккаунттан шыққыңыз келе ме?")

            .setPositiveButton("Ия") { _, _ ->
                requireActivity().startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
                viewModel.logout()
                this.dismiss()
            }
            .setNegativeButton("Жоқ"){_, _ ->
                this.dismiss()
            }
        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        val colorPrimary = ContextCompat.getColor(requireContext(), R.color.primaryColor)
        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(colorPrimary)
    }

}