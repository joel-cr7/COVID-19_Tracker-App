package com.example.covid_19tracker

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.covid_19tracker.databinding.ActivityMainBinding
import org.eazegraph.lib.models.PieModel

//using onClickListener on our spinner items
class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var country: String   //to hold the country selected by user
    private val types = arrayOf("cases", "deaths", "recovered", "active")  //for options in spinner
    private var covidDataList = ArrayList<DataModel>()  //data that will come from api
    private lateinit var covidAdapter: CovidAdapter   //adapter for recycler view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.spinner.onItemSelectedListener = this
        val arrayAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, types)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = arrayAdapter

        with(binding.recyclerview){
            covidAdapter = CovidAdapter(applicationContext, covidDataList)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = covidAdapter
        }

        makeApiCall()


        //country picker feature
        with(binding.ccp){
            setAutoDetectedCountry(true)    //detect users country automatically
            country = selectedCountryName
            fetchData()
        }

        binding.ccp.setOnCountryChangeListener{
            country = binding.ccp.selectedCountryName
            fetchData()
        }

    }


    private fun makeApiCall(){
        //coroutine created below is lifecycle aware and will be destroyed when app goes in the background
        lifecycleScope.launchWhenCreated {
            binding.progressbar.isVisible = true

            val response = try {
                //create reference of singleton object and make the api call
                ApiCall.api.getCovidData()
            }
            catch (e: Exception){
                e.printStackTrace()
                binding.progressbar.isVisible = false
                return@launchWhenCreated
            }

            //correct response
            if(response.isSuccessful && response.body()!=null){
                //getting objects of type 'DataModel'. Originally we were getting json but using Gson we converted it to objects (this is done in 'ApiCall').
                covidDataList.addAll(response.body()!!)
                covidAdapter.notifyDataSetChanged()
            }

            binding.progressbar.isVisible = false
        }
    }


    private fun fetchData(){
        lifecycleScope.launchWhenCreated {
            binding.progressbar.isVisible = true

            val response = try {
                ApiCall.api.getCovidData()
            }
            catch (e: Exception){
                e.printStackTrace()
                binding.progressbar.isVisible = false
                return@launchWhenCreated
            }

            //correct response
            if(response.isSuccessful && response.body()!=null){
                covidDataList.addAll(response.body()!!)
                //iterate through the whole size of the arraylist
                for(i in 0 until covidDataList.size){

                    //according to default/selected country get the data from the arraylist and set it on the textviews
                    if(covidDataList[i].country == country){

                        //set the data on the textviews
                        binding.activecase.text = covidDataList[i].active
                        binding.totalcase.text = covidDataList[i].cases
                        binding.todaytotal.text = covidDataList[i].todayCases
                        binding.totaldeath.text = covidDataList[i].deaths
                        binding.todaydeath.text = covidDataList[i].todayDeaths
                        binding.recoveredcase.text = covidDataList[i].recovered
                        binding.todayrecovered.text = covidDataList[i].todayRecovered

                        //set data on the graph
                        val active = covidDataList[i].active.toInt()
                        val total = covidDataList[i].cases.toInt()
                        val recovered = covidDataList[i].recovered.toInt()
                        val deaths = covidDataList[i].deaths.toInt()

                        updateGraph(active, total, recovered, deaths)

                    }

                }

            }

            binding.progressbar.isVisible = false
        }
    }

    private fun updateGraph(active: Int, total: Int, recovered: Int, deaths: Int) {
        binding.piechart.clearChart()
        with(binding.piechart){
            addPieSlice(PieModel("Confirm", total.toFloat(), Color.parseColor("#FFB701")))
            addPieSlice(PieModel("Active", active.toFloat(), Color.parseColor("#FF4CAF50")))
            addPieSlice(PieModel("Recovered", recovered.toFloat(), Color.parseColor("#38ACCD")))
            addPieSlice(PieModel("Deaths", deaths.toFloat(), Color.parseColor("#F55c47")))
            startAnimation()
        }

    }

    //both functions are part of the interface 'OnItemSelectedListener'.
    //getting the selected item from the spinner
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = types[position]
        binding.filter.text = item
        covidAdapter.filter(item)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}

