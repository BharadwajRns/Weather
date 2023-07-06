package com.example.weather.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.RecyclerLytBinding
import com.example.weather.db.entitys.WeatherEntity
import com.example.weather.utils.UnixTimeToDate
import javax.inject.Inject

class LocationsRecyclerAdapter @Inject constructor(val onClick : (weatherEntity: WeatherEntity) -> Unit)
    : RecyclerView.Adapter<LocationsRecyclerAdapter.ViewHolder>() {
        private val items : MutableList<WeatherEntity> = mutableListOf()
        private var binding : RecyclerLytBinding? = null

        class ViewHolder(binding: RecyclerLytBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newItems : List<WeatherEntity>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = RecyclerLytBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        binding?.let {
            item.apply {
                it.tvTimeZone.text = this.timezone
                it.tvTemperature.text = this.current.temp.toString().plus(" c")
                it.tvFeelsLike.text = this.current.feels_like.toString().plus(" c")
                it.tvCurrentDate.text = UnixTimeToDate().convertUnixTimestampToDate(this.current.dt)
                it.tvWindSpeed.text = this.current.wind_speed.toString()
                it.tvClouds.text = this.current.clouds.toString().plus(" %")
                holder.itemView.setOnClickListener {
                    onClick(this)
                }
            }
        }
    }
}