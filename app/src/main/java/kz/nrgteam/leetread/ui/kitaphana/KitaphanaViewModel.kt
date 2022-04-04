package kz.nrgteam.leetread.ui.kitaphana

import kz.nrgteam.leetread.data.cloud.ResultWrapper
import kz.nrgteam.leetread.data.cloud.repository.BaseCloudRepository
import kz.nrgteam.leetread.data.prefs.Prefs
import kz.nrgteam.leetread.dto.kitaphana.Book
import kz.nrgteam.leetread.dto.kitaphana.Quote
import kz.nrgteam.leetread.ui.kitaphana.adapters.BookVHUI
import kz.nrgteam.leetread.utils.base.BaseViewModel
import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class KitaphanaViewModel @Inject constructor(
    val baseCloudRepository: BaseCloudRepository,
    val prefs: Prefs
) : BaseViewModel() {

    private val _stateFlow = MutableStateFlow<KitaphanaUI>(KitaphanaUI.Loading)
    val stateFlow = _stateFlow.asStateFlow()

    init {
        _stateFlow.value = KitaphanaUI.Loading
    }

    private fun fetchKitaphana() {
        launchIO {
//            val itemsEditor = listOf<BookVHUI>(
//                BookVHUI.VHEditorChoice("Мектеп бітіргенге дейін оқуып бітіру керек 10 кітап"),
//                BookVHUI.VHEditorChoice("Өмір туралы кітаптар"),
//                BookVHUI.VHEditorChoice("Қызық оқиғалар")
//            )
//            val parentItemEd =
//                KitaphanaParentUI.EditorChoiceUI(itemsEditor)
            val allR = baseCloudRepository.getAllBooks()
            if(allR is ResultWrapper.Error){
                _stateFlow.value = KitaphanaUI.Error(allR.error)
            }
            val quoteR = baseCloudRepository.getQuotes(prefs.getUserId())
            val myLibR = baseCloudRepository.getMyLibraryBooks(prefs.getUserId())
            if (allR is ResultWrapper.Success && quoteR is ResultWrapper.Success && myLibR is ResultWrapper.Success) {
                Log.i(TAG, "fetchKitaphana: ALL API FETCHED CORRECTLY")
                gotAllRequiredForKitaphana(allR, quoteR, myLibR, )//parentItemEd)
            } else if (allR is ResultWrapper.Error && quoteR is ResultWrapper.Success && myLibR is ResultWrapper.Success) {
                Log.e(TAG, "fetchKitaphana: ERROR ALL_BOOKS")
                onlyAllBooksError(quoteR, myLibR)//, parentItemEd)
            } else if (allR is ResultWrapper.Success && quoteR is ResultWrapper.Error && myLibR is ResultWrapper.Success) {
                Log.e(TAG, "fetchKitaphana: ERROR QUOTE")
                onlyQuoteError(allR, myLibR)//, parentItemEd)

            } else if (allR is ResultWrapper.Success && quoteR is ResultWrapper.Success && myLibR is ResultWrapper.Error) {
                Log.e(TAG, "fetchKitaphana: ERROR LAST_OPENED")
                onlyLastOpenedError(allR, quoteR)//, parentItemEd)

            } else if (allR is ResultWrapper.Error && quoteR is ResultWrapper.Error && myLibR is ResultWrapper.Error) {
                _stateFlow.value = KitaphanaUI.Error(null)
            }
        }
    }

    private fun gotAllRequiredForKitaphana(
        allR: ResultWrapper.Success<List<Book>>,
        quoteR: ResultWrapper.Success<List<Quote>>,
        myLibR: ResultWrapper.Success<List<Book>>,
//        parentItemEd: KitaphanaParentUI.EditorChoiceUI
    ) {
        val parentItemQu =
            KitaphanaParentUI.QuoteUI(quoteR.value.map { BookVHUI.VHQuotation(it) })
        val parentItemLast =
            KitaphanaParentUI.LastOpenedUI(myLibR.value.map { BookVHUI.VHLastOpenedBook(it) })
        val parentItemAll =
            KitaphanaParentUI.AllBooksUI(allR.value.map { BookVHUI.VHBook(it) })
        _stateFlow.value = KitaphanaUI.Success(
            listOf(
                parentItemLast,
                parentItemQu,
//                parentItemEd,
                parentItemAll
            )
        )
    }

    private fun onlyAllBooksError(
        quoteR: ResultWrapper.Success<List<Quote>>,
        myLibR: ResultWrapper.Success<List<Book>>,
//        parentItemEd: KitaphanaParentUI.EditorChoiceUI
    ) {
        val parentItemQu =
            KitaphanaParentUI.QuoteUI(quoteR.value.map { BookVHUI.VHQuotation(it) })
        val parentItemLast =
            KitaphanaParentUI.LastOpenedUI(myLibR.value.map { BookVHUI.VHLastOpenedBook(it) })
        _stateFlow.value = KitaphanaUI.Success(
            listOf(
                parentItemLast,
                parentItemQu,
//                parentItemEd,
            )
        )
    }

    private fun onlyQuoteError(
        allR: ResultWrapper.Success<List<Book>>,
        myLibR: ResultWrapper.Success<List<Book>>,
//        parentItemEd: KitaphanaParentUI.EditorChoiceUI
    ) {
        val parentItemLast =
            KitaphanaParentUI.LastOpenedUI(myLibR.value.map { BookVHUI.VHLastOpenedBook(it) })
        val parentItemAll =
            KitaphanaParentUI.AllBooksUI(allR.value.map { BookVHUI.VHBook(it) })
        _stateFlow.value = KitaphanaUI.Success(
            listOf(
                parentItemLast,
//                parentItemEd,
                parentItemAll
            )
        )
    }

    private fun onlyLastOpenedError(
        allR: ResultWrapper.Success<List<Book>>,
        quoteR: ResultWrapper.Success<List<Quote>>,
//        parentItemEd: KitaphanaParentUI.EditorChoiceUI
    ) {
        val parentItemQu =
            KitaphanaParentUI.QuoteUI(quoteR.value.map { BookVHUI.VHQuotation(it) })
        val parentItemAll =
            KitaphanaParentUI.AllBooksUI(allR.value.map { BookVHUI.VHBook(it) })
        _stateFlow.value = KitaphanaUI.Success(
            listOf(
                parentItemQu,
//                parentItemEd,
                parentItemAll
            )
        )
    }

    fun refresh() {
        fetchKitaphana()
    }

    companion object {
        const val TAG = "ABO_KITAPHANA_VM"
    }
}

sealed class KitaphanaUI {
    object Loading : KitaphanaUI()
    object NotRefresh : KitaphanaUI()
    data class Success(val value: List<KitaphanaParentUI>) : KitaphanaUI()
    class Error(val error: String?) : KitaphanaUI()
}

sealed class KitaphanaParentUI {
    data class WeeklyProgressUI(val value: List<BookVHUI>) : KitaphanaParentUI()
    data class BooksUI(val value: List<BookVHUI>) : KitaphanaParentUI()
    data class EditorChoiceUI(val value: List<BookVHUI>) : KitaphanaParentUI()
    data class QuoteUI(val value: List<BookVHUI>) : KitaphanaParentUI()
    data class AllBooksUI(val value: List<BookVHUI>) : KitaphanaParentUI()
    data class LastOpenedUI(val value: List<BookVHUI>) : KitaphanaParentUI()
}