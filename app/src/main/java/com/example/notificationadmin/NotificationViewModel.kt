package com.example.notificationadmin


import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase_custom_messaging.AndroidBody
import com.example.firebase_custom_messaging.AndroidConfig

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.NullPointerException


data class NotificationData(val title: String, val message: String, val image: String = "")
data class UserNotification(
    var token: String = "",
    var title: String = "",
    var message: String = ""
)

class NotificationViewModel : ViewModel() {
    private val _messageData = MutableStateFlow(NotificationData("", ""))
    val messageData: StateFlow<NotificationData> = _messageData

    private val _userMessage = MutableStateFlow(UserNotification())
    val userMessage: StateFlow<UserNotification> = _userMessage

    fun setMessage(event: SetFiledEvent) {
        when (event) {
            is SetFiledEvent.SetMessage -> {
                _messageData.update {
                    it.copy(
                        message = event.message
                    )
                }
            }

            is SetFiledEvent.SetMessageForUser -> {
                _userMessage.update {
                    it.copy(
                        message = event.message
                    )
                }
            }

            is SetFiledEvent.SetTitle -> {
                _messageData.update {
                    it.copy(
                        title = event.title
                    )
                }
            }

            is SetFiledEvent.SetTitleForUser -> {
                _userMessage.update {
                    it.copy(
                        title = event.title
                    )
                }
            }

            is SetFiledEvent.SetTokenForUser -> {
                _userMessage.update {
                    it.copy(
                        token = event.token
                    )
                }
            }

            is SetFiledEvent.SetImageUrl -> {
                _messageData.update {
                    it.copy(
                        image = event.url
                    )
                }
            }
        }
    }

    private fun sendNotification() {
        try {
            viewModelScope.launch {
                MyApp.appModel.firebaseMessagingService.sendBroadCastNotification(
                    topic =  "New_IPO_Info",
                    title = messageData.value.title,
                    message = messageData.value.message,
                    androidConfig = AndroidConfig(notification = AndroidBody(image = messageData.value.image))
                )
            }

        } catch (e: Exception) {

            Log.d("Send Notification", "sendMessage: ${e.message}")
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun sendToUser() {
        Log.d("Send", "sendToUser: ${userMessage.value}")
        try {
            viewModelScope.launch {
                MyApp.appModel.firebaseMessagingService.sendToDevice(
                    userMessage.value.token,
                    userMessage.value.title,
                    message = userMessage.value.message
                )
            }

        } catch (e: HttpException ) {

            Log.d("Send Notification", "sendMessage: ${e.message}")
        }catch (e:NullPointerException){
            Log.d("Send Notification", "sendMessage: ${e.message}")
        }
    }

    fun setEvent(event: NotificationEvent) {
        when (event) {
            NotificationEvent.SendNotification -> sendNotification()
            NotificationEvent.SendNotificationToUser -> sendToUser()
        }
    }


}

sealed interface NotificationEvent {
    data object SendNotification : NotificationEvent
    data object SendNotificationToUser : NotificationEvent
}

sealed interface SetFiledEvent {
    data class SetTitleForUser(val title: String) : SetFiledEvent
    data class SetMessageForUser(val message: String) : SetFiledEvent
    data class SetTokenForUser(val token: String) : SetFiledEvent
    data class SetTitle(val title: String) : SetFiledEvent
    data class SetMessage(val message: String) : SetFiledEvent
    data class SetImageUrl(val url: String) : SetFiledEvent

}

