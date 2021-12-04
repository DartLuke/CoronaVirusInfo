package com.danielpasser.coronavirusinfo.ui.countrydetailsfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danielpasser.coronavirusinfo.R
import com.danielpasser.coronavirusinfo.model.Country
import com.danielpasser.coronavirusinfo.model.CountryDetails

import com.danielpasser.coronavirusinfo.ui.adapter.CountryDetailsAdapter

import com.danielpasser.coronavirusinfo.ui.decorator.ItemDecorator
import com.danielpasser.coronavirusinfo.utils.DataState
import com.danielpasser.coronavirusinfo.viewmodel.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CountryDetailsFragment : DaggerFragment() {
private val args:CountryDetailsFragmentArgs by navArgs()
private val country:Country by lazy { args.country }

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory
    private   lateinit var viewModel: CountryDetailsViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private val countryDetailsAdapter = CountryDetailsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_country, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupUI(view)

        setupAdapter(view)
        setupViewModel()
        setupObservers()
        super.onViewCreated(view, savedInstanceState)
        country.Country?.let { viewModel.getCovidData(it) }

    }

    private fun setupUI(view: View) {
        progressBar = view.findViewById(R.id.progressBar)
        view.findViewById<TextView>(R.id.text_view_country).text = country.Country

        view.findViewById<TextView>(R.id.text_view_new_confirmed).text = country.NewConfirmed.toString()
        view.findViewById<TextView>(R.id.text_view_new_deaths).text = country.NewDeaths.toString()
        view.findViewById<TextView>(R.id.text_view_new_recovered).text = country.NewRecovered.toString()

        view.findViewById<TextView>(R.id.text_view_total_confirmed).text = country.TotalConfirmed.toString()
        view.findViewById<TextView>(R.id.text_view_total_deaths).text = country.TotalDeaths.toString()
        view.findViewById<TextView>(R.id.text_view_total_recovered).text = country.TotalRecovered.toString()


    }

    private fun setupAdapter(view: View) {
        recyclerView = view.findViewById<RecyclerView>(R.id.country_details_recyclerview)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(view.context)
            addItemDecoration(ItemDecorator(5))
            adapter = countryDetailsAdapter

        }
    }

    private fun setupObservers() {
        viewModel.covidData.observe(viewLifecycleOwner, { dataState ->

            when (dataState) {
                is DataState.Success<List<CountryDetails>> -> {

                  changeListViewData(dataState.data)
                    showRecycleView(true)
                    showProgressBar(false)
                }
                is DataState.Error -> {

                    showProgressBar(false)
                    showRecycleView(true)
                    displayError(dataState.exception.message)
                }
                is DataState.Loading -> {
                    showRecycleView(false)
                    showProgressBar(true)
                }
            }

        })
    }

    private fun displayError(message: String?) {
        if (message == null)
            Toast.makeText(context, "Unknown Error", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    }

    private fun showProgressBar(isVisible: Boolean) {
        progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE

    }

    private fun showRecycleView(isVisible: Boolean) {
        recyclerView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun changeListViewData(countryDetails: List<CountryDetails>) {
        countryDetailsAdapter.changeData(countryDetails)
    }

    private fun setupViewModel() {
        viewModel = activity?.run { ViewModelProvider(this, viewModelFactory).get(CountryDetailsViewModel::class.java)} ?: throw Exception("Invalid Activity")
    }
}