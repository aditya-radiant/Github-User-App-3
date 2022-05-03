package com.dicoding.picodiploma.submission2.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import com.dicoding.picodiploma.submission2.R
import com.dicoding.picodiploma.submission2.adapter.PagerAdapter
import com.dicoding.picodiploma.submission2.data.helper.FavoriteViewModelFactory
import com.dicoding.picodiploma.submission2.databinding.ActivityDetailUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_AVATAR = "extra_avatar"
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var viewModel: DetailUserViewModel
    private lateinit var binding: ActivityDetailUserBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "User Profile"

        val login = intent.getStringExtra(EXTRA_USERNAME)
        val avatar = intent.getStringExtra(EXTRA_AVATAR)
        val id = intent.getIntExtra(EXTRA_ID, 0)

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, login)

        viewModel = obtainViewModel(this@DetailUserActivity)



        viewModel.isLoading.observe(this, {
            showLoading(it)
        })

        if (login != null) {
            viewModel.setUserDetail(login)
        }
        viewModel.getDetail().observe(this, {
            if(it != null){
                binding.apply {
                    tvName.text = it.name
                    tvUserName.text = it.login
                    tvFollowers.text = it.followers.toString()
                    tvFollowing.text = it.following.toString()
                    imageView.load(it.avatar_url) {
                        transformations(CircleCropTransformation())
                    }
                }
            }
        })

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch{
            val count = viewModel.checkFavorite(id)
            withContext(Dispatchers.Main){
                if (count > 0){
                    binding.fabFav.setImageResource(R.drawable.ic_favorite_clicked_red)
                    _isChecked = true
                }else{
                    binding.fabFav.setImageResource(R.drawable.ic_favorite_border)
                    _isChecked = false
                }

            }
        }

        binding.fabFav.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked){
                viewModel.insertFavorite(id, avatar, login)
            }else{
                viewModel.deleteFavorite(id)
            }
            binding.fabFav.setImageResource(
                if (_isChecked) R.drawable.ic_favorite_clicked_red else R.drawable.ic_favorite_border)
        }


        val pagerAdapter = PagerAdapter(this, supportFragmentManager, bundle)
        binding.apply {
            userPager.adapter = pagerAdapter
            userTab.setupWithViewPager(userPager)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailUserViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailUserViewModel::class.java)
    }
}








