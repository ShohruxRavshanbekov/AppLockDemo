package uz.futuresoft.applockdemo.presentation.view_model

import android.content.Context

sealed interface SharedAction {
    data class GetApps(val context: Context) : SharedAction
    data class OnChangeAppBlockedStatus(val status: Boolean, val packageName: String) : SharedAction
    data class GetApp(val context: Context, val packageName: String?) : SharedAction
}