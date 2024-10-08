package com.example.notificationadmin

import android.app.Application
import android.content.Context
import com.example.firebase_custom_messaging.FirebaseCustomMessaging



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
    val firebaseMessagingService: FirebaseCustomMessaging

}



class AppModuleImp(
    private val appContext: Context
) : AppModel {
    override val serviceFile: InputStream by
    lazy {
        appContext.assets.open("service_account_key.json")
    }
    override val firebaseMessagingService: FirebaseCustomMessaging
        by lazy {
            FirebaseCustomMessaging.getInstance("notification-example-21b1d", serviceAccountJson = MyApp.appModel.serviceFile)
        }

}
///new code


