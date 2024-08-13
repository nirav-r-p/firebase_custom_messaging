package com.example.firebase_custom_messaging

import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

object TokenProvider {
    private const val SCOPE = "https://www.googleapis.com/auth/firebase.messaging" // Scope
    //    private val serviceJsonStream:InputStream? = null  // Access Service Json for getting token
    private var credential: GoogleCredentials? = null
    suspend fun getAccessToken(
        serviceAccountJson: InputStream,
        storagePreference: StoragePreference
    ): String {
        return withContext(Dispatchers.IO) {
            /*
            * In this We manage over generated Token
            *   1. Take token and Expiry time form Shared Preference
            *   2. check that token is not null and not expiry then return that token
            *   3. if it's null or expiry we refresh token and store to shared preference and return generated token
             */

            if (credential == null || storagePreference.getExpiryTokenTime()<System.currentTimeMillis()) {
                credential = GoogleCredentials.fromStream(serviceAccountJson).createScoped(SCOPE)
            }
            val localToken = storagePreference.getToken()
            val expiredTime = storagePreference.getExpiryTokenTime()
            if (localToken != null && expiredTime != 0L && expiredTime > System.currentTimeMillis()) {
                "Bearer $localToken"
            } else {
                Log.d("input", "getAccessToken: $serviceAccountJson")
                credential?.refresh()
                Log.d("Key from google", "getAccessToken: ${credential?.accessToken} ")
                storagePreference.saveToken(credential?.accessToken!!.tokenValue)
                storagePreference.saveExpiryTokenTime(credential?.accessToken!!.expirationTime.time)
                "Bearer ${credential?.accessToken?.tokenValue}"
            }
        }
    }
}