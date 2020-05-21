package com.quockhanhng.training.perpetualcalendar.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("weather") val weatherList: List<Weather>,
    @SerializedName("main") val main: Main,
    @SerializedName("dt") val dt: Long,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("dt_txt") val dt_txt: String? = null
)

data class Weather(
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class Main(
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feel_like: Double,
    @SerializedName("temp_min") val temp_min: Double,
    @SerializedName("temp_max") val temp_max: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int
)

data class WeatherForeCastResponse(
    @SerializedName("list") val weatherForecastList: List<WeatherResponse>
)