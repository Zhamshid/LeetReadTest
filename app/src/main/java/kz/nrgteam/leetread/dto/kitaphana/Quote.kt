package kz.nrgteam.leetread.dto.kitaphana

data class Quote(
    val book_id: Int,
    val cfi: String,
    val date: String,
    val highlighted_text: String,
    val id: Int,
    val page_count:Int,
    val cover_file: String,
    val title: String,
    val epub_file:String,
    val note: String,
    val user_id: Int,
    val page_count_progress:Int?
)