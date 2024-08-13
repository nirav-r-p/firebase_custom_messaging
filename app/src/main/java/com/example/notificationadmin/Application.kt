package com.example.notificationadmin

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.firebase_custom_messaging.FirebaseMessagingService
import java.io.FileNotFoundException
import java.io.InputStream

class MyApp : Application() {
    companion object {
        lateinit var appModel: AppModel
    }

    override fun onCreate() {
        super.onCreate()
        appModel = AppModuleImp(this)
        appModel.firebaseMessagingService.initialize(this)
    }
}



interface AppModel {
    val serviceFile: InputStream
    val firebaseMessagingService: FirebaseMessagingService

}

//class StoragePreference(
//    private val context: Context
//) {
//    fun getToken(): String? {
//        return SharedPreferenceManager.getToken(context)
//    }
//
//    fun saveToken(token: String) {
//        SharedPreferenceManager.saveToken(context, token)
//    }
//
//    fun getExpiryTokenTime(): Long {
//        return SharedPreferenceManager.getExpiryTime(context)
//    }
//
//    fun saveExpiryTokenTime(expiryTime: Long) {
//        SharedPreferenceManager.saveExpiryTime(context, expiryTime)
//    }
//}
//
//object SharedPreferenceManager : SharedPreferenceOperation {
//    private const val PREF_NAME = "fcm_key"
//    private const val KEY_TOKEN = "key_token"
//    private const val KEY_EXPIRY = "key_expiry"
//
//    private fun getPreference(context: Context): SharedPreferences {
//        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//    }
//
//    override fun saveToken(context: Context, token: String) {
//        val editor = getPreference(context).edit()
//        editor.putString(KEY_TOKEN, token)
//        editor.apply()
//    }
//
//    override fun getToken(context: Context): String? {
//        return getPreference(context).getString(KEY_TOKEN, null)
//    }
//
//    override fun saveExpiryTime(context: Context, time: Long) {
//        val editor = getPreference(context).edit()
//        editor.putLong(KEY_EXPIRY, time)
//        editor.apply()
//    }
//
//    override fun getExpiryTime(context: Context): Long {
//        return getPreference(context).getLong(KEY_EXPIRY, 0L)
//    }
//
//}

class AppModuleImp(
    private val appContext: Context
) : AppModel {
    override val serviceFile: InputStream by
    lazy {
        appContext.assets.open("service_account_key.json")
    }
    override val firebaseMessagingService: FirebaseMessagingService
        by lazy {
            FirebaseMessagingService.getInstance("notification-example-21b1d", serviceAccountJson = MyApp.appModel.serviceFile)
        }

}


