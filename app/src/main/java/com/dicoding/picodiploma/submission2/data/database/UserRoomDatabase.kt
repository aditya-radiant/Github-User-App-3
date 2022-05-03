package com.dicoding.picodiploma.submission2.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.picodiploma.submission2.data.datasource.UserDao
import com.dicoding.picodiploma.submission2.data.datasource.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class UserRoomDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UserRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserRoomDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

