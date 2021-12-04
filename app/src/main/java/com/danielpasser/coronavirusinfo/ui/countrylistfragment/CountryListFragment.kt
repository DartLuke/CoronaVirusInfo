package com.danielpasser.coronavirusinfo.ui.countrylistfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danielpasser.coronavirusinfo.R
import com.danielpasser.coronavirusinfo.model.Country
import com.danielpasser.coronavirusinfo.model.CovidData
import com.danielpasser.coronavirusinfo.ui.adapter.CountryAdapter
import com.danielpasser.coronavirusinfo.ui.decorator.ItemDecorator
import com.danielpasser.coronavirusinfo.utils.DataState
import com.danielpasser.coronavirusinfo.utils.Resource
import com.danielpasser.coronavirusinfo.viewmodel.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class CountryListFragment : DaggerFragment(), CountryAdapter.OnClickListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory
    private lateinit var viewModel: CountryListViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private val countryAdapter = CountryAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_country_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupObservers()
        setupUI(view)
        setupAdapter(view)
        viewModel.download()
    }




    private fun setupViewModel() {
        viewModel = activity?.run {ViewModelProvider(this, viewModelFactory).get(CountryListViewModel::class.java)} ?: throw Exception("Invalid Activity")
    }

    private fun setupUI(view: View) {
        progressBar = view.findViewById(R.id.progressBar)

    }


    private fun setupAdapter(view: View) {
        recyclerView = view.findViewById<RecyclerView>(R.id.country_recyclerview)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(view.context)
           addItemDecoration(ItemDecorator(5))
            adapter = countryAdapter

        }
    }

    private fun setupObservers() {
        viewModel.covidData.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is DataState.Success<CovidData> -> {
                    changeListViewData(dataState.data.Countries)
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

    private fun changeListViewData(countries: List<Country>) {
        countryAdapter.changeData(countries)
    }

    override fun onClickItem(country: Country) {
        switchFragment(country)
    }


    private fun switchFragment(country: Country) {
       val nav = CountryListFragmentDirections.actionCountryListFragmentToCountryDetailsFragment(country)
        findNavController().navigate(nav)
    }
}