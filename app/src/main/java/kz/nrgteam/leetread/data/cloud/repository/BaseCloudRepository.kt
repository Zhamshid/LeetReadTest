package kz.nrgteam.leetread.data.cloud.repository

import kz.nrgteam.leetread.data.cloud.ResultWrapper
import kz.nrgteam.leetread.dto.UserResponseDto
import kz.nrgteam.leetread.dto.kitaphana.Book
import kz.nrgteam.leetread.dto.kitaphana.BookItem
import kz.nrgteam.leetread.dto.kitaphana.ParentBookItem
import kz.nrgteam.leetread.dto.kitaphana.Quote
import kz.nrgteam.leetread.dto.user.Follower
import kz.nrgteam.leetread.dto.user.User
import kz.nrgteam.leetread.model.auth.UserRequest
import kz.nrgteam.leetread.ui.home.adapters.HomeChildVHUI
import androidx.paging.PagingData
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import org.json.JSONObject

interface BaseCloudRepository {

    suspend fun register(userRequest: UserRequest): ResultWrapper<JSONObject>
    suspend fun login(userRequest: UserRequest): ResultWrapper<UserResponseDto>
    suspend fun follow(id: Int): ResultWrapper<JSONObject>
    suspend fun unFollow(id: Int): ResultWrapper<JSONObject>
    suspend fun getFollowers(id: Int): ResultWrapper<List<Follower>>
    suspend fun getFollowings(id: Int): ResultWrapper<List<Follower>>
    suspend fun getProfile(id: Int): ResultWrapper<User>
    suspend fun getBook(id: Int): ResultWrapper<BookItem>
    suspend fun getCountryRating(): ResultWrapper<List<Follower>>
    suspend fun getFirstPageBooks(): Flow<List<ParentBookItem>>
    suspend fun getMyLibraryBooks(id: Int): ResultWrapper<List<Book>>
    suspend fun getLastOpenedBooks(): ResultWrapper<List<Book>>
    fun getNews(): Flow<PagingData<HomeChildVHUI>>
    suspend fun getSchoolRating(id: Int): ResultWrapper<List<Follower>>
    suspend fun getClassRating(schoolId: Int, grade: Int): ResultWrapper<List<Follower>>
    suspend fun getAllBooks(): ResultWrapper<List<Book>>
    suspend fun getQuotes(userId: Int): ResultWrapper<List<Quote>>
    suspend fun getDailyChallenge(): ResultWrapper<List<Follower>>
    suspend fun getWeeklyChallenge(): ResultWrapper<List<Follower>>
    suspend fun getMonthlyChallenge(): ResultWrapper<List<Follower>>
    suspend fun updateUserImage(multipartBody: MultipartBody.Part?): ResultWrapper<JsonArray>
    suspend fun updateUserPassword(password: String): ResultWrapper<JsonObject>
    suspend fun updateAnnualGoal(number: Int): ResultWrapper<JsonObject>
    suspend fun setAnnualGoal(number: Int): ResultWrapper<JsonObject>
    suspend fun requestForNewPassword(email: String): ResultWrapper<JsonObject>
    suspend fun changeLastReadPageNumber(
        b: CloudRepository.BookLastPage
    ): ResultWrapper<JsonObject>

    fun getSearchedUsers(query: String): Flow<PagingData<Follower>>
}