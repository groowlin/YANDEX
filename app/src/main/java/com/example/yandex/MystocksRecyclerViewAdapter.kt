package com.example.yandex

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class MystocksRecyclerViewAdapter(

    private val mValues: ArrayList<Stock>

) : RecyclerView.Adapter<MystocksRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_stocks, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.name.text = item.name
        holder.ticker.text = item.ticker
        holder.sum.text = item.sum
        holder.percent.text = item.percent

        if(item.logo != ""){
            Picasso.get()
                .load(item.logo)
                .resize(55,55)
                // .placeholder(R.drawable.)
                .into(holder.logo)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        val ticker: TextView = mView.findViewById(R.id.ticker)
        val logo: ImageView = mView.findViewById(R.id.logo)
        val name: TextView = mView.findViewById(R.id.name)
        val sum: TextView = mView.findViewById(R.id.sum)
        val percent: TextView = mView.findViewById(R.id.percent)

    }
}
