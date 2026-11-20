package com.example.japim.ui.components.menu.contentMenu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.apollographql.apollo.api.label
import com.example.japim.navigation.Screen
import com.example.japim.ui.theme.FIFTH
import com.example.japim.ui.theme.FOURTH
import com.example.japim.ui.theme.MPlus2


@Composable
private fun generateButton (title : String, onClick : ()-> Unit) : Unit{
    Button(
        modifier = Modifier.fillMaxWidth(.7f).padding(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = FOURTH
        ),
        shape = RoundedCornerShape(10.dp),
        onClick = onClick,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 10.dp
        ),
    ){
        Text(title, color=FIFTH, fontFamily = MPlus2, fontSize = 20.sp);
    }
}
@Composable
fun GenerateContentMenu(
    onNavigateToEvents : ()-> Unit
){

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        generateButton("Meus Certificados", {
//            navController.navigate(Screen.)
        });
        generateButton("Eventos que Participo",{

        });
        generateButton("Eventos") {
            onNavigateToEvents()
        };
        generateButton("Validar Certificado",{

        });
        generateButton("Página Admin",{

        });
        generateButton("Sair da Conta",{

        });

    }
}