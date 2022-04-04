package kz.nrgteam.leetread.dto.kitaphana

import kz.nrgteam.leetread.ui.kitaphana.adapters.BookVHUI

data class ParentBookItem(
    val books: List<BookVHUI>,
    val genre: BookVHUI
)