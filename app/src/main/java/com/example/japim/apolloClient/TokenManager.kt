
package com.example.japim.apolloClient
import android.content.Context
import android.util.Log
import androidx.core.content.edit

private val KEY = "token_access"
class TokenManager(context: Context){

    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun getToken() : String? {
       val token = sharedPreferences.getString(KEY, null)
       Log.d("TokenManager", "getToken: $token")
       return token
    }

    fun saveToken(novoToken: String) {
        Log.d("TokenManager", "saveToken: $novoToken")
        sharedPreferences.edit(commit = true) { putString(KEY, novoToken) }
    }

    fun limparToken() {
        Log.d("TokenManager", "limparToken")
        sharedPreferences.edit(commit = true) { putString(KEY, "") }
    }
}
