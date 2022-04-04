package kz.nrgteam.leetread.ui.reader

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.data.prefs.Prefs
import kz.nrgteam.leetread.databinding.FragmentReaderBinding
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReaderFragment : Fragment(R.layout.fragment_reader) {

    private var _binding: FragmentReaderBinding? = null
    val binding get() = _binding!!
    private val navArgs: ReaderFragmentArgs by navArgs()
    private val closeUrl = "https://katev-kitap-interface.abmco.kz/#/close"

    @Inject
    lateinit var prefs: Prefs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentReaderBinding.bind(view)
        initUI()
        setupOnBackPressed()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initUI() {
        val webView = binding.webView
        webView.webChromeClient = WebChromeClient()
        clear()

        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.displayZoomControls = false
        webView.settings.builtInZoomControls = true
        webView.settings.domStorageEnabled = true

        webView.settings.allowFileAccess = true
        webView.settings.allowContentAccess = true
        webView.settings.allowFileAccessFromFileURLs = true
        webView.settings.allowUniversalAccessFromFileURLs = true

        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        webView.webViewClient = WebViewClient()

        val bookId = navArgs.book.id
        val bookName = navArgs.book.title
        val epubFile = navArgs.book.epubFile
        val lastPage = navArgs.book.lastPage
        val pageCountProgress = navArgs.book.page_count_progress
        val pageCount = navArgs.book.pageCount
        val userId = prefs.getUserId()
        val token = prefs.getAccessToken()

        val url: String
        if (lastPage.isNullOrEmpty()) {
            url = "https://katev-kitap-interface.abmco.kz/#/book-viewer?" +
                    "token=$token" +
                    "&bookName=$bookName" +
                    "&epubFile=$epubFile" +
                    "&pageCountProgress=$pageCountProgress" +
                    "&pageCount=$pageCount" +
                    "&userId=$userId" +
                    "&baseURL=${prefs.getBaseUrl()}" +
                    "&bookId=$bookId"
            webView.loadUrl(
                url
            )
        } else {
            url =
                "https://katev-kitap-interface.abmco.kz/#/book-viewer?" +
                        "token=$token" +
                        "&bookName=$bookName" +
                        "&epubFile=$epubFile" +
                        "&pageCountProgress=$pageCountProgress" +
                        "&lastPage=$lastPage" +
                        "&pageCount=$pageCount" +
                        "&userId=$userId" +
                        "&baseURL=${prefs.getBaseUrl()}" +
                        "&bookId=$bookId"
            webView.loadUrl(
                url
            )
        }
//        webView.loadUrl("javascript:localStorage.setItem('token', $token);")
        val webViewClient: WebViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
//                webView.loadUrl("javascript:localStorage.setItem('token', $token);")
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                if (Uri.parse(url).host == closeUrl) {
                    // This is my web site, so do not override; let my WebView load the page
                    return false
                }
                return true
            }

            override fun onPageFinished(view: WebView?, url: String) {
                Log.e("WebView", "onPageStarted$url")
                super.onPageFinished(view, url)
            }

            var count = 0
            override fun doUpdateVisitedHistory(
                view: WebView, url: String?,
                isReload: Boolean
            ) {
                Log.e("TAG", "doUpdateVisited History: " + view.url)
                if (count == 0 && view.url.equals(closeUrl)) {
                    findNavController().popBackStack()
                    count++
                    return
                }
            }

            override fun onReceivedError(
                view: WebView, errorCode: Int,
                description: String?, failingUrl: String?
            ) {
                Log.e("TAG", "onReceivedError: " + view.url)
            }


        }
        val webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {
                return true
            }

            override fun onJsConfirm(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {
                return true
            }

            override fun onJsPrompt(
                view: WebView?, url: String?, message: String?, defaultValue: String?,
                result: JsPromptResult?
            ): Boolean {
                return true
            }

        }
        webView.webChromeClient = webChromeClient
        webView.webViewClient = webViewClient
        binding.webView.addJavascriptInterface(
            JavaScriptInterface(
                requireActivity(),
                binding.webView
            ), "AndroidFunction"
        )
    }

    private fun clear() {
        val webView = binding.webView
        Log.e("TAG", "clear: ")
        webView.clearCache(true)
        webView.clearHistory()
        clearCookies(requireContext())
    }


    private fun clearCookies(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
        } else {
            val cookieSyncMngr = CookieSyncManager.createInstance(context)
            cookieSyncMngr.startSync()
            val cookieManager: CookieManager = CookieManager.getInstance()
            cookieManager.removeAllCookie()
            cookieManager.removeSessionCookie()
            cookieSyncMngr.stopSync()
            cookieSyncMngr.sync()
        }
    }

    private fun setupOnBackPressed() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.webView.loadUrl(closeUrl)
                isEnabled = false
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), onBackPressedCallback)
    }
}

class JavaScriptInterface(
    var mContext: Activity,
    private val webView: WebView
) {
    @JavascriptInterface
    fun reload() {
        mContext.runOnUiThread {
            webView.reload()
        }
    }
}