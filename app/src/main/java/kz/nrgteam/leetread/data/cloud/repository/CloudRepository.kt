package kz.nrgteam.leetread.data.cloud.repository

import kz.nrgteam.leetread.data.cloud.ResultWrapper
import kz.nrgteam.leetread.data.cloud.rest.ApiService
import kz.nrgteam.leetread.data.cloud.safeApiCall
import kz.nrgteam.leetread.data.prefs.Prefs
import kz.nrgteam.leetread.dto.UserResponseDto
import kz.nrgteam.leetread.dto.kitaphana.Book
import kz.nrgteam.leetread.dto.kitaphana.BookItem
import kz.nrgteam.leetread.dto.kitaphana.ParentBookItem
import kz.nrgteam.leetread.dto.user.Follower
import kz.nrgteam.leetread.dto.user.User
import kz.nrgteam.leetread.model.auth.UserRequest
import kz.nrgteam.leetread.ui.home.adapters.HomeChildVHUI
import kz.nrgteam.leetread.ui.home.paging.NewsPagingSource
import kz.nrgteam.leetread.ui.search_user.SearchPagingSource
import kz.nrgteam.leetread.utils.Constants.NEWS_NETWORK_PAGE_SIZE
import kz.nrgteam.leetread.utils.Constants.SEARCH_NETWORK_PAGE_SIZE
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CloudRepository @Inject constructor(
    private val api: ApiService,
    private val prefs: Prefs
) : BaseCloudRepository {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    override suspend fun register(userRequest: UserRequest): ResultWrapper<JSONObject> {
        return safeApiCall(dispatcher) {
            api.register(prefs.getBaseUrl() + "auth/register", userRequest)
        }
    }

    override suspend fun login(userRequest: UserRequest): ResultWrapper<UserResponseDto> {
        return safeApiCall(dispatcher) {
            api.login(prefs.getBaseUrl() + "auth/login", userRequest)
        }
    }

    override suspend fun follow(id: Int): ResultWrapper<JSONObject> {
        return safeApiCall(dispatcher) {
            api.subscribe(prefs.getBaseUrl() + "user/follow/${id}")
        }
    }

    override suspend fun unFollow(id: Int): ResultWrapper<JSONObject> {
        return safeApiCall(dispatcher) {
            api.unSubscribe(prefs.getBaseUrl() + "user/unfollow/${id}")
        }
    }

    override suspend fun getFollowers(id: Int): ResultWrapper<List<Follower>> {
        return safeApiCall(dispatcher) {
            api.getFollowers(prefs.getBaseUrl() + "user/followers/${id}")
        }
    }

    override suspend fun getFollowings(id: Int): ResultWrapper<List<Follower>> {
        return safeApiCall(dispatcher) {
            api.getFollowings(prefs.getBaseUrl() + "user/followings/${id}")
        }
    }

    override suspend fun getProfile(id: Int): ResultWrapper<User> {
        return safeApiCall(dispatcher) {
            api.getProfile(prefs.getBaseUrl() + "user/profile/${id}")
        }
    }

    override suspend fun getBook(id: Int): ResultWrapper<BookItem> {
        return safeApiCall(dispatcher) {
            api.getBook(prefs.getBaseUrl() + "book/${id}")
        }
    }

    override suspend fun getCountryRating(): ResultWrapper<List<Follower>> {
        return safeApiCall(dispatcher) {
            api.getCountryRating(prefs.getBaseUrl() + "rating")
        }
    }

    override suspend fun getSchoolRating(id: Int): ResultWrapper<List<Follower>> {
        return safeApiCall(dispatcher) {
            val paramObject = JsonObject().apply { addProperty("schoolId", id) }
            api.getSchoolRating(prefs.getBaseUrl() + "rating/school", paramObject)
        }
    }

    override suspend fun getClassRating(schoolId: Int, grade: Int): ResultWrapper<List<Follower>> {
        return safeApiCall(dispatcher) {
            val paramObject = JsonObject().apply {
                addProperty("schoolId", schoolId)
                addProperty("grade", grade)
            }
            api.getGradeRating(prefs.getBaseUrl() + "rating/grade", paramObject)
        }
    }

    override suspend fun getDailyChallenge(): ResultWrapper<List<Follower>> {
        return safeApiCall(dispatcher) {
            api.getDailyChallenge(prefs.getBaseUrl() + "challenges/by-day")
        }
    }

    override suspend fun getWeeklyChallenge(): ResultWrapper<List<Follower>> {
        return safeApiCall(dispatcher) {
            api.getWeeklyChallenge(prefs.getBaseUrl() + "challenges/by-week")
        }
    }

    override suspend fun getMonthlyChallenge(): ResultWrapper<List<Follower>> {
        return safeApiCall(dispatcher) {
            api.getMonthlyChallenge(prefs.getBaseUrl() + "challenges/by-month")
        }
    }

    override suspend fun updateUserImage(
        multipartBody: MultipartBody.Part?
    ): ResultWrapper<JsonArray> {
        return safeApiCall(dispatcher) {
            api.updateUserImage(prefs.getBaseUrl() + "user/cover", coverFile = multipartBody)
        }
    }

    override suspend fun updateUserPassword(
        password: String
    ): ResultWrapper<JsonObject> {
        return safeApiCall(dispatcher) {
            val paramObject = JsonObject().apply { addProperty("password", password) }
            api.updateUserPassword(prefs.getBaseUrl() + "user/profile/update_password", paramObject)
        }
    }

    override suspend fun updateAnnualGoal(number: Int): ResultWrapper<JsonObject> {
        return safeApiCall(dispatcher) {
            val paramObject = JsonObject().apply { addProperty("booksToFinish", number) }
            api.updateAnnualGoal(prefs.getBaseUrl() + "aim", paramObject)
        }
    }
    override suspend fun setAnnualGoal(number: Int): ResultWrapper<JsonObject> {
        return safeApiCall(dispatcher) {
            val paramObject = JsonObject().apply { addProperty("booksToFinish", number) }
            api.setAnnualGoal(prefs.getBaseUrl() + "aim", paramObject)
        }
    }

    override suspend fun requestForNewPassword(email: String): ResultWrapper<JsonObject> {
        return safeApiCall(dispatcher) {
            api.requestForNewPassword(
                prefs.getBaseUrl() + "user/profile/forgot_password/${email}"
            )
        }
    }


    override suspend fun getFirstPageBooks(): Flow<List<ParentBookItem>> {
        return flow {
            api.getFirstPageBooks(prefs.getBaseUrl() + "book")
        }
    }

    override suspend fun getMyLibraryBooks(id: Int): ResultWrapper<List<Book>> {
        return safeApiCall(dispatcher) {
            api.getMyLibraryBooks(prefs.getBaseUrl() + "book/library/${id}")
        }
    }

    override suspend fun getLastOpenedBooks(): ResultWrapper<List<Book>> {
        return safeApiCall(dispatcher) {
            api.getLastOpenedBooks(prefs.getBaseUrl() + "book/")
        }
    }

    override suspend fun getAllBooks() =
        withContext(dispatcher) {
            val res = api.getAllBooks(prefs.getBaseUrl() + "book/")
            if (res.isSuccessful) {
                ResultWrapper.Success(res.body()!!)
            } else {
                ResultWrapper.Error(code = res.code())
            }
        }

    override suspend fun getQuotes(userId: Int) =
        withContext(dispatcher) {
            val res = api.getQuotes(prefs.getBaseUrl() + "quotes/user/${userId}")
            if (res.isSuccessful) {
                ResultWrapper.Success(res.body()!!)
            } else {
                ResultWrapper.Error(code = res.code())
            }
        }

    override suspend fun changeLastReadPageNumber(
        b: BookLastPage
    ): ResultWrapper<JsonObject> {
        return safeApiCall(dispatcher) {
            api.changeLastPageNumber(prefs.getBaseUrl() + "book/library/activity", b)
        }
    }

    data class BookLastPage(
        val userId: String,
        val bookId: String,
        val percentage: Double,
        val pageCount: Int,
        val lastPage: String,
        val timeSpend: Int,
        val totalPageCount: Int
    )

    override fun getNews(): Flow<PagingData<HomeChildVHUI>> {
        return Pager(
            config = PagingConfig(
                pageSize = NEWS_NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsPagingSource(service = api, prefs)
            }
        ).flow
    }

    override fun getSearchedUsers(query: String): Flow<PagingData<Follower>> {
        return Pager(
            config = PagingConfig(
                pageSize = SEARCH_NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchPagingSource(service = api, query, prefs)
            }
        ).flow
    }

    companion object {
        const val TAG = "ABO_CLOUD_REPOSITORY"
    }


}