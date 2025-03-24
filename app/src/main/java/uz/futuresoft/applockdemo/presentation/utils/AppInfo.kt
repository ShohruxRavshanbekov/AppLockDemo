package uz.futuresoft.applockdemo.presentation.utils

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