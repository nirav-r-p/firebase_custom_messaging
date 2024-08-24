package com.example.firebase_custom_messaging

import android.util.Log


data class NotificationData(
    val title: String,
    val message: String,
    val android: AndroidConfig? = null
)


data class NotificationBody(
    val message: Message,
)

data class UserNotificationBody(
    val message: UserMessage
)

data class Message(
    val topic: String,
    val notification: Notification,
    val android: AndroidConfig? = null,
)

data class AndroidBody(
    val image: String,
)

data class AndroidConfig(
    val notification: AndroidBody,
)


data class UserMessage(
    val token: String,
    val notification: Notification,
)

data class Notification(
    val title: String,
    val body: String,
)

fun NotificationData.toBroadCastNotification(subscribeTopic: String): NotificationBody {
    Log.d("Image", "toBroadCastNotification: ${this.android?.notification?.image.toString()}")
    return NotificationBody(
        message = Message(
            topic = subscribeTopic,
            notification = Notification(
                title = this.title,
                body = this.message,
            ),
            android = this.android
        )
    )
}


fun NotificationData.toTargetDeviceNotification(token: String): UserNotificationBody {
    return UserNotificationBody(
        message = UserMessage(
            token = token,
            notification = Notification(
                this.title,
                this.message
            ),
        )
    )
}