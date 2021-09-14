package com.udeciti.list.activities.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udeciti.list.databinding.ListItemBinding
import com.udeciti.list.models.Earthquake

class EarthquakeAdapter(private val earthquakes: List<Earthquake>) : RecyclerView.Adapter<EarthquakeAdapter.ViewHolder>() {

    lateinit var onItemClick: (Earthquake) -> Unit

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ListItemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context))

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(earthquakes[position])
    }

    inner class ViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(earthquake: Earthquake){
            binding.magnitude.text = earthquake.magnitude.toString()
            binding.place.text = earthquake.place
            binding.root.setOnClickListener(){
                if (::onItemClick.isInitialized)
                    onItemClick(earthquake)
            }
        }
    }

    override fun getItemCount(): Int {
        return  earthquakes.count()
    }
}