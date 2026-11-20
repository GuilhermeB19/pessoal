package com.example.japim.ui.components

import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.japim.R
import com.example.japim.ui.components.menu.GenerateMenu
import com.example.japim.ui.components.menu.contentMenu.GenerateContentMenu
import com.example.japim.ui.theme.FOURTH
import com.example.japim.ui.theme.PRIMARY
import com.example.japim.ui.theme.SECONDARY
import com.example.japim.ui.theme.SUCCESS
import com.example.japim.ui.theme.THIRD

@Composable
fun GenerateHeader(
    onNavigateToEvents : ()-> Unit,
    contentPage : @Composable (PaddingValues)  -> Unit
){
    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                color = SECONDARY,
                shadowElevation = 4.dp
            ) {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(
                            id = R.drawable.logo_japim_branco
                        ),
                        contentDescription = "Logo do Japim",
                        modifier = Modifier.size(50.dp)
                    )

                    Button(
                        onClick = {},
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 10.dp
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = THIRD
                        ),
                        shape = RoundedCornerShape(5.dp)
                    ){
                        Text("Eventos")
                    }

                    Button(
                        onClick = {},
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 10.dp
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FOURTH
                        ),
                        shape = RoundedCornerShape(5.dp)
                    ){
                        Text("Comunidade", color= PRIMARY)
                    }

                    GenerateMenu { closeMenu ->
                        GenerateContentMenu(
                            onNavigateToEvents
                        );
                    }
                }
            }
        }
    ) { paddingInterno ->
        contentPage(paddingInterno);
    }
}
