package com.example.datossinmvvm

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenUser() {

    val context = LocalContext.current

    val db = remember {
        crearDatabase(context)
    }

    val dao = remember {
        db.userDao()
    }

    val coroutineScope = rememberCoroutineScope()

    var firstName by remember {
        mutableStateOf("")
    }

    var lastName by remember {
        mutableStateOf("")
    }

    var dataUser by remember {
        mutableStateOf("")
    }

    var mensaje by remember {
        mutableStateOf("")
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("Usuarios Room")
                },

                actions = {

                    TextButton(
                        onClick = {

                            if (
                                firstName.isNotBlank() &&
                                lastName.isNotBlank()
                            ) {

                                val user = User(
                                    firstName = firstName,
                                    lastName = lastName
                                )

                                coroutineScope.launch {

                                    agregarUsuario(
                                        user = user,
                                        dao = dao
                                    )

                                    dataUser = getUsers(dao)

                                    mensaje =
                                        "Usuario agregado correctamente"
                                }

                                firstName = ""
                                lastName = ""

                            } else {

                                mensaje =
                                    "Ingrese nombre y apellido"
                            }
                        }
                    ) {
                        Text("Agregar")
                    }

                    TextButton(
                        onClick = {

                            coroutineScope.launch {

                                dataUser = getUsers(dao)

                                mensaje =
                                    "Usuarios actualizados"
                            }
                        }
                    ) {
                        Text("Listar")
                    }
                }
            )
        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            TextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("ID (solo lectura)")
                },
                readOnly = true,
                singleLine = true
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            TextField(
                value = firstName,
                onValueChange = {
                    firstName = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("First Name:")
                },
                singleLine = true
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            TextField(
                value = lastName,
                onValueChange = {
                    lastName = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Last Name:")
                },
                singleLine = true
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Button(
                onClick = {

                    coroutineScope.launch {

                        val eliminado =
                            eliminarUltimoUsuario(dao)

                        if (eliminado) {

                            mensaje =
                                "Último usuario eliminado correctamente"

                        } else {

                            mensaje =
                                "No hay usuarios para eliminar"
                        }

                        dataUser =
                            getUsers(dao)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    "Eliminar último registro"
                )
            }

            Spacer(
                modifier = Modifier.height(15.dp)
            )

            Text(
                text = mensaje
            )

            Spacer(
                modifier = Modifier.height(15.dp)
            )

            Text(
                text = dataUser,
                fontSize = 20.sp
            )
        }
    }
}


fun crearDatabase(
    context: Context
): UserDatabase {

    return Room.databaseBuilder(
        context.applicationContext,
        UserDatabase::class.java,
        "user_db"
    ).build()
}


suspend fun getUsers(
    dao: UserDao
): String {

    var respuesta = ""

    val users = dao.getAll()

    users.forEach { user ->

        respuesta +=
            "${user.uid} - ${user.firstName} - ${user.lastName}\n"
    }

    return respuesta
}


suspend fun agregarUsuario(
    user: User,
    dao: UserDao
) {

    try {

        dao.insert(user)

    } catch (e: Exception) {

        Log.e(
            "User",
            "Error al insertar: ${e.message}"
        )
    }
}


suspend fun eliminarUltimoUsuario(
    dao: UserDao
): Boolean {

    val ultimoUsuario =
        dao.getLastUser()
            ?: return false

    dao.delete(
        ultimoUsuario
    )

    return true
}