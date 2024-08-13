package com.example.notificationadmin

import android.util.JsonToken
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class NotificationData(val title: String,val message: String)
data class UserNotification(var token: String="",var title: String="",var message: String="")
class NotificationViewModel:ViewModel() {
    private val _messageData = MutableStateFlow(NotificationData("",""))
    val messageData:StateFlow<NotificationData> = _messageData

    private val _userMessage = MutableStateFlow(UserNotification())
    val userMessage:StateFlow<UserNotification> =_userMessage
    private fun setTitle(title:String){
        _messageData.update {
            it.copy(
                title = title
            )
        }

    }

    private fun setMessage(message:String){
        _messageData.update {
            it.copy(
                message = message
            )
        }
    }
    private fun setTitleForUser(title:String){
        _userMessage.update {
            it.copy(
                title = title
            )
        }

    }

    private fun setMessageForUser(message:String){
        _userMessage.update {
            it.copy(
                message = message
            )
        }
    }
    private fun setToken(token: String){
        _userMessage.update {
            it.copy(
                token = token
            )
        }
    }
    private fun sendNotification(){
        try {
            viewModelScope.launch {
                MyApp.appModel.firebaseMessagingService.sendBroadCastNotification("New_IPO_Info",messageData.value.title, message = messageData.value.message)
            }

        }catch (e:Exception){

            Log.d("Send Notification", "sendMessage: ${e.message}")
        }
    }
   private fun sendToUser(){
       try {
           viewModelScope.launch {
               MyApp.appModel.firebaseMessagingService.sendToDevice(userMessage.value.token,userMessage.value.title, message = userMessage.value.message)
           }

       }catch (e:Exception){

           Log.d("Send Notification", "sendMessage: ${e.message}")
       }
   }

    fun onEvent(event:NotificationEvent){
        when(event){
            NotificationEvent.SendNotification -> {
                sendNotification()
            }
            is NotificationEvent.SetMessage -> {
                setMessage(event.message)
            }
            is NotificationEvent.SetTitle -> {
                setTitle(event.title)
            }

            NotificationEvent.SendNotificationToUser -> {
                sendToUser()
            }
            is NotificationEvent.SetMessageForUser -> {
                setMessageForUser(event.message)
            }
            is NotificationEvent.SetTitleForUser -> {
                setTitleForUser(event.title)
            }

            is NotificationEvent.SetTokenForUser -> {
                setToken(event.token)
            }
        }
    }


}
sealed interface NotificationEvent{
    data class SetTitle(val title: String):NotificationEvent
    data class SetMessage(val message:String):NotificationEvent
    data object SendNotification:NotificationEvent
    data class SetTitleForUser(val title: String):NotificationEvent
    data class SetMessageForUser(val message:String):NotificationEvent
    data class SetTokenForUser(val token:String):NotificationEvent
    data object SendNotificationToUser:NotificationEvent
}

