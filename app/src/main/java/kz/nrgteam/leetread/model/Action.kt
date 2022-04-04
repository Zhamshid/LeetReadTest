package kz.nrgteam.leetread.model

import kz.nrgteam.leetread.dto.kitaphana.Kitap

data class Action(
    var userName: String,
    var userImage: String?,
    val userId: Int,
    var published_time: Long,
    var kitap: Kitap?,
    var text: String?,
    var likes: Int = 0,
    val id: Int? = null,
    val secondUserId:Int? = null
)