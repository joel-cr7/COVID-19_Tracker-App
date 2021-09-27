package com.example.covid_19tracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.covid_19tracker.databinding.LayoutItemBinding
import java.text.NumberFormat

class CovidAdapter(val context: Context, val covidDataList: ArrayList<DataModel>): RecyclerView.Adapter<CovidAdapter.MyViewHolder>() {

    var m = 1  //filtering data in recycler view

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //get the data at the position of the adapter
        val coviddata = covidDataList[position]

        //'m' is changed according to the selected item form the spinner
        if(m==1){
            holder.binding.countrycase.text = NumberFormat.getInstance().format(coviddata.cases.toInt())
        }
        else if(m==2){
            holder.binding.countrycase.text = NumberFormat.getInstance().format(coviddata.recovered.toInt())
        }
        else if(m==3){
            holder.binding.countrycase.text = NumberFormat.getInstance().format(coviddata.deaths.toInt())
        }
        else{
            holder.binding.countrycase.text = NumberFormat.getInstance().format(coviddata.active.toInt())
        }

        holder.binding.countryname.text = covidDataList[position].country

    }

    override fun getItemCount(): Int {
        return covidDataList.size
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding =  LayoutItemBinding.bind(itemView)
    }

    fun filter(item: String){
        if(item == "cases"){
            m=1
        }
        else if(item == "recovered"){
            m=2
        }
        else if(item == "deaths"){
            m=3
        }
        else{
            m=4
        }
        notifyDataSetChanged()
    }
}