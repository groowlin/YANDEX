package com.example.yandex

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var favourite: TextView
    lateinit var allText: TextView

    var stocksFav = ArrayList<Stock>()
    var all = ArrayList<Stock>()


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        favourite = findViewById(R.id.favourite)
        allText = findViewById(R.id.all)

        favourite.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        favourite.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32F)
                        allText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24F)
                        favourite.setTextColor(Color.rgb(0,0,0))
                        allText.setTextColor(Color.rgb(186, 189, 192))

                        newList(1)
                    }

                }
                return v?.onTouchEvent(event) ?: false
            }
        })

        allText.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        allText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32F)
                        favourite.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24F)
                        allText.setTextColor(Color.rgb(0,0,0))
                        favourite.setTextColor(Color.rgb(186, 189, 192))

                        newList(0)
                    }

                }
                return v?.onTouchEvent(event) ?: false
            }
        })

        search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                justForSearch(newText!!)
                return false
            }

        })
    }
    fun newList(buttonIndex: Int){

        val fragmentManager = supportFragmentManager

        val fragmentFav = fragmentManager.findFragmentById(R.id.listFragment) as StocksFragment?
        fragmentFav?.adapter2(buttonIndex)
    }

    fun justForSearch(newText: String){

        val fragmentManager = supportFragmentManager

        val fragmentSearch = fragmentManager.findFragmentById(R.id.listFragment) as StocksFragment?
        fragmentSearch?.adapterForSearch(newText)
    }
}
