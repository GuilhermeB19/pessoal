package com.example.japim.screen.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.japim.graphql.GetEventsQuery
import com.example.japim.remoteDataSource.EventRemoteDataSource
import com.example.japim.ui.components.GenerateHeader
import com.example.japim.ui.components.cards.GenerateCard
import com.example.japim.ui.components.cards.GenerateCardCrud
import com.example.japim.ui.components.search.GenerateSearchBar
import com.example.japim.ui.theme.SECONDARY
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.japim.apolloClient.TokenManager
import com.example.japim.apolloClient.generateApolloClient


@Composable
fun EventListScreen(
    tokenProvider: TokenManager
){
    val eventRemoteDataSource = remember { EventRemoteDataSource(generateApolloClient(tokenProvider)) }
    val coroutineScope = rememberCoroutineScope()

    var events by remember {
        mutableStateOf<List<GetEventsQuery.Event?>?>(emptyList())
    }

    val loadEvents: () -> Unit = {
        coroutineScope.launch {
            events = eventRemoteDataSource.fetchEvents()
        }
    }

    LaunchedEffect(Unit) {
        loadEvents()
    }

    GenerateSearchBar("teste",{});

    Column(

    ) {
        events?.forEach { event ->
            if (event != null) {
                GenerateCardCrud(
                    event.name,
                    event.short_description,
                    event.speakers.firstOrNull()?.name ?: "",
                    10,
                    10,
                    event.is_payment,
                    {},
                    {
                        coroutineScope.launch {
                            eventRemoteDataSource.deleteEvent(event.id)
                            loadEvents()
                        }
                    }
                )
            }
        }
    }

}