package com.dicoding.picodiploma.submission2.data.reposository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.picodiploma.submission2.data.datasource.UserDao
import com.dicoding.picodiploma.submission2.data.datasource.UserEntity
import com.dicoding.picodiploma.submission2.data.database.UserRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepository (application: Application)  {
    private val mUserDao: UserDao

    init {
        val database = UserRoomDatabase.getDatabase(application)
        mUserDao = database.userDao()
    }

    fun getFavoriteUser(): LiveData<List<UserEntity>> = mUserDao.getFavoriteUser()

    fun insertFavoriteUser(id: Int, avatar: String?, login: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val favoriteUser = UserEntity(id, avatar, login)
            mUserDao.insert(favoriteUser)
        }
    }

    fun deleteFavoriteUser(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            mUserDao.delete(id)
        }
    }

    suspend fun getFavoriteUserById(id: Int)= mUserDao.CheckUser(id)

}

