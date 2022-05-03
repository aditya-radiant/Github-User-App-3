package com.dicoding.picodiploma.submission2.data.datasource



import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey
    val id: Int,
    val avatar_url: String?,
    val login: String?


):Serializable