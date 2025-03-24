package uz.futuresoft.applockdemo.presentation.screens.apps.utils

data class AppInfo(
    val name: String,
    val packageName: String,
    val locked: Boolean,
) {
    companion object {
        val INITIAL = AppInfo(
            name = "",
            packageName = "",
            locked = false,
        )
    }
}