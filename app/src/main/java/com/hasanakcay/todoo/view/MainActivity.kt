package com.hasanakcay.todoo.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hasanakcay.todoo.R
import com.hasanakcay.todoo.adapter.NoteListAdapter
import com.hasanakcay.todoo.model.OpenWeatherModel
import com.hasanakcay.todoo.model.WeatherModel
import com.hasanakcay.todoo.service.WeatherAPI
import com.hasanakcay.todoo.util.RealmHelper
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var noteRecyclerView: RecyclerView
    private var compositeDisposable : CompositeDisposable ?= null
    private val BASE_URL = "https://api.openweathermap.org/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteRecyclerView = findViewById(R.id.rv_note_list)
        compositeDisposable = CompositeDisposable()

        if (RealmHelper().getAllNote(this).size == 0) {
            empty_list_tv.visibility = View.VISIBLE
            noteRecyclerView.visibility = View.GONE
        } else {
            empty_list_tv.visibility = View.GONE
            noteRecyclerView.visibility = View.VISIBLE
            noteRecyclerView.layoutManager = LinearLayoutManager(this)
            noteRecyclerView.adapter = NoteListAdapter(RealmHelper().getAllNote(this), this)
        }
        callAllWeatherData()
    }

    fun addNote(view: View) {
        val intent = Intent(this, NoteDetailActivity::class.java)
        startActivity(intent)
    }

    fun callAllWeatherData(){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(WeatherAPI::class.java)

        compositeDisposable?.add(retrofit.getWeatherData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse))
    }

    private fun handleResponse(weatherModelData : OpenWeatherModel){
        weatherModelData.let {
            tv_description.text = it.weather.get(0).description
            tv_temprature.text = it.main.temp.toString()

            val icon = it.weather.get(0).icon
            val iconURL = "https://openweathermap.org/img/w/$icon.png"
            Picasso.get().load(iconURL).into(imageView)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }

}