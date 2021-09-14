package com.udeciti.list.activities.earthquake

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.udeciti.list.R
import com.udeciti.list.databinding.ActivityEarthquakeBinding
import com.udeciti.list.models.Earthquake
import java.text.SimpleDateFormat
import java.util.*

class Earthquake : AppCompatActivity() {
    companion object {
        const val EARTHQUAKE_KEY = "earthquake_info"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityEarthquakeBinding>(this, R.layout.activity_earthquake)
        val bundle = intent.extras!!
        val earthquakeInfo = bundle.getParcelable<Earthquake>(EARTHQUAKE_KEY)
        binding.earthquakeModel = earthquakeInfo

        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = Date(earthquakeInfo!!.time)
        val formattedString = simpleDateFormat.format(date)
        binding.date.text = formattedString

    }
}