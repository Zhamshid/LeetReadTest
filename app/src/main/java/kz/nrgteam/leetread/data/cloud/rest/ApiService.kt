package kz.nrgteam.leetread.data.cloud.rest

import kz.nrgteam.leetread.data.cloud.repository.CloudRepository
import kz.nrgteam.leetread.dto.UserResponseDto
import kz.nrgteam.leetread.dto.home.NewsDto
import kz.nrgteam.leetread.dto.kitaphana.Book
import kz.nrgteam.leetread.dto.kitaphana.BookItem
import kz.nrgteam.leetread.dto.kitaphana.ParentBookItem
import kz.nrgteam.leetread.dto.kitaphana.Quote
import kz.nrgteam.leetread.dto.user.Follower
import kz.nrgteam.leetread.dto.user.User
import kz.nrgteam.leetread.model.auth.UserRequest
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @GET
    suspend fun register(
        @Url url:String,
        @Body user: UserRequest
    ): Response<JSONObject>


    @POST
    suspend fun login(
        @Url url:String,
        @Body user: UserRequest
    ): Response<UserResponseDto>

    @POST
    suspend fun subscribe(
        @Url url:String
    ): Response<JSONObject>

    @POST
    suspend fun unSubscribe(
        @Url url:String
    ): Response<JSONObject>

    @GET
    suspend fun getFollowers(
        @Url url:String
    ): Response<List<Follower>>

    @GET
    suspend fun getFollowings(
        @Url url:String
    ): Response<List<Follower>>

    @GET
    suspend fun getProfile(
        @Url url:String
    ): Response<User>

    @GET
    suspend fun getBook(
        @Url url:String
    ): Response<BookItem>

    @GET
    suspend fun getCountryRating(
        @Url url:String,
    ): Response<List<Follower>>

    @POST
    suspend fun getSchoolRating(
        @Url url:String,
        @Body schoolId: JsonObject
    ): Response<List<Follower>>

    @POST
    suspend fun getGradeRating(
        @Url url:String,
        @Body grade: JsonObject
    ): Response<List<Follower>>

    @GET
    suspend fun getDailyChallenge(
        @Url url:String,
    ): Response<List<Follower>>

    @GET
    suspend fun getWeeklyChallenge(
        @Url url:String,
    ): Response<List<Follower>>

    @GET
    suspend fun getMonthlyChallenge(
        @Url url:String,
    ): Response<List<Follower>>

    @GET
    suspend fun getFirstPageBooks(
        @Url url:String,
    ): Response<List<ParentBookItem>>

    @GET
    suspend fun getMyLibraryBooks(
        @Url url:String,
    ): Response<List<Book>>

    @GET
    suspend fun getLastOpenedBooks(
        @Url url:String,
    ): Response<List<Book>>

    @GET
    suspend fun getAllBooks(
        @Url url:String,
    ): Response<List<Book>>

    @GET
    suspend fun getQuotes(
        @Url url:String,
    ): Response<List<Quote>>

    @GET
    suspend fun getNews(
        @Url url:String,
    ): List<NewsDto>

    @POST
    suspend fun changeLastPageNumber(
        @Url url:String,
        @Body changeLastPage: CloudRepository.BookLastPage
    ): Response<JsonObject>

    @Multipart
    @PUT
    suspend fun updateUserImage(
        @Url url:String,
        @Part coverFile: MultipartBody.Part?
    ): Response<JsonArray>

    @PUT
    suspend fun updateUserPassword(
        @Url url:String,
        @Body password: JsonObject
    ): Response<JsonObject>

    @PUT
    suspend fun updateAnnualGoal(
        @Url url:String,
        @Body goal: JsonObject
    ): Response<JsonObject>

    @POST
    suspend fun setAnnualGoal(
        @Url url:String,
        @Body goal: JsonObject
    ): Response<JsonObject>

    @GET
    suspend fun requestForNewPassword(
        @Url url:String,
    ): Response<JsonObject>

    @POST
    suspend fun searchUsers(
        @Url url:String,
        @Body body: JsonObject
    ): List<Follower>

}
