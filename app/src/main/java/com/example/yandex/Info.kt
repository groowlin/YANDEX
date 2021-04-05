package com.example.yandex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_info.*

class Info : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val jsonFile = intent.getStringExtra("jsonFile")

        val gson = Gson()
        val obj: Stock = gson.fromJson<Stock>(jsonFile, Stock::class.java)

        tickerInfo.text = obj.ticker
        nameInfo.text = obj.name
        countryInfo.text ="Country: \n" + obj.country
        currentPriceInfo.text = "Current price: \n" + "$" +obj.sum
        ipoInfo.text ="IPO: \n"+ obj.ipo
        industryInfo.text ="Industry: \n"+ obj.finnhubIndustry
        webInfo.text = "Web URL: \n"+obj.weburl

        if(obj.logo != ""){
            Picasso.get()
                .load(obj.logo)
                .resize(1500,1500)
                .placeholder(R.drawable.ic_dollar).resize(1500,1500)
                .into(logoInfo)
        }

        if(obj.isLike)
            likeInfo.setImageResource(R.drawable.ic_favourite2)
        else
            likeInfo.setImageResource(R.drawable.ic_favourite)

        //кнопка стрелки "назад" работает, но из-за неисправности svg её не видно
        finish2.setOnClickListener {
            finish()
        }
    }
}
