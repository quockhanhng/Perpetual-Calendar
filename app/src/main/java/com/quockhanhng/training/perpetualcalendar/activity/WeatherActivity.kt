package com.quockhanhng.training.perpetualcalendar.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.quockhanhng.training.perpetualcalendar.API
import com.quockhanhng.training.perpetualcalendar.R
import com.quockhanhng.training.perpetualcalendar.SharedPrefUtil
import com.quockhanhng.training.perpetualcalendar.adapter.WeatherForecastAdapter
import com.quockhanhng.training.perpetualcalendar.model.WeatherForeCastResponse
import com.quockhanhng.training.perpetualcalendar.model.WeatherResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_weather.*
import kotlin.math.roundToInt

class WeatherActivity : AppCompatActivity() {

    private lateinit var weatherForecastAdapter: WeatherForecastAdapter
    private var weatherSubscription: Disposable? = null
    private var weatherForecastSubscription: Disposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        SharedPrefUtil.getData("cityName")?.let { name ->
            updateView(name)
        }

        spinnerCities.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(adapter: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                if (pos != 0) {
                    var selectedCity = adapter?.getItemAtPosition(pos).toString()
                    if (selectedCity == "TP Hồ Chí Minh") selectedCity = "Thành phố Hồ Chí Minh"
                    if (selectedCity == "Bà Rịa - Vũng Tàu") selectedCity = "Vũng Tàu"
                    if (selectedCity == "Thừa Thiên Huế") selectedCity = "Huế"
                    SharedPrefUtil.setData("cityName", selectedCity)
                    selectedCity += ", VN"
                    updateView(selectedCity)
                }
            }
        }
    }

    private fun updateView(name: String) {
        edtCitySearch.setText(name)
        API.create().apply {
            weatherSubscription = weatherRightNow(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { onGetWeatherInfoSuccess(it, name) },
                    { onGetWeatherInfoFailure(it) })
            weatherForecastSubscription = weatherForecast(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { onGetWeatherForecastSuccess(it) },
                    { onGetWeatherForecastFailure(it) })
        }
    }

    private fun onGetWeatherInfoSuccess(weatherResponse: WeatherResponse, name: String) {
        tvTemp.text = "${weatherResponse.main.temp.roundToInt()}°C"
        tvCity.text = name
        tvWeather.text = weatherResponse.weatherList[0].description
        tvHumidity.text = "${weatherResponse.main.humidity}%"
        tvVision.text = "${weatherResponse.visibility / 1000} km"
        val iconId = "ic_${weatherResponse.weatherList[0].icon}"
        val context = ivWeather.context
        val id = context.resources.getIdentifier(iconId, "drawable", context.packageName)
        ivWeather.setImageResource(id)
    }

    private fun onGetWeatherInfoFailure(it: Throwable?) {
        Log.w("Weather Forecast", it.toString())

    }

    private fun onGetWeatherForecastSuccess(weatherForeCastResponse: WeatherForeCastResponse) {
        val weatherForecastList = weatherForeCastResponse.weatherForecastList as ArrayList
        val listOfWeatherForecast = ArrayList<ArrayList<WeatherResponse>>()
        var weatherForecasts = ArrayList<WeatherResponse>()

        for (i in 0 until weatherForecastList.size) {
            if (i != weatherForecastList.size - 1) {
                val date = weatherForecastList[i].dt_txt!!.split(" ")[0]
                val nextDate = weatherForecastList[i + 1].dt_txt!!.split(" ")[0]
                weatherForecasts.add(weatherForecastList[i])
                if (nextDate != date) {
                    listOfWeatherForecast.add(weatherForecasts)
                    weatherForecasts = ArrayList()
                }
            } else {
                weatherForecasts.add(weatherForecastList[i])
                listOfWeatherForecast.add(weatherForecasts)
            }
        }
        weatherForecastAdapter = WeatherForecastAdapter(this, listOfWeatherForecast)
        listViewWeatherForecast.adapter = weatherForecastAdapter
    }

    private fun onGetWeatherForecastFailure(it: Throwable?) {
        Log.w("Weather Forecast", it.toString())
    }

    fun searchCityWeather(view: View) {
        val cityName = edtCitySearch.text.toString()
        SharedPrefUtil.setData("cityName", cityName)
        updateView(cityName)
    }

    override fun onDestroy() {
        super.onDestroy()
        weatherSubscription!!.dispose()
        weatherForecastSubscription!!.dispose()
    }
}
