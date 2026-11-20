package com.example.japim.remoteDataSource

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.example.japim.graphql.ConfirmedPresenceByVolunteerMutation
import com.example.japim.graphql.DeleteEventMutation
import com.example.japim.graphql.FinishEventMutation
import com.example.japim.graphql.GetEventByIdQuery
import com.example.japim.graphql.GetEventsQuery
import com.example.japim.graphql.GetPresenceParticipantQuery
import com.example.japim.graphql.SubscribeParticipantMutation
import com.example.japim.graphql.SubscribeVolunteerMutation

class EventRemoteDataSource(private val apolloClient: ApolloClient) {
    suspend fun fetchEvents(): List<GetEventsQuery.Event?>? {    return try {
        val response = apolloClient.query(GetEventsQuery()).execute()

        if (response.hasErrors()) {
            Log.e("APOLLO_TEST", "Erros GraphQL: ${response.errors}")
        } else {
            Log.d("APOLLO_TEST", "Sucesso: ${response.data}")
        }

        response.data?.events
    } catch (e: Exception) {
        // Imprime o erro exato no Logcat (ex: Conexão recusada, Timeout, Host desconhecido)
        Log.e("APOLLO_TEST", "Falha ao executar requisição GraphQL", e)
        null
    }
    }

    suspend fun fetchEventById(id : String) : GetEventByIdQuery.Event? {
        return try {
            val response = apolloClient.query(GetEventByIdQuery(id)).execute();

            if (response.hasErrors()) {
                Log.e("APOLLO_TEST", "Erros GraphQL: ${response.errors}")
            }

            response.data?.event
        } catch (e: Exception) {
            Log.e("APOLLO_TEST", "Falha ao executar requisição GraphQL", e)
            null
        }
    }

    suspend fun subscribeParticipant(event_id : String) : SubscribeParticipantMutation.SubscribeParticipant? {
        return try {
            val response = apolloClient.mutation(SubscribeParticipantMutation(event_id)).execute();

            if (response.hasErrors()) {
                Log.e("APOLLO_TEST", "Erros GraphQL: ${response.errors}")
            }

            response.data?.subscribeParticipant
        } catch (e: Exception) {
            Log.e("APOLLO_TEST", "Falha ao executar requisição GraphQL", e)
            null
        }

    }

    suspend fun subscribeVolunteer(event_id : String) : SubscribeVolunteerMutation.SubscribeVolunteer? {
        return try {
            val response = apolloClient.mutation(SubscribeVolunteerMutation(event_id)).execute();

            if (response.hasErrors()) {
                Log.e("APOLLO_TEST", "Erros GraphQL: ${response.errors}")
            }

            response.data?.subscribeVolunteer
        } catch (e: Exception) {
            Log.e("APOLLO_TEST", "Falha ao executar requisição GraphQL", e)
            null
        }

    }

    suspend fun deleteEvent(event_id : String) : DeleteEventMutation.DeleteEvent ? {
        return try {
            val response = apolloClient.mutation(DeleteEventMutation(event_id)).execute();

            if (response.hasErrors()) {
                Log.e("APOLLO_TEST", "Erros GraphQL: ${response.errors}")
            }

            response.data?.deleteEvent
        } catch (e: Exception) {
            Log.e("APOLLO_TEST", "Falha ao executar requisição GraphQL", e)
            null
        }
    }

    suspend fun confirmedPresenceByVolunteer(id: String, participant_id : String) : ConfirmedPresenceByVolunteerMutation.ConfirmedPresenceByVolunteer?{
        return try {
            val response = apolloClient.mutation(ConfirmedPresenceByVolunteerMutation(id,participant_id)).execute();
            if (response.hasErrors()) {
                Log.e("APOLLO_TEST", "Erros GraphQL: ${response.errors}")
            }

            response.data?.confirmedPresenceByVolunteer
        } catch (e: Exception) {
            Log.e("APOLLO_TEST", "Falha ao executar requisição GraphQL", e)
            null

        }
    }

    suspend fun participantPresenceDay(id : String, participant_id: String) : GetPresenceParticipantQuery.ParticipantPresenceDay?{
        return try {
            val response = apolloClient.query(GetPresenceParticipantQuery(id,participant_id)).execute();
            if (response.hasErrors()) {
                Log.e("APOLLO_TEST", "Erros GraphQL: ${response.errors}")
            }
            response.data?.participantPresenceDay
        } catch (e: Exception) {
            Log.e("APOLLO_TEST", "Falha ao executar requisição GraphQL", e)
            null
        }
    }

    suspend fun finishEvent(id : String) : FinishEventMutation.FinishEvent?{
        return try {
            val response = apolloClient.mutation(FinishEventMutation(id)).execute();
            if (response.hasErrors()) {
                Log.e("APOLLO_TEST", "Erros GraphQL: ${response.errors}")
            }
            response.data?.finishEvent
        } catch (e: Exception) {
            Log.e("APOLLO_TEST", "Falha ao executar requisição GraphQL", e)
            null
        }
    }
}
