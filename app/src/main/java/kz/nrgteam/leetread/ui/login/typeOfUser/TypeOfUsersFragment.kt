package kz.nrgteam.leetread.ui.login.typeOfUser

import kz.nrgteam.leetread.R
import kz.nrgteam.leetread.data.prefs.Prefs
import kz.nrgteam.leetread.databinding.FragmentTypeOfUsersBinding
import kz.nrgteam.leetread.utils.Config.BASE_URL_STUDENTS
import kz.nrgteam.leetread.utils.Config.BASE_URL_TEACHERS
import kz.nrgteam.leetread.utils.Config.BASE_URL_TUTOR
import kz.nrgteam.leetread.utils.base.BaseBindingFragment
import kz.nrgteam.leetread.utils.navigate
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TypeOfUsersFragment :
    BaseBindingFragment<FragmentTypeOfUsersBinding>(R.layout.fragment_type_of_users),
    TypeOfUserAdapter.TypeUserAdapterListener {

    @Inject
    lateinit var prefs: Prefs

    private lateinit var typeOfUserAdapter: TypeOfUserAdapter
    private val types = listOf(
        TypeOfUser("Оқушылар", false),
        TypeOfUser("Мұғалімдер", false),
        TypeOfUser("Басқа", false)
    )

    override fun initViews(savedInstanceState: Bundle?) {
        typeOfUserAdapter = TypeOfUserAdapter(this).apply {
            items = types
        }
        binding.run {
            userTypesRv.adapter = typeOfUserAdapter
            create.run {
                title.text = "Жалғастыру"
                title.setTextColor(getClr(R.color.white))
                root.setCardBackgroundColor(getClr(R.color.primaryColor))
                root.setOnClickListener { navigateToLogin() }
            }
        }
    }

    private fun navigateToLogin() {
        val baseUrl = when (types[typeOfUserAdapter.selectedItemPos].title) {
            "Мұғалімдер" -> BASE_URL_TEACHERS
            "Басқа" -> BASE_URL_TUTOR
            else -> BASE_URL_STUDENTS
        }
        prefs.setBaseUrl(baseUrl)
        val n = TypeOfUsersFragmentDirections.actionTypeOfUsersFragmentToLoginFragment()
        navigate(n)
    }

    override fun onClickType(pos: Int) {
        types[pos].isSelected = !types[pos].isSelected
    }
}

data class TypeOfUser(val title: String, var isSelected: Boolean)