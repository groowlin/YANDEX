package com.example.yandex

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finnhub.api.apis.DefaultApi
import com.finnhub.api.infrastructure.ApiClient
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

    fun apiWork(recyclerView: RecyclerView, context: Context){
        val coroutineScope = CoroutineScope(Dispatchers.Main).launch{
            withContext(Dispatchers.IO){
                ApiClient.apiKey["token"] = "c1b7a7n48v6rcdq9uk1g"
                val apiClient = DefaultApi()

                try {
                    val res1 = apiClient.indicesConstituents("^DJI")
                    if (res1 != null) {
                        Log.w("Result:", res1.constituents.toString())
                        indicesConstituents.addAll(res1.constituents!!)

                        for (i in 0..indicesConstituents.size-10){ // limit of api requests
                            launch {
                                val res2 = apiClient.quote(indicesConstituents.get(i))
                                val res3 = apiClient.companyProfile2(indicesConstituents.get(i), isin = null, cusip = null)
                                stocks.add( Stock(res3.name, res3.currency, res3.exchange, res3.ipo, res3.ticker,
                                    res3.weburl, res3.logo, res3.finnhubIndustry, res3.country, res2.c!!.toDouble()/res2.pc!!.toDouble()*100-100, res2.c!!.toDouble(), res2.c!!.toDouble()-res2.pc!!.toDouble()))
                            }

                        }
                    } else {
                        Log.w("Result:", "it's null!")
                    }

                } catch (e:Exception) {
                    e.message?.let { Log.w("EXCEPTION", it) }
                }
            }
            withContext(Dispatchers.Main){
                recyclerView.adapter = MystocksRecyclerViewAdapter(stocks)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stocks_list, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.list)

        if (view is RecyclerView) {
            view.layoutManager = LinearLayoutManager(context)
        }

        apiWork(recyclerView,context!!)

        return view
    }
}
