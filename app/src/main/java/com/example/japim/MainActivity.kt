
package com.example.japim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.japim.apolloClient.TokenManager
import com.example.japim.ui.theme.JAPIMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            JAPIMTheme {
                AppNavHost(TokenManager(this))
            }
        }
    }
}
