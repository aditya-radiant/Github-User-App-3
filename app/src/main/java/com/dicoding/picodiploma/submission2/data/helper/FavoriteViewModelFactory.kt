package com.dicoding.picodiploma.submission2.data.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.submission2.ui.detail.DetailUserViewModel
import com.dicoding.picodiploma.submission2.ui.favorite.FavoriteUserViewModel

class FavoriteViewModelFactory private constructor(
    private val application: Application
) : ViewModelProvider.NewInstanceFactory() {


    companion object {
        @Volatile
        private var instance: FavoriteViewModelFactory? = null
        @JvmStatic
        fun getInstance(application: Application): FavoriteViewModelFactory {
            if (instance == null) {
                synchronized(FavoriteViewModelFactory::class.java) {
                    if (instance == null) {
                        instance = FavoriteViewModelFactory(application)
                    }
                }
            }
            return instance as FavoriteViewModelFactory
        }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)) {
            return DetailUserViewModel(application) as T
        } else if (modelClass.isAssignableFrom(FavoriteUserViewModel::class.java)) {
            return FavoriteUserViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

}
