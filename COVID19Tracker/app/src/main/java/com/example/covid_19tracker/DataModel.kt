package com.example.covid_19tracker

data class DataModel(
    val active: String,
    val cases: String,
    val country: String,
    val deaths: String,
    val recovered: String,
    val todayCases: String,
    val todayDeaths: String,
    val todayRecovered: String
)