package kz.nrgteam.leetread.dto.kitaphana

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Kitap(
     val id: Int? = 0,
     val name: String? = "",
     val bookImage: String? = "",
     val authors: List<String> = arrayListOf(),
     val genres: List<String> = arrayListOf(),
     val comments: List<Comment> = arrayListOf()
) : Parcelable


@Parcelize
data class Comment(
     val id: String,
     val context: String
): Parcelable

@Parcelize
data class Genre1(
     val id: String,
     val context: String
): Parcelable


@Parcelize
data class Author(
     val id: String,
     val first_name: String,
     val last_name: String,
     val books: List<Kitap>
): Parcelable