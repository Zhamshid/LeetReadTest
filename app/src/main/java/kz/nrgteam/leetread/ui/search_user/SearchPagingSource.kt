package kz.nrgteam.leetread.ui.search_user

import kz.nrgteam.leetread.data.cloud.rest.ApiService
import kz.nrgteam.leetread.data.prefs.Prefs
import kz.nrgteam.leetread.dto.user.Follower
import kz.nrgteam.leetread.utils.Constants.STARTING_PAGE_INDEX
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.JsonObject
import retrofit2.HttpException
import java.io.IOException

class SearchPagingSource(
    private val service: ApiService,
    private val query: String,
    private val prefs: Prefs
) : PagingSource<Int, Follower>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Follower> {
        val pageIndex = params.key ?: STARTING_PAGE_INDEX
        return try {
            val jsonObject = JsonObject().apply { addProperty("searchValue", query) }
            val response = service.searchUsers(
                body = jsonObject,
                url = prefs.getBaseUrl() + "rating/search-user/$pageIndex"
            )
            val search = response
            val nextKey =
                if (search.isEmpty()) {
                    null
                } else {
                    pageIndex + 1
                }
            LoadResult.Page(
                data = search,
                prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    /**
     * The refresh key is used for subsequent calls to PagingSource.Load after the initial load.
     */
    override fun getRefreshKey(state: PagingState<Int, Follower>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}