package com.example.covid_19tracker

import retrofit2.Response
import retrofit2.http.GET

interface CovidApi {

    //"Call" is useful when we are willing to use its enqueue callback function.
    //When we use Coroutines or RxJava in the project(which is the best professional practice) to provide asynchronous execution ,
    // we don't need enqueue callback. We could just use Response.

    @GET("countries")
    suspend fun getCovidData(): Response<List<DataModel>>

    //note: we could also pass query parameters as arguments of the function eg: api key
}