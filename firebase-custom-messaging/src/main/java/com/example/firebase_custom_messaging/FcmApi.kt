package com.example.firebase_custom_messaging

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface FcmApi {
    @POST("/v1/projects/{projectId}/messages:send")
    suspend fun broadcast(
        @Path("projectId") projectId:String,
        @Header("Authorization") authHeader: String,
        @Body data: NotificationBody
    )
    @POST("/v1/project/{projectId}/message:send")
    suspend fun sendTo(
        @Path("projectId") projectId:String,
        @Header("Authorization") authHeader: String,
        @Body data: UserNotificationBody
    )
}