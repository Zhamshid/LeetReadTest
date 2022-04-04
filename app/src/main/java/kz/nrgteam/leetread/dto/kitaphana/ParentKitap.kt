package kz.nrgteam.leetread.dto.kitaphana

data class ParentKitap(var category:String, var kitaptar: List<Kitap>, var isLastOpenedBookCategory: Boolean = false)
