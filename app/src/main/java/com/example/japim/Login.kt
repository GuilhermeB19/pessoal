
package com.example.japim

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.japim.apolloClient.TokenManager
import com.example.japim.apolloClient.generateApolloClient
import com.example.japim.graphql.LoginMutation
import com.example.japim.graphql.type.Login as LoginInput
import com.example.japim.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun Login(
    modifier: Modifier = Modifier,
    tokenProvider : TokenManager,
    onCriarConta: () -> Unit = {},
    onLoginSucesso: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var carregando by remember { mutableStateOf(false) }
    var mensagemErro by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PRIMARY)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Logo do Japim
        Image(
            painter = painterResource(
                id = R.drawable.logo_japim_preto
            ),
            contentDescription = "Logo do Japim",
            modifier = Modifier.size(160.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Título
        Text(
            text = "Login",
            color = SECONDARY,
            fontFamily = MPlus2,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Campo E-mail
        TextField(
            value = email,
            onValueChange = {
                email = it
                mensagemErro = null
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !carregando,
            placeholder = {
                Text(
                    text = "Insira seu E-mail",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = MPlus2,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = FIFTH
                )
            },
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center,
                fontFamily = MPlus2,
                fontSize = 14.sp
            ),
            shape = RoundedCornerShape(50.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = FOURTH,
                unfocusedContainerColor = FOURTH,
                focusedTextColor = FIFTH,
                unfocusedTextColor = FIFTH,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = FIFTH
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Campo Senha
        TextField(
            value = senha,
            onValueChange = {
                senha = it
                mensagemErro = null
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !carregando,
            placeholder = {
                Text(
                    text = "Insira sua Senha",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = MPlus2,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = FIFTH
                )
            },
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center,
                fontFamily = MPlus2,
                fontSize = 14.sp
            ),
            shape = RoundedCornerShape(50.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = FOURTH,
                unfocusedContainerColor = FOURTH,
                focusedTextColor = FIFTH,
                unfocusedTextColor = FIFTH,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = FIFTH
            )
        )

        if (mensagemErro != null) {
            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = mensagemErro ?: "",
                color = ERROR,
                fontFamily = MPlus2,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Botão Entrar
        Button(
            onClick = {
                val emailInformado = email.trim()

                when {
                    emailInformado.isBlank() || senha.isBlank() -> {
                        mensagemErro = "Preencha o e-mail e a senha."
                    }

                    else -> {
                        carregando = true
                        mensagemErro = null

                        coroutineScope.launch {
                            try {
                                val response = generateApolloClient(tokenProvider).mutation(
                                    LoginMutation(
                                        input = LoginInput(
                                            username = emailInformado,
                                            password = senha
                                        )
                                    )
                                ).execute()

                                val tokenRecebido = response.data?.login?.token_access

                                val token: String? = tokenRecebido as? String

                                when {
                                    response.exception != null -> {
                                        mensagemErro = "Não foi possível conectar à API."
                                    }

                                    response.errors?.isNotEmpty() == true -> {
                                        mensagemErro = "E-mail ou senha inválidos."
                                    }

                                    token.isNullOrBlank() -> {
                                        mensagemErro = "A API não retornou um token válido."
                                    }

                                    else -> {
//                                        val tokenManager = TokenManager(Context);
                                        tokenProvider.saveToken(token)
                                        onLoginSucesso()
                                    }
                                }


                            } catch (e: Exception) {
                                mensagemErro =
                                    "Falha de conexão. Verifique a API e a rede."
                            } finally {
                                carregando = false
                            }
                        }
                    }
                }
            },
            enabled = !carregando,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SUCCESS
            )
        ) {
            if (carregando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = FOURTH,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Entrar",
                    color = FOURTH,
                    fontFamily = MPlus2,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        // Botão Criar Conta
        Button(
            onClick = onCriarConta,
            enabled = !carregando,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = THIRD
            )
        ) {
            Text(
                text = "Criar Conta",
                color = PRIMARY,
                fontFamily = MPlus2,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview(tokenProvider: TokenManager) {
    Login(tokenProvider=tokenProvider)
}
