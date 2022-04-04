package kz.nrgteam.leetread.common

object InternalDeepLink {
    const val DOMAIN = "kz.nrgteam://"

    const val HOME = "${DOMAIN}home"
    const val ZHARYSTAR = "${DOMAIN}zharystar"
    const val KITAPHANA = "${DOMAIN}kitaphana"
    const val RATING = "${DOMAIN}rating"
    const val PROFILE = "${DOMAIN}profile"

    fun makeCustomDeepLink(id: String): String {
        return "${DOMAIN}customDeepLink?id=${id}"
    }
}