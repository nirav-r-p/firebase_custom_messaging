package com.example.firebase_custom_messaging

import android.content.Context
import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.io.InputStream
import java.net.URL

final class FirebaseCustomMessaging private constructor(
    private val projectName: String,
    private val serviceAccountJson: InputStream
) {
    private  var sharedPreferences:StoragePreference?=null
    companion object {
        private var instance:FirebaseCustomMessaging?=null
        private val api: FcmApi = Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
        fun getInstance(
            projectName: String,
            serviceAccountJson: InputStream
        ): FirebaseCustomMessaging {
            if (instance != null) return instance as FirebaseCustomMessaging
            instance= FirebaseCustomMessaging(projectName, serviceAccountJson)
            return instance as FirebaseCustomMessaging
        }
    }

    fun initialize(context: Context){
        sharedPreferences= StoragePreference(context)
    }
    @Throws(HttpException::class,NullPointerException::class)
    suspend fun sendBroadCastNotification(topic: String,title:String,message:String,androidConfig: AndroidConfig?=null){
        if (sharedPreferences==null) throw NullPointerException("First Initialize  FirebaseMessagingService ")
        val authToken=TokenProvider.getAccessToken(instance!!.serviceAccountJson,sharedPreferences!!)
        try {
            instance?.let {
                api.broadcast(
                    projectId = instance!!.projectName,
                    authHeader = authToken,
                    NotificationData(title, message, androidConfig).toBroadCastNotification(topic)
                )
            }
        }catch (e:Exception) {
           throw e
        }
    }

    @Throws(NullPointerException::class,retrofit2.HttpException::class)
   suspend fun sendToDevice(token:String,title: String,message: String){
       if (sharedPreferences==null) throw NullPointerException("First Initialize  FirebaseMessagingService ")
       val authToken=TokenProvider.getAccessToken(instance!!.serviceAccountJson,sharedPreferences!!)

            instance?.let {
                api.sendTo(projectId = instance!!.projectName , authHeader = authToken,NotificationData(title, message).toTargetDeviceNotification(token) )
            }


   }

}