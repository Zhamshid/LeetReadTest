package kz.nrgteam.leetread.ui.home.paging

import kz.nrgteam.leetread.data.cloud.rest.ApiService
import kz.nrgteam.leetread.data.prefs.Prefs
import kz.nrgteam.leetread.dto.home.NewsType
import kz.nrgteam.leetread.dto.kitaphana.Kitap
import kz.nrgteam.leetread.model.Action
import kz.nrgteam.leetread.model.Article
import kz.nrgteam.leetread.model.home.News
import kz.nrgteam.leetread.ui.home.adapters.HomeChildVHUI
import kz.nrgteam.leetread.utils.Constants.STARTING_PAGE_INDEX
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException

class NewsPagingSource(
    private val service: ApiService,
    private val prefs: Prefs
) : PagingSource<Int, HomeChildVHUI>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomeChildVHUI> {
        val pageIndex = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = service.getNews(
                prefs.getBaseUrl()+"main-page/news/$pageIndex",
            )
            val news = response.map { toNews(it.toNews()) }
            val nextKey =
                if (news.isEmpty()) {
                    null
                } else {
                    pageIndex + 1
                }
            LoadResult.Page(
                data = news,
                prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    private fun toNews(news1: News): HomeChildVHUI {
        return when (news1.newsType) {
            NewsType.FINISH_BOOK -> {
                HomeChildVHUI.ActionBookUI(
                    Action(
                        userName = news1.firstName + " " + news1.lastName,
                        news1.userImage,
                        news1.followerId ?: 0,
                        news1.time,
                        Kitap(
                            id = news1.bookId,
                            name = news1.bookName,
                            bookImage = news1.bookImage,
                        ),
                        "${news1.firstName} кітап бітірді"
                    )
                )
            }
            NewsType.ADDED_BOOK -> {
                HomeChildVHUI.ActionBookUI(
                    Action(
                        news1.firstName + " " + news1.lastName,
                        news1.userImage,
                        news1.followerId ?: 0,
                        news1.time,
                        Kitap(
                            id = news1.bookId,
                            name = news1.bookName,
                            bookImage = news1.bookImage,
                        ),
                        "${news1.firstName} кітапті кітапханасына қосты"
                    )
                )
            }
            NewsType.FOLLOWED_TO_SOMEBODY -> {
                HomeChildVHUI.ActionUI(
                    Action(
                        news1.firstName + " " + news1.lastName,
                        news1.userImage,
                        news1.followerId ?: 0,
                        news1.time,
                        null,
                        "${news1.secondUserFullName}",
                        secondUserId = news1.followingId
                    )
                )
            }
            NewsType.ARTICLE -> {
                HomeChildVHUI.ArticleUI(
                    Article(
                        image = news1.bookImage,
                        title = "",
                        text = "",
                        null
                    )
                )
            }
        }
    }

    /**
     * The refresh key is used for subsequent calls to PagingSource.Load after the initial load.
     */
    override fun getRefreshKey(state: PagingState<Int, HomeChildVHUI>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}