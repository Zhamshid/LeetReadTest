package kz.nrgteam.leetread.dto.kitaphana

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val _id: String?,
    val authors: List<String>?,
    val bookCover: String?,
    val bookFile: String?,
    @SerializedName("cover_file")
    val coverFile: String?,
    val description: String?,
    @SerializedName("epub_file")
    val epubFile: String?,
    val genre: List<String>?,
    val id: Int?,
    val title: String?,
    @SerializedName("page_count")
    val pageCount: Int?,
    @SerializedName("last_page")
    val lastPage: String?,
    val page_count_progress:Int
):Parcelable