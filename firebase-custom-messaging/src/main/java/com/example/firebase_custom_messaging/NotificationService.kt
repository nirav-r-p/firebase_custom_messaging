package com.example.firebase_custom_messaging

import android.content.Context
import android.net.http.HttpException
import com.example.firebase_custom_messaging.FirebaseMessagingService.Companion.api
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.io.InputStream

final class FirebaseMessagingService private constructor(
    private val projectName: String,
    private val serviceAccountJson: InputStream
) {
    private  var sharedPreferences:StoragePreference?=null
    companion object {
        private var instance:FirebaseMessagingService?=null
        private val api: FcmApi = Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
        fun getInstance(
            projectName: String,
            serviceAccountJson: InputStream
        ): FirebaseMessagingService {
            if (instance != null) return instance as FirebaseMessagingService
            instance= FirebaseMessagingService(projectName, serviceAccountJson)
            return instance as FirebaseMessagingService
        }
    }

    fun initialize(context: Context){
        sharedPreferences= StoragePreference(context)
    }
    @Throws(HttpException::class,NullPointerException::class)
    suspend fun sendBroadCastNotification(topic: String,title:String,message:String){
        if (sharedPreferences==null) throw NullPointerException("First Initialize  FirebaseMessagingService ")
        val authToken=TokenProvider.getAccessToken(instance!!.serviceAccountJson,sharedPreferences!!)
        instance?.let {
            api.broadcast(projectId = instance!!.projectName , authHeader = authToken,NotificationData(title, message).toBroadCastNotification(topic) )
        }
    }
    @Throws(HttpException::class,NullPointerException::class)
   suspend fun sendToDevice(token:String,title: String,message: String){
       if (sharedPreferences==null) throw NullPointerException("First Initialize  FirebaseMessagingService ")
       val authToken=TokenProvider.getAccessToken(instance!!.serviceAccountJson,sharedPreferences!!)
       instance?.let {
           api.sendTo(projectId = instance!!.projectName , authHeader = authToken,NotificationData(title, message).toTargetDeviceNotification(token) )
       }
   }

}