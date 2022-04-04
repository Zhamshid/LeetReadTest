package kz.nrgteam.leetread.utils.base

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


abstract class BaseFragment : Fragment(), IResourcesIDListener {

    private var isNetworkWasLost = false
    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var connectivityManager: ConnectivityManager? = null
    private var dialogForLoader: Dialog? = null

    /*  Modal windows  */
    open fun toast(context: Context?, msg: Any, isDuration: Boolean = false) {
        context?.let {
            val duration = if (isDuration) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
            when (msg) {
                is String ->
                    Toast.makeText(context, msg, duration).show()
                is Int ->
                    Toast.makeText(context, msg, duration).show()
            }
        }
    }

    fun showLoader() {
        activity?.let {
        }
    }

    fun hideLoader() {
        dialogForLoader?.dismiss()
        killDialog()
    }

    private fun killDialog() {
        if (dialogForLoader != null)
            dialogForLoader = null
    }

    private fun registerNetworkCallback() {
        networkCallback =
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    Log.i("internetConnection", "onAvailable: ")
                }

                override fun onLost(network: Network) {
                    Log.i("internetConnection", "onLost: ")
                    isNetworkWasLost = true
//                    vm.isRefreshing.postValue(false)
                }
            }

        connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.let {
            if (!isNetworkConnected()) {
                isNetworkWasLost = true
            }
            networkCallback?.let { networkCallback ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    it?.registerDefaultNetworkCallback(networkCallback)
                } else {
                    val request = NetworkRequest.Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
                    it?.registerNetworkCallback(request, networkCallback)
                }
            }
        }
    }


    open fun isNetworkConnected(): Boolean {
        connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT < 23) {
            val ni = connectivityManager?.activeNetworkInfo
            if (ni != null) {
                return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == ConnectivityManager.TYPE_MOBILE)
            }
        } else {
            val n = connectivityManager?.activeNetwork
            if (n != null) {
                val nc = connectivityManager?.getNetworkCapabilities(n)
                return nc?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true || nc!!.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                )
            }
        }
        return false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        registerNetworkCallback()
    }

    override fun onDestroy() {
        super.onDestroy()
        networkCallback?.let {
            connectivityManager?.unregisterNetworkCallback(it)
        }
    }

    /*  Resources ID getters  */

    /*
     *  If your app supported more one language,
     *  you can add your locale
     *  example -> yourResources.getString(id);
     */
    override fun getStr(@StringRes id: Int): String = getString(id)

    /*
     * Concat all your text, strings and resources,
     * to one String
     */
    override fun concatStr(text: String): String = text

    /*
     * Get drawable (png, jpg, svg, ....) by ID
     */
    @Nullable
    override fun getImg(@DrawableRes id: Int): Drawable? =
        ContextCompat.getDrawable(requireActivity(), id)

    /*
     * Get color by ID
     */
    @ColorInt
    override fun getClr(@ColorRes id: Int): Int = ContextCompat.getColor(requireActivity(), id)


}