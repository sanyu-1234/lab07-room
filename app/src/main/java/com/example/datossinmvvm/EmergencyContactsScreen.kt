package com.example.datossinmvvm

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.room.Room
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactsScreen() {

    val context =
        LocalContext.current

    val database =
        remember {

            crearPulseraDatabase(
                context
            )
        }

    val dao =
        remember {

            database
                .emergencyContactDao()
        }

    val scope =
        rememberCoroutineScope()

    var name by remember {
        mutableStateOf("")
    }

    var phone by remember {
        mutableStateOf("")
    }

    var relationship by remember {
        mutableStateOf("")
    }

    var editingId by remember {
        mutableIntStateOf(0)
    }

    var contacts by remember {
        mutableStateOf(
            emptyList<EmergencyContact>()
        )
    }

    suspend fun cargarContactos() {

        contacts =
            dao.getAll()
    }

    LaunchedEffect(Unit) {

        cargarContactos()
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        "Contactos de emergencia"
                    )
                }
            )
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            TextField(
                value = name,
                onValueChange = {
                    name = it
                },
                modifier =
                    Modifier.fillMaxWidth(),
                label = {
                    Text("Nombre")
                }
            )

            Spacer(
                Modifier.height(8.dp)
            )

            TextField(
                value = phone,
                onValueChange = {
                    phone = it
                },
                modifier =
                    Modifier.fillMaxWidth(),
                label = {
                    Text("Teléfono")
                }
            )

            Spacer(
                Modifier.height(8.dp)
            )

            TextField(
                value = relationship,
                onValueChange = {
                    relationship = it
                },
                modifier =
                    Modifier.fillMaxWidth(),
                label = {
                    Text("Parentesco")
                }
            )

            Spacer(
                Modifier.height(12.dp)
            )

            Button(

                modifier =
                    Modifier.fillMaxWidth(),

                onClick = {

                    if (
                        name.isNotBlank() &&
                        phone.isNotBlank()
                    ) {

                        scope.launch {

                            if (
                                editingId == 0
                            ) {

                                dao.insert(

                                    EmergencyContact(
                                        name = name,
                                        phone = phone,
                                        relationship =
                                            relationship
                                    )
                                )

                            } else {

                                dao.update(

                                    EmergencyContact(
                                        id = editingId,
                                        name = name,
                                        phone = phone,
                                        relationship =
                                            relationship
                                    )
                                )
                            }

                            name = ""
                            phone = ""
                            relationship = ""
                            editingId = 0

                            cargarContactos()
                        }
                    }
                }
            ) {

                Text(

                    if (editingId == 0)
                        "Guardar contacto"
                    else
                        "Actualizar contacto"
                )
            }

            Spacer(
                Modifier.height(16.dp)
            )

            LazyColumn {

                items(
                    contacts,
                    key = {
                        it.id
                    }
                ) { contact ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 5.dp
                            )
                    ) {

                        Column(
                            modifier =
                                Modifier.padding(
                                    12.dp
                                )
                        ) {

                            Text(
                                "Nombre: ${contact.name}"
                            )

                            Text(
                                "Teléfono: ${contact.phone}"
                            )

                            Text(
                                "Parentesco: ${contact.relationship}"
                            )

                            Spacer(
                                Modifier.height(
                                    5.dp
                                )
                            )

                            Row(
                                horizontalArrangement =
                                    Arrangement.spacedBy(
                                        8.dp
                                    )
                            ) {

                                TextButton(
                                    onClick = {

                                        editingId =
                                            contact.id

                                        name =
                                            contact.name

                                        phone =
                                            contact.phone

                                        relationship =
                                            contact.relationship
                                    }
                                ) {

                                    Text("Editar")
                                }

                                TextButton(
                                    onClick = {

                                        scope.launch {

                                            dao.delete(
                                                contact
                                            )

                                            cargarContactos()
                                        }
                                    }
                                ) {

                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


fun crearPulseraDatabase(
    context: Context
): PulseraDatabase {

    return Room.databaseBuilder(
        context.applicationContext,
        PulseraDatabase::class.java,
        "pulsera_db"
    ).build()
}