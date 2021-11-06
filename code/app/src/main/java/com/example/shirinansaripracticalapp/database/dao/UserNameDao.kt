package com.example.shirinansaripracticalapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.shirinansaripracticalapp.database.entity.UserNameEntity

@Dao
interface UserNameDao {
    @Query("SELECT * FROM UserNameEntity")
    fun getAll(): List<UserNameEntity>

    @Insert
    fun insertAll(vararg users: UserNameEntity)

    @Query("DELETE FROM UserNameEntity")
    fun deleteAll()
}