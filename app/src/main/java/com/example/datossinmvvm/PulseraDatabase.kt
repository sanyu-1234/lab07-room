package com.example.datossinmvvm

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        EmergencyContact::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PulseraDatabase :
    RoomDatabase() {

    abstract fun emergencyContactDao():
            EmergencyContactDao
}