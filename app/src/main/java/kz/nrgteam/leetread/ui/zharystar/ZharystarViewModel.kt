package kz.nrgteam.leetread.ui.zharystar

import kz.nrgteam.leetread.model.UserX
import kz.nrgteam.leetread.utils.base.BaseViewModel
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ZharystarViewModel @Inject constructor(
//    baseCloudRepository: BaseCloudRepository
) :
    BaseViewModel() {

    val usersDaily = MutableLiveData(ArrayList<UserX>())
    val usersWeekly = MutableLiveData(ArrayList<UserX>())
    val usersMonthly = MutableLiveData(ArrayList<UserX>())

    init {
        getDailyRatingUsers()
        getWeeklyRatingUsers()
        getMonthlyRatingUsers()
    }

    fun stories(): MutableList<String> {
        return mutableListOf(
            "https://images.unsplash.com/photo-1527203561188-dae1bc1a417f?ixid=" +
                    "MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80",
            "https://images.unsplash.com/photo-1527203561188-dae1bc1a417f?ixid=" +
                    "MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80",
            "https://images.unsplash.com/photo-1527203561188-dae1bc1a417f?ixid=" +
                    "MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80"
        )
    }

    private fun getDailyRatingUsers() {
        launchIO {
            //there must be api service request
            val userArray = ArrayList<UserX>()
            userArray.add(
                UserX(
                    "0",
                    "https://images.unsplash.com/photo-1527203561188-dae1bc1a417f?ixid=" +
                            "MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80",
                    "sad"
                )
            )
            userArray.add(
                UserX(
                    "3",
                    "https://images.unsplash.com/photo-1527203561188-dae1bc1a417f?ixid=" +
                            "MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80",
                    "sad"
                )
            )
            userArray.add(
                UserX(
                    "3",
                    "https://images.unsplash.com/photo-1527203561188-dae1bc1a417f?ixid=" +
                            "MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80",
                    "sad"
                )
            )
            userArray.add(
                UserX(
                    "3",
                    "https://images.unsplash.com/photo-1527203561188-dae1bc1a417f?ixid=" +
                    "MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80",
                    "sad"
                )
            )
            userArray.add(
                UserX(
                    "2",
                    "https://images.unsplash.com/photo-1527203561188-dae1bc1a417f?ixid=" +
                    "MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80",
                    "sad"
                )
            )
            userArray.add(
                UserX(
                    "1",
                    "https://images.unsplash.com/photo-1527203561188-dae1bc1a417f?ixid=" +
                            "MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80",
                    "sad"
                )
            )
            usersDaily.postValue(userArray)

        }
    }


    private fun getWeeklyRatingUsers() {
        launchIO {
            //there must be api service request
            val userArray = ArrayList<UserX>()
            userArray.add(
                UserX(
                    "0",
                    "https://images.unsplash.com/photo-1527203561188-dae1bc1a417f?ixid=" +
                            "MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80",
                    "sad"
                )
            )
            userArray.add(
                UserX(
                    "1",
                    "https://images.unsplash.com/photo-1527203561188-dae1bc1a417f?ixid=" +
                            "MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80",
                    "sad"
                )
            )
//            user_array.add(User(2,"https://s3-alpha-sig.figma.com/img/2da7/e21e/d2aab1e88e4e95690edab083cb547dda?Expires=1633305600&Signature=PVQ-id5QaEjn23Lqbm-~oN~QqC6Y5Yn5Q2xQTd3E~vZB5OjYP803dO25whnemjU5lm7t~U7spd6ghEPVKj9DYDbvF~9VheecVzhvKqURXUqnNJHAY9SuGsX7Xd3xOOkAO2D7eNho6RkyKYbT-t4JChqTsbvUrniGKJbEw1jlIlBiN6RUvJ64kwIj9SFBXOKlcj-oOCaCN1sgAoTBEG3~7RzfEU-kSao9YCIK2Ad62T6rfQiVTO2xSLmXdIqz1HcFot1wPn~tMmgiFcpFkHcTjn5LC4ZYWmD1I~5YssH3HMP9A8yizKNDntayAB9W7PH1iWy0NH6c9g~UNS~XP~z3Xw__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA","sad"))
//            user_array.add(User(3,"https://s3-alpha-sig.figma.com/img/2da7/e21e/d2aab1e88e4e95690edab083cb547dda?Expires=1633305600&Signature=PVQ-id5QaEjn23Lqbm-~oN~QqC6Y5Yn5Q2xQTd3E~vZB5OjYP803dO25whnemjU5lm7t~U7spd6ghEPVKj9DYDbvF~9VheecVzhvKqURXUqnNJHAY9SuGsX7Xd3xOOkAO2D7eNho6RkyKYbT-t4JChqTsbvUrniGKJbEw1jlIlBiN6RUvJ64kwIj9SFBXOKlcj-oOCaCN1sgAoTBEG3~7RzfEU-kSao9YCIK2Ad62T6rfQiVTO2xSLmXdIqz1HcFot1wPn~tMmgiFcpFkHcTjn5LC4ZYWmD1I~5YssH3HMP9A8yizKNDntayAB9W7PH1iWy0NH6c9g~UNS~XP~z3Xw__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA","sad"))
//            usersWeekly.postValue(userArray)

        }
    }


    private fun getMonthlyRatingUsers() {
        launchIO {
            //there must be api service request
            val userArray = ArrayList<UserX>()
            userArray.add(
                UserX(
                    "0",
                    "https://images.unsplash.com/photo-1527203561188-dae1bc1a417f?ixid=" +
                            "MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80",
                    "sad"
                )
            )
            userArray.add(
                UserX(
                    "1",
                    "https://images.unsplash.com/photo-1527203561188-dae1bc1a417f?ixid=" +
                            "MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80",
                    "sad"
                )
            )
//            user_array.add(User(2,"https://s3-alpha-sig.figma.com/img/2da7/e21e/d2aab1e88e4e95690edab083cb547dda?Expires=1633305600&Signature=PVQ-id5QaEjn23Lqbm-~oN~QqC6Y5Yn5Q2xQTd3E~vZB5OjYP803dO25whnemjU5lm7t~U7spd6ghEPVKj9DYDbvF~9VheecVzhvKqURXUqnNJHAY9SuGsX7Xd3xOOkAO2D7eNho6RkyKYbT-t4JChqTsbvUrniGKJbEw1jlIlBiN6RUvJ64kwIj9SFBXOKlcj-oOCaCN1sgAoTBEG3~7RzfEU-kSao9YCIK2Ad62T6rfQiVTO2xSLmXdIqz1HcFot1wPn~tMmgiFcpFkHcTjn5LC4ZYWmD1I~5YssH3HMP9A8yizKNDntayAB9W7PH1iWy0NH6c9g~UNS~XP~z3Xw__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA","sad"))
//            user_array.add(User(3,"https://s3-alpha-sig.figma.com/img/2da7/e21e/d2aab1e88e4e95690edab083cb547dda?Expires=1633305600&Signature=PVQ-id5QaEjn23Lqbm-~oN~QqC6Y5Yn5Q2xQTd3E~vZB5OjYP803dO25whnemjU5lm7t~U7spd6ghEPVKj9DYDbvF~9VheecVzhvKqURXUqnNJHAY9SuGsX7Xd3xOOkAO2D7eNho6RkyKYbT-t4JChqTsbvUrniGKJbEw1jlIlBiN6RUvJ64kwIj9SFBXOKlcj-oOCaCN1sgAoTBEG3~7RzfEU-kSao9YCIK2Ad62T6rfQiVTO2xSLmXdIqz1HcFot1wPn~tMmgiFcpFkHcTjn5LC4ZYWmD1I~5YssH3HMP9A8yizKNDntayAB9W7PH1iWy0NH6c9g~UNS~XP~z3Xw__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA","sad"))
            usersMonthly.postValue(userArray)
        }
    }
}