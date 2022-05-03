package com.dicoding.picodiploma.submission2.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.submission2.R
import com.dicoding.picodiploma.submission2.adapter.UserAdapter
import com.dicoding.picodiploma.submission2.data.helper.SettingPreferences
import com.dicoding.picodiploma.submission2.data.helper.ViewModelFactory
import com.dicoding.picodiploma.submission2.data.model.Item
import com.dicoding.picodiploma.submission2.databinding.ActivityMainBinding
import com.dicoding.picodiploma.submission2.ui.detail.DetailUserActivity
import com.dicoding.picodiploma.submission2.ui.favorite.FavoriteUserActivity
import com.google.android.material.switchmaterial.SwitchMaterial

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    private val mainViewModel: MainViewModel by viewModels()


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAdapter = UserAdapter()
        userAdapter.notifyDataSetChanged()


        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Item) {
                Intent(this@MainActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_AVATAR, data.avatar_url)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    startActivity(it)
                }
            }

        })

        var _isDarkMode = true

        val pref = SettingPreferences.getInstance(dataStore)
        val ThemeViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[ThemeViewModel::class.java]
        ThemeViewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.ismode.setImageResource(R.drawable.ic_darkmode)
                _isDarkMode = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.ismode.setImageResource(R.drawable.ic_sunny)
                _isDarkMode = false
            }
        }


        binding.ismode.setOnClickListener {
            _isDarkMode = !_isDarkMode
            if (_isDarkMode) {
                ThemeViewModel.saveThemeSetting(true)
            } else {
                ThemeViewModel.saveThemeSetting(false)
            }
            binding.ismode.setImageResource(
                if (_isDarkMode) R.drawable.ic_darkmode else R.drawable.ic_sunny
            )
        }


        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.apply {
            rvGithubUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvGithubUser.setHasFixedSize(true)
            rvGithubUser.adapter = userAdapter


            btnSearch.setOnClickListener {
                searchUser()
                closeKeyboard()

            }
            searchView.setOnKeyListener { _, keycode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keycode == KeyEvent.KEYCODE_ENTER) {
                    searchUser()
                    closeKeyboard()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false

            }
        }

        mainViewModel.getUsername(false)

        mainViewModel.getUser().observe(this) {
            if (it != null) {
                userAdapter.setData(it)
            }
        }
    }

    private fun searchUser() {
        binding.apply {
            val query = searchView.text.toString()
            if (query.isEmpty()) {
                mainViewModel.getUsername(true)
            } else {
                mainViewModel.setSearchUsername(query)
            }

        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite_item -> {
                Intent(this, FavoriteUserActivity::class.java).also {
                    startActivity(it)
                }
            }R.id.darkmode ->{
                item.setIcon(R.drawable.ic_darkmode)

            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}

