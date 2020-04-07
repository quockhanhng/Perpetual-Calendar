package com.quockhanhng.training.perpetualcalendar.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.quockhanhng.training.perpetualcalendar.R
import com.quockhanhng.training.perpetualcalendar.getWeekDay
import kotlinx.android.synthetic.main.fragment_content.*
import kotlin.random.Random

class ContentFragment(var date: String) : Fragment() {

    private var dd: Int = 0
    private var mm: Int = 0
    private var yyyy: Int = 0
    private lateinit var quotesArray: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateSplit = date.split("/")
        dd = dateSplit[0].toInt()
        mm = dateSplit[1].toInt()
        yyyy = dateSplit[2].toInt()
        quotesArray = resources.getStringArray(R.array.quotes_array)
        updateUI()
    }

    private fun updateUI() {
        background.setBackgroundColor(getRandomBackground())
        tvDay.text = dd.toString()
        tvWeekDay.text = getWeekDay(dd, mm, yyyy)
        tvQuote.text = getRandomQuote()
    }

    private fun getRandomQuote(): String {
        val index = Random.nextInt(0, quotesArray.size - 1)
        return quotesArray[index]
    }

    private fun getRandomBackground(): Int {
        val colors = resources.getIntArray(R.array.colors)
        val randInt = Random.nextInt(colors.size)
        return colors[randInt]
    }
}
