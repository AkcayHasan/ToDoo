package com.hasanakcay.todoo.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hasanakcay.todoo.R
import com.hasanakcay.todoo.adapter.NoteListAdapter
import com.hasanakcay.todoo.base.BaseActivity
import com.hasanakcay.todoo.databinding.ActivityMainBinding
import com.hasanakcay.todoo.model.OpenWeatherModel
import com.hasanakcay.todoo.service.WeatherAPI
import com.hasanakcay.todoo.util.RealmHelper
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private var compositeDisposable : CompositeDisposable ?= null
    companion object{
        const val BASE_URL = "https://api.openweathermap.org/"
    }

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actions()

        compositeDisposable = CompositeDisposable()

        if (RealmHelper().getAllNote(this).size == 0) {
            binding.emptyListTv.visibility = View.VISIBLE
            binding.rvNoteList.visibility = View.GONE
        } else {
            binding.emptyListTv.visibility = View.GONE
            binding.rvNoteList.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = NoteListAdapter(RealmHelper().getAllNote(this@MainActivity), this@MainActivity)
            }
        }
        callAllWeatherData()
    }

    private fun actions(){
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, NoteDetailActivity::class.java)
            startActivity(intent)
        }
    }

    private fun callAllWeatherData(){
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
            binding.tvDescription.text = it.weather[0].description
            binding.tvTemprature.text = it.main.temp.toString()

            val icon = it.weather[0].icon
            val iconURL = "https://openweathermap.org/img/w/$icon.png"
            Picasso.get().load(iconURL).into(binding.imageView)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }



}