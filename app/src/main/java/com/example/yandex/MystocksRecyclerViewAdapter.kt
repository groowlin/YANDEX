package com.example.yandex

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import java.util.*

class MystocksRecyclerViewAdapter(

    private val mValues: List<Stock>,
    private val context: Context,
    private val touchDown: Boolean

) : RecyclerView.Adapter<MystocksRecyclerViewAdapter.ViewHolder>(), Filterable {

    var stocksSearch: List<Stock>

    init {
        stocksSearch = mValues
    }

    override fun getItemCount(): Int {
        return stocksSearch.size
    }

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    stocksSearch = mValues
                } else {

                    val resultList = mutableListOf<Stock>()

                    for (row in mValues) {
                        if (row.ticker.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT)) ||
                            row.name.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    stocksSearch = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = stocksSearch
                Log.w("SEARCH",stocksSearch.toString())
                return filterResults
            }
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                stocksSearch = results?.values as List<Stock>
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_stocks, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = stocksSearch[position]

        holder.name.text = item.name
        holder.ticker.text = item.ticker
        holder.sum.text = item.sum
        holder.percent.text = item.percent


        if(item.logo != ""){
            Picasso.get()
                .load(item.logo)
                .resize(400,400)
                .placeholder(R.drawable.ic_dollar)
                .into(holder.logo)
        }

        holder.ticker.text = item.ticker
        if (item.name.length>20)
            holder.name.text = item.name.substring(0, 20) + "..."
        else
            holder.name.text = item.name
        holder.sum.text = "$" + item.sum + " "// + item.currency
        if(item.percent.replace(',', '.').toDouble()<0){
            holder.percent.setTextColor(Color.RED)
            holder.percent.text = "-$" + item.pc.replace("-", "") + " (" + item.percent.replace("-", "") + "%)"
        }
        if (item.percent.replace(',', '.').toDouble()>0){
            holder.percent.setTextColor(Color.rgb(36,178,93))
            holder.percent.text = "+$"+ item.pc + " (" + item.percent + "%)"
        }
        if (item.percent.replace(',', '.').toDouble()==0.toDouble()){
            holder.percent.setTextColor(Color.GRAY)
            holder.percent.text = "=$"+ item.pc + " (" + item.percent + "%)"
        }

        if(item.isLike)
            holder.imageButtonLike.setImageResource(R.drawable.ic_favourite2)
        else
            holder.imageButtonLike.setImageResource(R.drawable.ic_favourite)

        holder.imageButtonLike.setOnClickListener{
            if(!item.isLike) {
                holder.imageButtonLike.setImageResource(R.drawable.ic_favourite2)
                item.isLike = true
                holder.editor.putString(item.ticker, item.ticker).apply()
            } else {
                holder.imageButtonLike.setImageResource(R.drawable.ic_favourite)
                item.isLike = false
                holder.editor.remove(item.ticker).commit()
            }
        }

        jsonToPref(item)

        if(touchDown){
            holder.info.setOnClickListener {
                val sharedPreference2 =  context.getSharedPreferences("PREFERENCE_ALL",Context.MODE_PRIVATE)
                val intent = Intent(context, Info::class.java)
                intent.putExtra("jsonFile", getJsonFromPref(item.ticker,sharedPreference2))
                context.startActivity(intent)
            }
        }
    }

    fun jsonToPref(obj: Stock){
        val sharedPreference2 =  context.getSharedPreferences("PREFERENCE_ALL",Context.MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor = sharedPreference2.edit()

        val gson = Gson()

        val json = gson.toJson(obj)

        prefsEditor.putString(obj.ticker, json)
        prefsEditor.commit()
    }

    fun getJsonFromPref(name: String, mPrefs: SharedPreferences): String{
        val json = mPrefs.getString(name, "")
        return json!!
    }


    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        val ticker: TextView = mView.findViewById(R.id.ticker)
        val logo: ImageView = mView.findViewById(R.id.logo)
        val name: TextView = mView.findViewById(R.id.name)
        val sum: TextView = mView.findViewById(R.id.sum)
        val percent: TextView = mView.findViewById(R.id.percent)
        val sharedPreference =  context.getSharedPreferences("PREFERENCE_FAVOURITE",Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        val imageButtonLike: ImageButton = mView.findViewById(R.id.like)
        val info:RelativeLayout = mView.findViewById(R.id.forInfo)
    }
}
