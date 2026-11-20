package com.example.japim.screen.events

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.japim.graphql.GetEventByIdQuery
import com.example.japim.remoteDataSource.EventRemoteDataSource
import com.example.japim.remoteDataSource.AuthRemoteDataSource
import com.example.japim.ui.events.GenerateEventDetails
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import com.example.japim.graphql.AuthQuery
import com.example.japim.ui.theme.PRIMARY
import com.example.japim.ui.theme.ERROR
import androidx.compose.material3.Text
import kotlinx.coroutines.launch
import com.example.japim.apolloClient.TokenManager
import com.example.japim.apolloClient.generateApolloClient


@Composable
fun EventDetailScreen(
    tokenProvider: TokenManager,
    event_id : String
){
    val client = remember { generateApolloClient(tokenProvider) }
    val authRemoteDataSource = remember { AuthRemoteDataSource(client) }
    val eventRemoteDataSource = remember { EventRemoteDataSource(client) }
    val coroutineScope = rememberCoroutineScope()

    var auth by remember { mutableStateOf<AuthQuery.Me?>(null) }
    var isAuthLoading by remember { mutableStateOf(true) }
    var event by remember { mutableStateOf<GetEventByIdQuery.Event?>(null) }
    var isEventLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            auth = authRemoteDataSource.auth()
            isAuthLoading = false
        }
        coroutineScope.launch {
            event = eventRemoteDataSource.fetchEventById(event_id)
            isEventLoading = false
        }
    }

    if (isAuthLoading || isEventLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PRIMARY)
        }
        return
    }

    val currentUser = auth
    if (currentUser == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "erro na autenticação para detalhes de eventos",
                color = ERROR
            )
        }
        return
    }

    event?.let {
        GenerateEventDetails(
            id=it.id,
            title = it.name,
            description = it.description,
            name_speakers = it.speakers.map { speaker -> speaker?.name ?: "" },
            total_participants = it.total_participants,
            max_participants = it.max_participants,
            total_volunteers = it.total_volunteers,
            max_volunteers = it.max_volunteers,
            workload = it.workload,
            value = it.value,
            it.in_event,
            it.status_event.initialism,
            currentUser.id,
            currentUser.role.initialism,
            onClickParticipant = {
                coroutineScope.launch {
                    val result = eventRemoteDataSource.subscribeParticipant(event_id)
                    if (result != null) {
                        Log.d("Subscribe", "Inscrição realizada com sucesso: ${result.id}")
                    } else {
                        Log.e("Subscribe", "Falha ao se inscrever")
                    }
                }
            },
            onClickVolunteer = {
                coroutineScope.launch {
                    val result = eventRemoteDataSource.subscribeVolunteer(event_id)
                    if(result != null){
                        Log.d("Subscribe", "Inscrição realizada como voluntário realizado com sucesso: ${result.id}")
                    } else {
                        Log.e("Subscribe", "Falha ao se inscrever")
                    }
                }
            },
            onClickPresence = {
                coroutineScope.launch {
                    val result = eventRemoteDataSource.confirmedPresenceByVolunteer(it.id, currentUser.id)
                    if(result != null){
                        Log.d("Presence", "Presença confirmada com sucesso: ${result.id}")
                    } else {
                        Log.e("Presence", "Falha ao confirmar a presença")
                    }
                }
            },
            onClickFinishEvent = {
                coroutineScope.launch {
                    val result = eventRemoteDataSource.finishEvent(it.id)
                    if(result != null){
                        Log.d("Presence", "Evento Finalizado com sucesso: ${result.id}")
                    } else {
                        Log.e("Presence", "Falha ao Finalizar o evento")
                    }
                }
            }
        )
    }
}
