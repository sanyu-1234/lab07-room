package com.example.datossinmvvm

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface EmergencyContactDao {

    @Query(
        "SELECT * FROM emergency_contacts ORDER BY id DESC"
    )
    suspend fun getAll(): List<EmergencyContact>

    @Insert
    suspend fun insert(
        contact: EmergencyContact
    )

    @Update
    suspend fun update(
        contact: EmergencyContact
    )

    @Delete
    suspend fun delete(
        contact: EmergencyContact
    )
}