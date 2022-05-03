package com.dicoding.picodiploma.submission2.ui.favorite

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.submission2.data.reposository.UserRepository


class FavoriteUserViewModel(application: Application): ViewModel(){
    private val mUserRepository: UserRepository = UserRepository(application)

    fun getUser() = mUserRepository.getFavoriteUser()

}
