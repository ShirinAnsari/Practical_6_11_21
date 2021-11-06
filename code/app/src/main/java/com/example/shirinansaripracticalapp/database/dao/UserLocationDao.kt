package com.example.shirinansaripracticalapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.shirinansaripracticalapp.database.entity.UserLocationEntity

@Dao
interface UserLocationDao {
    @Query("SELECT * FROM UserLocationEntity")
    fun getAll(): List<UserLocationEntity>

    @Insert
    fun insertAll(vararg users: UserLocationEntity)

    @Query("DELETE FROM UserLocationEntity")
    fun deleteAll()
}