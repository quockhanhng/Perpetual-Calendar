package com.quockhanhng.training.perpetualcalendar

import com.quockhanhng.training.perpetualcalendar.model.WeatherForeCastResponse
import com.quockhanhng.training.perpetualcalendar.model.WeatherResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapServices {
    @GET("data/2.5/weather")
    fun weatherRightNow(@Query("q") cityName: String): Observable<WeatherResponse>

    @GET("data/2.5/forecast")
    fun weatherForecast(@Query("q") cityName: String): Observable<WeatherForeCastResponse>
}