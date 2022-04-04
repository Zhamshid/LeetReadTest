package kz.nrgteam.leetread.ui.settings

import kz.nrgteam.leetread.MainActivity
import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.databinding.FragmentUserSettingsBinding
import kz.nrgteam.leetread.ui.settings.dialog.DialogLeaveYesNo
import kz.nrgteam.leetread.utils.navigate
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*


@AndroidEntryPoint
class UserSettingsFragment : Fragment(R.layout.fragment_user_settings) {

    private lateinit var binding: FragmentUserSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()
    private var takePictureActivityResultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            onPictureGot(TAKE_PICTURE, data)
        }
    }
    private var getFromGalleryActivityResultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            onPictureGot(TAKE_FROM_GALLERY, data)
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Log.i(TAG, "handleOnBackPressed: ${viewModel.wasChanges}")
            if (!viewModel.wasChanges) {
                isEnabled = false
                findNavController().popBackStack()
            } else {
                discardChanges()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserSettingsBinding.bind(view)
        initUI()
        setOnClickListener()
        collectData()
    }

    private fun collectData() {
        lifecycleScope.launchWhenResumed {
            viewModel.stateFlow.collectLatest { result ->
                when (result) {
                    SettingsUI.Loading -> {
                        binding.loading()
                    }
                    SettingsUI.NotLoading -> {
                        binding.notLoading()
                    }
                    SettingsUI.Success -> {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun FragmentUserSettingsBinding.loading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun FragmentUserSettingsBinding.notLoading() {
        progressBar.visibility = View.GONE
    }

    private fun initUI() {
        val activity = activity as? MainActivity
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        requireActivity().run {
            onBackPressedDispatcher.addCallback(requireActivity(), onBackPressedCallback)
//            findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.setNavigationOnClickListener {
//                if (viewModel.wasChanges) {
//                    discardChanges()
//                } else {
//                    findNavController().popBackStack()
//                }
//            }
        }
        binding.run {
            viewModel.imageBitmap?.let {
                setImageFromBitmap(it)
            }
            if (viewModel.imageBitmap == null && viewModel.userImage != null) {
                Glide.with(requireActivity())
                    .load(viewModel.userImage)
                    .into(userImage)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (viewModel.wasChanges) {
                    discardChanges()
                } else {
                    findNavController().popBackStack()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setOnClickListener() {
//        requireActivity().findViewById<ImageView>(R.id.save)?.run {
//            setOnClickListener {
//                callUploadImageService(viewModel.imageBitmap)
//            }
//        }
        binding.run {
            logoutContainer.setOnClickListener {
                logUserOut()
            }
            passwordContainer.setOnClickListener {
                navigateToChangePassword()
            }
            annualGoalContainer.setOnClickListener {
                navigateToChangeAnnualGoal()
            }
            imageOuterCardView.setOnClickListener {
                if (checkAndRequestPermissions()) {
                    chooseImage(requireActivity())
                }
            }
        }
    }

    private fun logUserOut() {
        childFragmentManager.beginTransaction().add(DialogLeaveYesNo(), "logout").commit()
    }

    private fun discardChanges() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Өзгерістерді сақтайық па?").setPositiveButton(
            "Ия"
        ) { p0, _ ->
            callUploadImageService(viewModel.imageBitmap)
            p0.dismiss()
        }.setNegativeButton("Жоқ") { p0, _ ->
            p0.dismiss()
            findNavController().popBackStack()
        }.show()
    }

    private fun navigateToChangePassword() {
        val n =
            UserSettingsFragmentDirections.actionUserSettingsFragmentToChangePasswordFragment()
        navigate(n)
    }

    private fun navigateToChangeAnnualGoal() {
        val n =
            UserSettingsFragmentDirections.actionUserSettingsFragmentToChangeAnnualGoalFragment(
                viewModel.userAnnualGoalNumber
            )
        navigate(n)
    }

    private fun callUploadImageService(bitmap: Bitmap?) {
        val file = bitmapToFile(bitmap = bitmap)
        val body = requestToUploadData(file)
        viewModel.updateUserInfo(body)
    }

    private fun bitmapToFile(filename: String = "img.jpeg", bitmap: Bitmap?): File {
        val f = File(requireContext().cacheDir, filename)
        f.createNewFile()
        val bos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, bos)
        val bitmapData = bos.toByteArray()
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(f)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitmapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return f
    }

    private fun requestToUploadData(f: File): MultipartBody.Part {
        val reqFile: RequestBody = f.asRequestBody("image/*".toMediaType())
        return MultipartBody.Part.createFormData("coverFile", f.name, reqFile)
    }

    private fun chooseImage(context: Context) {
        val optionsMenu = arrayOf<CharSequence>(
            "Суретке түсіру",
            "Галлереядан алу",
            "Өшіру"
        )
        val builder = AlertDialog.Builder(context)
        builder.setItems(optionsMenu) { _, i ->
            when {
                optionsMenu[i] == "Суретке түсіру" -> {
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    takePictureActivityResultLauncher.launch(
                        takePicture
                    )
                }
                optionsMenu[i] == "Галлереядан алу" -> {
                    val photoPickerIntent = Intent(Intent.ACTION_PICK)
                    photoPickerIntent.type = "image/*"
                    getFromGalleryActivityResultLauncher.launch(photoPickerIntent)
                }
                optionsMenu[i] == "Өшіру" -> {
                    binding.setImageFromBitmap(null)
                }
            }
        }
        builder.show()
    }

    private fun checkAndRequestPermissions(): Boolean {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val cameraPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        )
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                .add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(), listPermissionsNeeded
                    .toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(
                        requireContext(),
                        "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT
                    )
                        .show()
                }
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(
                        requireContext(),
                        "FlagUp Requires Access to Your Storage.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    chooseImage(requireContext())
                }
            }
        }
    }


    private fun onPictureGot(requestCode: Int, data: Intent?) {
        when (requestCode) {
            TAKE_PICTURE -> if (data != null) {
                val bitmap = data.extras!!["data"] as Bitmap?
                binding.setImageFromBitmap(bitmap)
            }
            TAKE_FROM_GALLERY -> if (data?.data != null) {
                try {
                    val imageUri: Uri = data.data!!
                    val imageStream: InputStream? =
                        requireActivity().contentResolver.openInputStream(imageUri)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    binding.setImageFromBitmap(selectedImage)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun FragmentUserSettingsBinding.setImageFromBitmap(bitmap: Bitmap?) {
        viewModel.imageBitmap = bitmap
        viewModel.wasChanges = true
        changeBackButtonImage()
        if (bitmap == null) {
            userImage.isVisible = false
            userImage.alpha = 0.5f
        } else {
            userImage.setImageBitmap(bitmap)
            userImage.alpha = 1f
            userImage.isVisible = true
        }
    }

    private fun changeBackButtonImage() {
        if (viewModel.imageBitmap != null) {
            (activity as MainActivity).changeBackButtonToCancel()
        } else {
            (activity as MainActivity).changeBackButtonToArrow()
        }
    }

    companion object {
        const val REQUEST_ID_MULTIPLE_PERMISSIONS = 101
        const val TAG = "ABO_USER_SETTINGS"
        const val TAKE_PICTURE = 0
        const val TAKE_FROM_GALLERY = 1
    }

}