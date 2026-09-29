package com.example.datossinmvvm

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "emergency_contacts"
)
data class EmergencyContact(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    val phone: String,

    val relationship: String
)