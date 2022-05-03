package com.dicoding.picodiploma.submission2.data.datasource

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table")
    fun getFavoriteUser(): LiveData<List<UserEntity>>

    @Query ("DELETE FROM user_table WHERE user_table.id = :id")
    suspend fun delete(id: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Query("SELECT COUNT(*) FROM user_table WHERE user_table.id = :id")
    suspend fun CheckUser(id: Int): Int

}