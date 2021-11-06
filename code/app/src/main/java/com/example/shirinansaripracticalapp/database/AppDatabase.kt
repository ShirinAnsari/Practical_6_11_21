package com.example.shirinansaripracticalapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shirinansaripracticalapp.database.dao.UserNameDao
import com.example.shirinansaripracticalapp.database.dao.UserDao
import com.example.shirinansaripracticalapp.database.dao.UserLocationDao
import com.example.shirinansaripracticalapp.database.dao.UserPictureDao
import com.example.shirinansaripracticalapp.database.entity.UserEntity
import com.example.shirinansaripracticalapp.database.entity.UserLocationEntity
import com.example.shirinansaripracticalapp.database.entity.UserNameEntity
import com.example.shirinansaripracticalapp.database.entity.UserPictureEntity

@Database(
    entities = [UserEntity::class, UserNameEntity::class, UserLocationEntity::class, UserPictureEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao?
    abstract val userNameDao: UserNameDao?
    abstract val userLocationDao: UserLocationDao?
    abstract val userPictureDao: UserPictureDao?

    fun cleanUp() {
        appDB = null
    }

    companion object {
        private var appDB: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase? {
            if (null == appDB) {
                appDB =
                    buildDatabaseInstance(context)
            }
            return appDB
        }

        private fun buildDatabaseInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "user_db"
            )
                .allowMainThreadQueries().build()
        }
    }
}