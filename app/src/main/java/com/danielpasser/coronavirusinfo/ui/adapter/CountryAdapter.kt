package com.danielpasser.coronavirusinfo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.danielpasser.coronavirusinfo.R
import com.danielpasser.coronavirusinfo.model.Country

class CountryAdapter(private var onClickListener: OnClickListener) :
    RecyclerView.Adapter<CountryAdapter.MyViewHolder>() {
    private val countries: ArrayList<Country> = arrayListOf()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(country: Country, onClickListener: OnClickListener) {
            itemView.findViewById<TextView>(R.id.text_view_country).text = country.Country
            itemView.findViewById<TextView>(R.id.text_view_new_confirmed).text = country.NewConfirmed.toString()
            itemView.findViewById<TextView>(R.id.text_view_new_deaths).text = country.NewDeaths.toString()

            itemView.findViewById<TextView>(R.id.text_view_new_recovered).text = country.NewRecovered.toString()
            itemView.setOnClickListener {
                onClickListener.onClickItem(country)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_view_country_list, parent, false)
    )


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(countries[position], onClickListener)
    }

    override fun getItemCount() = countries.size

    fun changeData(_countries: List<Country>) {
        countries.apply {
            clear()
            addAll(_countries)
        }
        notifyDataSetChanged()
    }


    interface OnClickListener {
        fun onClickItem(country: Country)
    }


}