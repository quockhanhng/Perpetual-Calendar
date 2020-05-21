package com.quockhanhng.training.perpetualcalendar.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.quockhanhng.training.perpetualcalendar.R
import com.quockhanhng.training.perpetualcalendar.getWeekDayShort
import com.quockhanhng.training.perpetualcalendar.model.WeatherResponse

class WeatherForecastAdapter(
    private val context: Activity,
    private val listOfWeatherResponses: ArrayList<ArrayList<WeatherResponse>>
) : ArrayAdapter<ArrayList<WeatherResponse>>(
    context,
    R.layout.item_weather_forecast,
    listOfWeatherResponses
) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.item_weather_forecast, null, true)

        val itemDate = rowView.findViewById(R.id.itemDate) as TextView
        val itemWeatherInfo = rowView.findViewById(R.id.itemWeatherInfo) as TextView
        val ivWeather = rowView.findViewById(R.id.ivWeather) as ImageView

        val weatherResponses = listOfWeatherResponses[position]

        val date = weatherResponses[0].dt_txt?.split(" ")?.get(0)!!.split("-")
        val yy = date[0].toInt()
        val mm = date[1].toInt()
        val dd = date[2].toInt()
        val day = getWeekDayShort(dd, mm, yy)
        itemDate.text = "$day, $dd/$mm"
        var minTemp = weatherResponses[0].main.temp_min
        var maxTemp = weatherResponses[0].main.temp_max
        for (weatherResponse in weatherResponses) {
            minTemp = minTemp.coerceAtMost(weatherResponse.main.temp_min)
            maxTemp = maxTemp.coerceAtLeast(weatherResponse.main.temp_max)
        }
        val infoString =
            "${minTemp.toInt()} - ${maxTemp.toInt()}°C, ${weatherResponses[0].weatherList[0].description}"
        itemWeatherInfo.text = infoString

        val iconId = "ic_${weatherResponses[0].weatherList[0].icon}"
        val context = ivWeather.context
        val id = context.resources.getIdentifier(iconId, "drawable", context.packageName)
        ivWeather.setImageResource(id)

        return rowView
    }
}