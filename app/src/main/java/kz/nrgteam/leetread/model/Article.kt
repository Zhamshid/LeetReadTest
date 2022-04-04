package kz.nrgteam.leetread.model

data class Article(
    var image: String?,
    var title: String,
    var text: String,
    val id: Int? = null
)