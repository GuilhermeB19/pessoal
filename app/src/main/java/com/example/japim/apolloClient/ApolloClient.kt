
package com.example.japim.apolloClient

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.apollographql.apollo.ApolloClient
import com.example.japim.BuildConfig
import com.example.japim.interceptor.AuthorizationInterceptor





fun generateApolloClient(tokenProvider : TokenManager): ApolloClient {
    val apolloClient: ApolloClient by lazy {
        ApolloClient.Builder()
            .serverUrl(BuildConfig.GRAPHQL_SERVER_URL)
            .addInterceptor(
                AuthorizationInterceptor(
                    tokenProvider
                )
            )
            .build()
    }
    return apolloClient;
}