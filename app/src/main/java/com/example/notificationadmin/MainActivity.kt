package com.example.notificationadmin

import android.os.Bundle
import androidx.activity.ComponentActivity

import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.notificationadmin.ui.theme.NotificationAdminTheme


class MainActivity : ComponentActivity() {

    private val viewModel:NotificationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            NotificationAdminTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        SendBox(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun ColumnScope.SendBox(viewModel: NotificationViewModel) {
   val data by viewModel.messageData.collectAsState()
    val userdata by viewModel.userMessage.collectAsState()
    Text(text = "For Broad Cast ")
    TextField(value = data.title, onValueChange ={viewModel.onEvent(NotificationEvent.SetTitle(it))}, label = {
        Text(text = "Title")
    })

    TextField(value = data.message, onValueChange ={viewModel.onEvent(NotificationEvent.SetMessage(it))} , label = {
        Text(text = "Message")
    })
    Box(Modifier.height(12.dp))
    ElevatedButton(onClick = { 
        if(data.title!="" && data.message!=""){
            viewModel.onEvent(NotificationEvent.SendNotification)
        }
    }) {
        Text(text = "Broadcast Message")
    }
    Text(text = "For User ")
    Box(Modifier.height(12.dp))
    TextField(value = userdata.token, onValueChange ={viewModel.onEvent(NotificationEvent.SetTokenForUser(it))}, label = { Text(
        text = "Token"
    )})
    TextField(value = userdata.title, onValueChange ={viewModel.onEvent(NotificationEvent.SetTitleForUser(it))}, label = {
        Text(text = "Title")
    } )
    TextField(value = userdata.message, onValueChange ={viewModel.onEvent(NotificationEvent.SetMessageForUser(it))} , label = {
        Text(text = "Message")
    })
    Box(Modifier.height(12.dp))
    ElevatedButton(onClick = {
        if(userdata.title!="" && userdata.message!="" && userdata.token!=""){
            viewModel.onEvent(NotificationEvent.SendNotificationToUser)
        }
    }) {
        Text(text = "SendMessage to User")
    }
    
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NotificationAdminTheme {

    }
}