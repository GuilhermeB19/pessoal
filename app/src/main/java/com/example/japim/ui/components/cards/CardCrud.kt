package com.example.japim.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.japim.R
import com.example.japim.ui.theme.ERROR
import org.w3c.dom.Text
import com.example.japim.ui.theme.FIFTH
import com.example.japim.ui.theme.FOURTH
import com.example.japim.ui.theme.MPlus2
import com.example.japim.ui.theme.PRIMARY
import com.example.japim.ui.theme.SECONDARY
import com.example.japim.ui.theme.SUCCESS
import com.example.japim.ui.theme.WARNING


@Composable
fun GenerateCardCrud(title : String, text : String, speaker : String, totalParticipants : Int, totalVolunteers : Int, isPayment : Boolean,  onClickUpdate : () -> Unit, onClickDelete : () -> Unit){
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = SECONDARY,
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = PRIMARY,
                fontFamily = MPlus2,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = PRIMARY,
                fontFamily = MPlus2
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = onClickUpdate,
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 10.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WARNING
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Editar", color=PRIMARY)
                }

                Button(
                    onClick = onClickDelete,
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 10.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ERROR
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Deletar",color= FOURTH)
                }
            }


            // info of events for quantity
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Participantes", // Importante para acessibilidade
                        tint = FIFTH
                    )
                    Text(
                        totalParticipants.toString(),
                        fontFamily = MPlus2,
                        fontWeight = FontWeight.Bold,
                        color=FIFTH
                    )


                    Icon(
                        imageVector = Icons.Default.SupportAgent,
                        contentDescription = "Voluntários", // Importante para acessibilidade
                        tint = FIFTH
                    )
                    Text(
                        totalVolunteers.toString(),
                        fontFamily = MPlus2,
                        fontWeight = FontWeight.Bold,
                        color=FIFTH

                    )

                    if(isPayment){
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = "É Pago?",
                            tint = FIFTH
                        )

                        Text(
                            "Pago",
                            fontFamily = MPlus2,
                            fontWeight = FontWeight.Bold,
                            color=FIFTH

                        )
                    }

                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        speaker,
                        fontFamily = MPlus2,
                        fontWeight = FontWeight.Bold,
                        color = FIFTH
                    )
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Palestrante", // Importante para acessibilidade
                        tint = FIFTH
                    )
                }

            }
        }
    }


}