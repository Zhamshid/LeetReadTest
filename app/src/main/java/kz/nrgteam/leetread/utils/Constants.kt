package kz.nrgteam.leetread.utils

object Constants {

    const val BASE_URL = "ABO_BASE_URL"

    const val USERS_ONLINE_LIST_HOR_MARGIN = 8
    const val USERS_ONLINE_LIST_ITEM_SIZE = 50
    const val USERS_EX_WINNERS_LIST_HOR_MARGIN = 5
    const val USERS_EX_WINNERS_LIST_ITEM_SIZE = 35

    const val DAILY_NUMBER = 1
    const val WEEKLY_NUMBER = 2
    const val MONTHLY_NUMBER = 3

    const val GOLD_RANK = 1
    const val SILVER_RANK = 2
    const val BRONZE_RANK = 3

    const val marginBetweenCards = 16f
    const val leftPadding = 0f
    const val cardCountOnLastOpenedCategory = 3f
    const val cardCountCategory = 3f

    //Paging-News
    const val STARTING_PAGE_INDEX = 1
    const val NEWS_NETWORK_PAGE_SIZE: Int = 10
    const val SEARCH_NETWORK_PAGE_SIZE: Int = 10

    val monthsInKazakh = mapOf("January" to "Қаңтар", "February" to "Ақпан", "March" to "Наурыз",
        "April" to "Сәуір", "May" to  "Мамыр", "June" to "Маусым", "July" to "Шілде", "August" to "Тамыз",
        "September" to "Қыркүйек", "October" to "Қазан", "November" to "Қараша", "December" to "Желтоқсан")
    val weekNamesInKazakh = mapOf("Monday" to "Дүйсенбі", "Tuesday" to "Сейсенбі", "Wednesday" to "Сәрсенбі",
        "Thursday" to "Бейсенбі", "Friday" to  "Жұма", "Saturday" to "Сенбі", "Sunday" to "Жексенбі")
}