package com.example.covid_19tracker

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//singleton, only one instance will be created
object ApiCall {

    val BASE_URL = "https://corona.lmao.ninja/v2/"

    //lazy bcz this will only be called when 'api' is accessed
    val api: CovidApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CovidApi::class.java)
    }

}