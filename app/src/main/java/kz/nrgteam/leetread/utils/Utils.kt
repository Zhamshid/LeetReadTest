package kz.nrgteam.leetread.utils

import android.content.res.Resources
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import java.util.*

fun Number.px(): Int {
    return (this.toFloat() * Resources.getSystem().displayMetrics.density).toInt()
}
fun Number.dp(): Int{
    return (this.toInt() / Resources.getSystem().displayMetrics.density).toInt()
}
fun Fragment.navigate(uri: Uri) {
    val controller = findNavController()
    val currentDestination =
        (controller.currentDestination as? FragmentNavigator.Destination)?.className
            ?: (controller.currentDestination as? DialogFragmentNavigator.Destination)?.className
    if (currentDestination == this.javaClass.name) {
        controller.navigate(uri)
    }
}
fun Fragment.navigate(directions: NavDirections) {
    val controller = findNavController()
    val currentDestination =
        (controller.currentDestination as? FragmentNavigator.Destination)?.className
            ?: (controller.currentDestination as? DialogFragmentNavigator.Destination)?.className
    if (currentDestination == this.javaClass.name) {
        controller.navigate(directions)
    }
}

fun Long.getSpentPublishedTime(): String {
    var conversionTime = ""
    val diff = (Calendar.getInstance().timeInMillis - this) / 1000
    val diffYears = diff / (365 * 24 * 60 * 60)
    val diffDays = diff / (24 * 60 * 60)
    val diffHours = diff / (60 * 60)
    val diffMin = diff / (60)

    when {
        diffYears > 1 -> {
            conversionTime += "$diffYears жыл "
        }
        diffDays > 0 -> {
            conversionTime += "$diffDays күн "
        }
        diffHours > 0 -> {
            conversionTime += (diffHours - diffDays * 24).toString() + " сағат "
        }
        diffMin > 0 -> {
            conversionTime += (diffMin - diffHours * 60).toString() + " минут "
        }
        diff >= 0 -> {
            conversionTime += (diff - diffMin * 60).toString() + " секунд "
        }
    }
    if (conversionTime != "") {
        conversionTime += "бұрын"
    }
    return conversionTime

}
