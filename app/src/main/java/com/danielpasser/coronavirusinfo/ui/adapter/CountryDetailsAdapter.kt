package com.danielpasser.coronavirusinfo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.danielpasser.coronavirusinfo.R
import com.danielpasser.coronavirusinfo.model.Country
import com.danielpasser.coronavirusinfo.model.CountryDetails

class CountryDetailsAdapter() :
    RecyclerView.Adapter<CountryDetailsAdapter.MyViewHolder>() {
    private val countryDetailsList: ArrayList<CountryDetails> = arrayListOf()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(countryDetails: CountryDetails) {
            itemView.findViewById<TextView>(R.id.text_view_date).text = countryDetails.Date.substringBefore("T")
            itemView.findViewById<TextView>(R.id.text_view_confirmed).text = countryDetails.Confirmed.toString()
            itemView.findViewById<TextView>(R.id.text_view_recovered).text = countryDetails.Recovered.toString()
            itemView.findViewById<TextView>(R.id.text_view_deaths).text = countryDetails.Deaths.toString()
            itemView.findViewById<TextView>(R.id.text_view_active).text = countryDetails.Active.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_view_country_details_list, parent, false)
    )


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(countryDetailsList[position])
    }

    override fun getItemCount() = countryDetailsList.size

    fun changeData(_countryDetails: List<CountryDetails>) {
        countryDetailsList.apply {
            clear()
            addAll(_countryDetails)
        }

        notifyDataSetChanged()

    }




}