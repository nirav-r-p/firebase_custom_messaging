# Firebase Custom Messaging Library

## Overview

This repository contains a custom Android library for handling Firebase Cloud Messaging (FCM). This library provides a streamlined way to integrate FCM into your Android projects, with added functionalities and easy-to-use methods for sending notifications.

## Features

- **Easy Integration**: Simplifies the process of integrating FCM into your Android app.
- **Customizable**: Allows for easy configuration of FCM parameters and settings.
- **Robust API**: Provides methods to send notifications and manage FCM tokens.

## Installation

To use this library in your Android project, follow these steps:

### 1. Add JitPack Repository

Add JitPack to your project's `build.gradle` file:

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

### 2. Add the dependency in module:app build.gradle file
```groovy
    dependency{
        //add for firebase messaging (this need for my custom package)
        implementation ("com.google.auth:google-auth-library-oauth2-http:1.17.0")
        //For Cloud Messaging Network request
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        //For Json Factory Convert
        implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
        // my custom package for messaging
        implementation("com.github.nirav-r-p:firebase_custom_messaging:1.0.0")
    }
```

### 3. Setup (Admin Side)
 1. Manifest File (Permission)
 ```groovy
 <manifest
    <uses-permission android:name="android.permission.INTERNET"/>
</manifest>

```
   2. Initialize the Library
In your application class or activity, initialize the library with your Firebase project name and the path to your service account JSON file:
```groovy
 class MyApp : Application() {
    companion object{
        lateinit var firebaseMessagingProvider:  FirebaseMessagingService
    }
    override fun onCreate() {
        super.onCreate()

        // Initialize FirebaseNotification with project name and service account
        firebaseMessagingProvider = FirebaseMessagingService.getInstance(
            projectName = "your_project_name",
            serviceAccountJson = assets.open("service_account_key.json")
        )
        firebaseMessagingProvider.initialize(this)
    }
}

```
  Make sure to place your service_account_key.json file in the assets folder of your project.

  3. Send a Notification
```groovy
   class sampleViewModel:ViewModel(){
         fun sendNotification(){
         // use firebaseMessagingProvider
         // for broadcast notification
          viewModelScope.launch {
              MyApp.firebaseMessagingService.sendBroadCastNotification("<subscribed-topic>","<Title>","<Message>")
           }

        }
   }

```
### 4. Setup (User Side)
   1. Manifest File
 ```groovy
 <manifest
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS"/> <!--notification permission-->
</manifest>

```
   2. Add the dependency in module:app build.gradle file
```groovy
    dependency{
        //For Firebase Massaging
        implementation("com.google.firebase:firebase-messaging")
    }
```
   3. Create Class for firebase Messaging
```groovy
    class NotificationService:FirebaseMessagingService() {

       override fun onNewToken(token: String) {
           super.onNewToken(token)
            //If we want to save this token to database --> not necessary
       }

       override fun onMessageReceived(message: RemoteMessage) {
          super.onMessageReceived(message)
          //If we want to handle notification data --> if required
       }
}
```

  4. Initialize with subscribed top
  ```groovy
    class SampleViewModel:ViewModel(){
        init {
         viewModelScope.launch {
           Log.d("subscribeToNewTopic", ": Done")
           Firebase.messaging.subscribeToTopic("<subscribed-topic>").await()
          }
       }
    }
  ```

make sure that in admin side and user side subscribed top are equle.


### 5 Project Structure

```groovy
 MyProject/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── assets/
│   │   │   │   └── service_account_key.json
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── example/
│   │   │   │           └── myapp/
│   │   │   │               └── MyApp.kt
│   │   │   └── res/
│   │   └── build.gradle
│   └── build.gradle
├── build.gradle
└── settings.gradle


```


## Contributing
Contributions are welcome! Please submit issues or pull requests on GitHub to help improve the library.
### Not forget to star this repositorie 🌟🌟🌟
