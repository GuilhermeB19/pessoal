
package com.example.japim

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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.japim.apolloClient.TokenManager

import com.example.japim.ui.theme.*
import com.example.japim.apolloClient.generateApolloClient
import com.example.japim.graphql.CreateUserMutation
import com.example.japim.graphql.type.UserInputCreate

import kotlinx.coroutines.launch

@Composable
fun Cadastro(
    modifier: Modifier = Modifier,
    onVoltarLogin: () -> Unit = {},
    tokenProvider : TokenManager,
) {

    var nome by remember { mutableStateOf("") }
    var sobrenome by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var ra by remember { mutableStateOf("") }
    var alunoFucapi by remember { mutableStateOf(false) }

    var carregando by remember { mutableStateOf(false) }
    var mensagemErro by remember { mutableStateOf<String?>(null) }
    var mensagemSucesso by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PRIMARY)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(
                id = R.drawable.logo_japim_preto
            ),
            contentDescription = "Logo do Japim",
            modifier = Modifier.size(160.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Criação de Conta",
            color = SECONDARY,
            fontFamily = MPlus2,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        CampoCadastro(
            valor = nome,
            aoAlterar = { nome = it },
            texto = "Insira seu Nome"
        )

        Spacer(modifier = Modifier.height(10.dp))

        CampoCadastro(
            valor = sobrenome,
            aoAlterar = { sobrenome = it },
            texto = "Insira seu Sobrenome"
        )

        Spacer(modifier = Modifier.height(10.dp))

        CampoCadastro(
            valor = cpf,
            aoAlterar = {
                if (it.length <= 11 && it.all { c -> c.isDigit() }) {
                    cpf = it
                }
            },
            texto = "Insira seu CPF",
            tipoTeclado = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(10.dp))

        CampoCadastro(
            valor = email,
            aoAlterar = { email = it },
            texto = "Insira seu E-mail",
            tipoTeclado = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(10.dp))

        CampoCadastro(
            valor = senha,
            aoAlterar = { senha = it },
            texto = "Insira sua Senha",
            senha = true,
            tipoTeclado = KeyboardType.Password
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = alunoFucapi,
                onCheckedChange = {
                    alunoFucapi = it
                    if (!it) {
                        ra = ""
                    }
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = SECONDARY,
                    uncheckedColor = SECONDARY,
                    checkmarkColor = PRIMARY
                )
            )

            Text(
                text = "Sou Aluno da FUCAPI",
                color = SECONDARY,
                fontFamily = MPlus2,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp
            )
        }

        if (alunoFucapi) {
            CampoCadastro(
                valor = ra,
                aoAlterar = { ra = it },
                texto = "Insira seu RA",
                tipoTeclado = KeyboardType.Number
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        mensagemErro?.let {
            Text(
                text = it,
                color = Color.Red,
                fontFamily = MPlus2,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        mensagemSucesso?.let {
            Text(
                text = it,
                color = SUCCESS,
                fontFamily = MPlus2,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        // Criar conta usando a API GraphQL
        Button(
            onClick = {
                when {
                    nome.isBlank() ||
                            sobrenome.isBlank() ||
                            cpf.isBlank() ||
                            email.isBlank() ||
                            senha.isBlank() -> {
                        mensagemErro =
                            "Preencha todos os campos obrigatórios."
                    }

                    cpf.length != 11 -> {
                        mensagemErro =
                            "O CPF deve conter 11 dígitos."
                    }

                    !android.util.Patterns.EMAIL_ADDRESS
                        .matcher(email.trim()).matches() -> {
                        mensagemErro =
                            "Informe um e-mail válido."
                    }

                    alunoFucapi && ra.isBlank() -> {
                        mensagemErro =
                            "Informe seu RA."
                    }

                    else -> {
                        carregando = true
                        mensagemErro = null
                        mensagemSucesso = null

                        coroutineScope.launch {
                            try {
                                val response = generateApolloClient(tokenProvider).mutation(
                                    CreateUserMutation(
                                        input = UserInputCreate(
                                            name = "${nome.trim()} ${sobrenome.trim()}",
                                            email = email.trim(),
                                            password = senha,
                                            cpf = cpf,
                                            ra = if (alunoFucapi) {
                                                ra.trim()
                                            } else {
                                                ""
                                            }
                                        )
                                    )
                                ).execute()

                                val erroConexao = response.exception
                                val errosGraphQL = response.errors
                                val usuario = response.data?.createUser

                                when {
                                    erroConexao != null -> {
                                        mensagemErro =
                                            "Não foi possível conectar à API."
                                    }

                                    !errosGraphQL.isNullOrEmpty() -> {
                                        mensagemErro =
                                            errosGraphQL.first().message
                                    }

                                    usuario != null -> {
                                        mensagemSucesso =
                                            "Conta criada com sucesso!"

                                        nome = ""
                                        sobrenome = ""
                                        cpf = ""
                                        email = ""
                                        senha = ""
                                        ra = ""
                                        alunoFucapi = false
                                    }

                                    else -> {
                                        mensagemErro =
                                            "Não foi possível concluir o cadastro."
                                    }
                                }

                            } catch (e: Exception) {
                                mensagemErro =
                                    "Falha de conexão com a API: ${e.message}"
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
                    strokeWidth = 2.dp,
                    color = FOURTH
                )
            } else {
                Text(
                    text = "Criar Conta",
                    color = FOURTH,
                    fontFamily = MPlus2,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Limpar formulário
        Button(
            onClick = {
                nome = ""
                sobrenome = ""
                cpf = ""
                email = ""
                senha = ""
                ra = ""
                alunoFucapi = false
                mensagemErro = null
                mensagemSucesso = null
            },
            enabled = !carregando,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = WARNING
            )
        ) {
            Text(
                text = "Limpar",
                color = PRIMARY,
                fontFamily = MPlus2,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(70.dp))

        // Voltar ao Login
        Button(
            onClick = onVoltarLogin,
            modifier = Modifier
                .width(160.dp)
                .height(40.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = THIRD
            )
        ) {
            Text(
                text = "Voltar ao Login",
                color = PRIMARY,
                fontFamily = MPlus2,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun CampoCadastro(
    valor: String,
    aoAlterar: (String) -> Unit,
    texto: String,
    senha: Boolean = false,
    habilitado: Boolean = true,
    tipoTeclado: KeyboardType = KeyboardType.Text
) {
    TextField(
        value = valor,
        onValueChange = aoAlterar,
        modifier = Modifier.fillMaxWidth(),
        enabled = habilitado,
        singleLine = true,
        placeholder = {
            Text(
                text = texto,
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
            keyboardType = tipoTeclado
        ),
        visualTransformation = if (senha) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = FOURTH,
            unfocusedContainerColor = FOURTH,
            disabledContainerColor = FOURTH,
            focusedTextColor = FIFTH,
            unfocusedTextColor = FIFTH,
            disabledTextColor = FIFTH,
            focusedPlaceholderColor = FIFTH,
            unfocusedPlaceholderColor = FIFTH,
            disabledPlaceholderColor = FIFTH,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = FIFTH
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun CadastroPreview(tokenProvider: TokenManager) {
    Cadastro(tokenProvider=tokenProvider)
}
