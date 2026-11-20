package com.example.japim.ui.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreTime
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.japim.globalsFunctions.parseReal
import com.example.japim.graphql.GetPresenceParticipantQuery
import com.example.japim.remoteDataSource.EventRemoteDataSource
import com.example.japim.ui.theme.MPlus2
import com.example.japim.ui.theme.PRIMARY
import com.example.japim.ui.theme.SECONDARY
import com.example.japim.ui.theme.SUCCESS
import kotlinx.coroutines.coroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.japim.apolloClient.TokenManager
import com.example.japim.apolloClient.generateApolloClient


private val FINISHED = "ENC"
private val USER = "USE"
private val VOLUNTEER = "VOL"
private val STUDENT = "STU"
private fun isFinished(status: String) : Boolean{
    return FINISHED == status;
}

private fun isParticipant(initialism : String) : Boolean{
    return initialism in listOf(USER, VOLUNTEER, STUDENT);
}
@Composable
private fun isConfirmedDay(event_id: String, participant_id: String) : Boolean{
    val context = LocalContext.current
    val tokenProvider = remember { TokenManager(context) }
    val eventRemoteDataSource = remember { EventRemoteDataSource(generateApolloClient(tokenProvider)) }
    var verifyParticipant by remember { mutableStateOf<GetPresenceParticipantQuery.ParticipantPresenceDay?>(null) }
    LaunchedEffect(Unit){
        verifyParticipant = eventRemoteDataSource.participantPresenceDay(event_id,participant_id)
    }

    return verifyParticipant != null
}

@Composable
private fun GenerateCardEvent(
    id : String,
    total_participants : Int,
    max_participants : Int,
    total_volunteers : Int,
    max_volunteers : Int,
    workload : Int,
    value : Double,
    in_event : Boolean,
    status : String,
    auth_id : String,
    auth_role:  String,
    onClickParticipant : ()-> Unit,
    onClickVolunteer : ()-> Unit,
    onClickPresence : ()-> Unit,
    onClickFinishEvent : ()->Unit
    ){
    Card(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        shape = RoundedCornerShape(0),
        colors = CardDefaults.elevatedCardColors(
            containerColor = SECONDARY.copy(0.4f),
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround,

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Participantes",
                    tint = PRIMARY
                )

                Text("Participantes", color= PRIMARY, fontFamily = MPlus2, fontSize = 13.sp, fontWeight = FontWeight.Bold);
                Text("$total_participants/$max_participants", color= PRIMARY, fontFamily = MPlus2, fontSize = 15.sp);

            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Icon(
                    imageVector = Icons.Default.SupportAgent,
                    contentDescription = "Voluntários",
                    tint = PRIMARY
                )

                Text("Voluntários", color= PRIMARY, fontFamily = MPlus2, fontSize = 13.sp, fontWeight = FontWeight.Bold);
                Text("$total_volunteers/$max_volunteers", color= PRIMARY, fontFamily = MPlus2, fontSize = 15.sp);
            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Icon(
                    imageVector = Icons.Default.MoreTime,
                    contentDescription = "Carga Horária",
                    tint = PRIMARY
                )

                Text("Carga Horária", color= PRIMARY, fontFamily = MPlus2, fontSize = 13.sp,fontWeight = FontWeight.Bold);
                Text("$workload", color= PRIMARY, fontFamily = MPlus2, fontSize = 15.sp);
            }


            if(value != 0.0){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {

                    Icon(
                        imageVector = Icons.Default.Paid,
                        contentDescription = "Valor",
                        tint = PRIMARY
                    )

                    Text("Valor", color= PRIMARY, fontFamily = MPlus2, fontSize = 13.sp,fontWeight = FontWeight.Bold);
                    Text("R$ ${value.parseReal()}", color= PRIMARY, fontFamily = MPlus2, fontSize = 15.sp);
                }
            }
        }

        if(!in_event && !isFinished(status)){
            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = onClickParticipant,
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 10.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SUCCESS
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Ser Participante")
                }

                Button(
                    onClick = onClickVolunteer,
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 10.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SUCCESS
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Ser Voluntário")
                }

            }
        }else if(!isFinished(status) && !isConfirmedDay(id, auth_id) && isParticipant(auth_role) ){
            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center
            ){

                Button(
                    onClick = onClickPresence,
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 10.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SUCCESS
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Solicitar Presença")
                }
            }
        }else if(!isFinished(status)){
            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center
            ){

                Button(
                    onClick = onClickFinishEvent,
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 10.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SUCCESS
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Finalizar Evento")
                }
            }
        }



    }
}
@Composable
fun GenerateEventDetails(
    id: String,
    title: String,
    description: String,
    name_speakers: List<String>,
    total_participants: Int,
    max_participants: Int,
    total_volunteers: Int,
    max_volunteers: Int,
    workload: Int,
    value: Double,
    in_event: Boolean,
    status: String,
    auth_id : String,
    auth_role: String,
    onClickParticipant: () -> Unit,
    onClickVolunteer: () -> Unit,
    onClickPresence: ()-> Unit,
    onClickFinishEvent: () -> Unit,
){
    Column(
        modifier = Modifier.fillMaxHeight(.9f).fillMaxWidth().padding(12.dp).clip(RoundedCornerShape(10.dp)).background(SECONDARY).padding(vertical = 12.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        Text(
            title,
            color = PRIMARY,
            fontFamily = MPlus2,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        );

        Text(description, color = PRIMARY, fontFamily = MPlus2, fontSize = 20.sp);

        Text(
            "Palestrante",
            color = PRIMARY,
            fontFamily = MPlus2,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        );

        name_speakers.forEach { speaker->
            Text(speaker, color = PRIMARY, fontFamily = MPlus2, fontSize = 25.sp);
        }

        GenerateCardEvent(
            id,
            total_participants,
            max_participants,
            total_volunteers,
            max_volunteers,
            workload,
            value,
            in_event,
            status,
            auth_id,
            auth_role,
            onClickParticipant,
            onClickVolunteer,
            onClickPresence,
            onClickFinishEvent
        );

    }

}
