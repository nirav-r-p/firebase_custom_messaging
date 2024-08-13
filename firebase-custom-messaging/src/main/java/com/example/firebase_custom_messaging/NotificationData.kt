package com.example.firebase_custom_messaging

data class NotificationData(
    val title:String,
    val message:String
)
data class NotificationBody(
    val message: Message  ,
)
data class UserNotificationBody(
    val message: UserMessage
)
data class Message(
    val topic: String,
     val notification: Notification
)
data class UserMessage(
    val token: String,
     val notification: Notification
)

data class Notification(
    val title: String,
    val body: String,
)
fun NotificationData.toBroadCastNotification(subscribeTopic:String):NotificationBody{
    return NotificationBody(
        message = Message(
            topic = subscribeTopic,
            notification = Notification(
                title = this.title,
                body = this.message
            )
        )
    )
}



fun NotificationData.toTargetDeviceNotification(token: String):UserNotificationBody{
    return UserNotificationBody(
        message = UserMessage(
            token = token,
            notification = Notification(
                this.title,
                this.message
            )
        )
    )
}