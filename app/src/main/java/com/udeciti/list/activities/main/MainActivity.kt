package com.udeciti.list.activities.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.udeciti.list.R
import com.udeciti.list.activities.earthquake.Earthquake
import com.udeciti.list.api.Response
import com.udeciti.list.databinding.ActivityMainBinding

private const val SORT_BY_KEY  = "earthquakeSortBy"
private const val PREFERENCES_NAME = "earthquakes_preferences"

class MainActivity : AppCompatActivity() {
    private lateinit var earthquakeAdapter: EarthquakeAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,
            R.layout.activity_main
        )
        binding.earthquakeRecycler.layoutManager = LinearLayoutManager(this)

        val sortBy = getSortBy()
        viewModel = ViewModelProvider(this, MainViewModelFactory(application, sortBy)).get(MainViewModel::class.java)

        viewModel.status.observe(this, Observer {
            apiResponse ->
            when(apiResponse){
                Response.DONE -> {
                    binding.loading.visibility = View.GONE
                }
                Response.ERROR -> {
                    binding.loading.visibility = View.GONE
                }
                Response.NOT_INTERNET_CONNECTION -> {
                    binding.loading.visibility = View.GONE
                    Toast.makeText(this, "No hay conexión", Toast.LENGTH_SHORT).show()
                }
                Response.LOADING -> {
                    binding.loading.visibility = View.VISIBLE
                }
            }
        })

        viewModel.earthquakesList.observe(this, Observer {
            earthquakeList ->

                earthquakeAdapter = EarthquakeAdapter(earthquakeList)
                binding.earthquakeRecycler.adapter = earthquakeAdapter

                earthquakeAdapter.onItemClick = {
                    val intent: Intent = Intent(this, Earthquake::class.java).apply {
                        putExtra(Earthquake.EARTHQUAKE_KEY, it)
                        Log.i("edgMain", it.magnitude.toString())
                    }
                    startActivity(intent)
                    finish()
                }

                binding.emptyView.visibility = View.GONE
                if (earthquakeList.isEmpty())
                    binding.emptyView.visibility = View.VISIBLE
        })

    }

    private fun getSortBy(): String {
        val preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
        return preferences.getString(SORT_BY_KEY, "") ?: ""
    }

    private fun saveSortPreference(sortBy: String){
        val preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
        val editor = preferences.edit()

        editor.putString(SORT_BY_KEY, sortBy)
        editor.apply()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var sortBy = "time asc"

        if (item.itemId == R.id.main_menu_sort_magnitude){
            sortBy = "magnitude desc"
        }

        viewModel.loadEarthquakes(sortBy)
        saveSortPreference(sortBy)

        return super.onOptionsItemSelected(item)
    }
}