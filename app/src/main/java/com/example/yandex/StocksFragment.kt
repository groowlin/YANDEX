package com.example.yandex

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finnhub.api.apis.DefaultApi
import com.finnhub.api.infrastructure.ApiClient
import com.google.gson.Gson
import com.novoda.merlin.MerlinsBeard
import kotlinx.android.synthetic.main.fragment_stocks.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class StocksFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    val indicesConstituents = ArrayList<String>()
    val stocks = ArrayList<Stock>()
    val stocksFav = ArrayList<Stock>()
    val tickersArray = Helper(ArrayList<String>())

    lateinit var adapterForSearch: MystocksRecyclerViewAdapter
    lateinit var recycler: RecyclerView

    lateinit var sharedPreference: SharedPreferences
    lateinit var allPreferences: Map<String, String>

    fun apiWork(recyclerView: RecyclerView, context: Context){
        val coroutineScope = CoroutineScope(Dispatchers.Main).launch{

            withContext(Dispatchers.IO){

                ApiClient.apiKey["token"] = "c1f6oof48v6rumcfuoc0"

                val apiClient = DefaultApi()

                try {

                    val res1 = apiClient.indicesConstituents("^DJI")

                    tickersArray.array.clear()

                    for (i in 0..res1.constituents!!.size - 1) {
                        tickersArray.array.add(res1.constituents!![i])
                    }

                    writeTickersListToPref(tickersArray)

                    stocks.clear()

                    if (res1 != null) {
                        Log.w("Result:", res1.constituents.toString())
                        indicesConstituents.addAll(res1.constituents!!)

                        for (i in 0..indicesConstituents.size-10){ // limit of api requests

                            launch {

                                val res2 = apiClient.quote(indicesConstituents.get(i))
                                val res3 = apiClient.companyProfile2(indicesConstituents.get(i), isin = null, cusip = null)

                                val obj = Stock(res3.name, res3.currency, res3.exchange, res3.ipo, res3.ticker,
                                    res3.weburl, res3.logo, res3.finnhubIndustry, res3.country,
                                    res2.c!!.toDouble()/res2.pc!!.toDouble()*100-100,
                                    res2.c!!.toDouble(), res2.c!!.toDouble()-res2.pc!!.toDouble(), false)

                                if(allPreferences.containsKey(res3.ticker)){
                                    obj.isLike = true
                                }
                                jsonToPref(obj)
                            }
                        }

                    } else {
                        Log.w("Result:", "it's null!")
                    }

                } catch (e:Exception) {
                    e.message?.let { Log.w("EXCEPTION", it) }
                }
            }

            stocks.clear()

            for(i in 0..sizeOfPref()-2){
                try {
                    stocks.add(jsonFromPref(tickersArray.array[i]))
                }catch (e: Exception) {
                    Log.e("getApiData()", "ERROR")
                    e.message?.let { Log.e("EXCEPTION", it) }
                }
            }

            adapterForSearch = MystocksRecyclerViewAdapter(stocks, context, true)
            recyclerView.adapter = adapterForSearch
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stocks_list, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.list)

        val intent = Intent(context, Info::class.java)

        if (view is RecyclerView) {
            view.layoutManager = LinearLayoutManager(context)
        }

        sharedPreference =  context!!.getSharedPreferences("PREFERENCE_LIKE",Context.MODE_PRIVATE)
        allPreferences = sharedPreference.all as Map<String, String>

        recycler = recyclerView

        try{

            tickersArray.array = getTickersFromPref("tickers").array

            for(i in 0 until sizeOfPref() - 2){
                stocks.add(jsonFromPref(tickersArray.array [i]))
            }

            Log.w("PREFERENCE", stocks.toString())

            adapterForSearch = MystocksRecyclerViewAdapter(stocks, context!!, false)
            recyclerView.adapter = adapterForSearch

        } catch (e:Exception) {
            e.message?.let { Log.e("EXCEPTION", it) }
        }

        val merlinsBeard = MerlinsBeard.from(context)

        if(merlinsBeard.isConnected || merlinsBeard.isConnectedToWifi) {
            apiWork(recyclerView, context!!)
        }
        else{
            val toast1 = Toast.makeText(context, "Check your network connection \n" +
                    "and just restart the app", Toast.LENGTH_LONG)
            toast1.show()
        }

        return view
    }

    fun adapter2(buttonIndex: Int){

        if(buttonIndex==1){

            stocksFav.clear()

            for (i in 0 until stocks.size){
                if (stocks.get(i).isLike==true){
                    stocksFav.add(stocks.get(i))
                }
            }

            if(stocksFav.isEmpty()){
                val toast = Toast.makeText(context, "You have no favourite yet!", Toast.LENGTH_SHORT)
                toast.show()
            }

            adapterForSearch = MystocksRecyclerViewAdapter(stocksFav,context!!,true)
            recycler.adapter = adapterForSearch
        }

        if(buttonIndex==0){
            adapterForSearch = MystocksRecyclerViewAdapter(stocks,context!!,  true)
            recycler.adapter = adapterForSearch
        }
    }

    fun adapterForSearch(newText: String){
        adapterForSearch.filter.filter(newText)
    }

    fun jsonFromPref (name:String): Stock{
        val sharedPreference2 =  context!!.getSharedPreferences("PREFERENCE_ALL",Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreference2.getString(name, "")
        val obj: Stock = gson.fromJson<Stock>(json, Stock::class.java)
        return obj
    }

    fun writeTickersListToPref(obj: Helper){

        val sharedPreference2 =  context!!.getSharedPreferences("PREFERENCE_ALL",Context.MODE_PRIVATE)

        val prefsEditor: SharedPreferences.Editor = sharedPreference2.edit()
        val gson = Gson()
        val json = gson.toJson(obj)
        prefsEditor.putString("tickers", json)
        prefsEditor.commit()
    }

    fun getTickersFromPref(name: String): Helper{

        val sharedPreference2 =  context!!.getSharedPreferences("PREFERENCE_ALL",Context.MODE_PRIVATE)


        val gson = Gson()
        val json = sharedPreference2.getString(name, "")
        val obj: Helper = gson.fromJson<Helper>(json, Helper::class.java)
        return obj
    }

    fun sizeOfPref():Int{
        val sharedPreference2 =  context!!.getSharedPreferences("PREFERENCE_ALL",Context.MODE_PRIVATE)
        val allPreferences: Map<String, *> = sharedPreference2.getAll()
        return allPreferences.size
    }

    fun jsonToPref(obj: Stock){
        val sharedPreference2 =  context!!.getSharedPreferences("PREFERENCE_ALL",Context.MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor = sharedPreference2.edit()

        val gson = Gson()

        val json = gson.toJson(obj)

        prefsEditor.putString(obj.ticker, json)
        prefsEditor.commit()
    }

}
