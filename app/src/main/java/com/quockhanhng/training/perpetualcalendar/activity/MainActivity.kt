package com.quockhanhng.training.perpetualcalendar.activity

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.quockhanhng.training.perpetualcalendar.R
import com.quockhanhng.training.perpetualcalendar.adapter.CalendarAdapter
import com.quockhanhng.training.perpetualcalendar.fragment.ContentFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var currentPos: Int = 151
    private val rightNow = Date()
    private lateinit var displayDay: Date
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private lateinit var displayPrevDay: String
    private lateinit var displayCurrDay: String
    private lateinit var displayNextDay: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpBottomNav()
        setUpAdapter()

    }

    private fun setUpBottomNav() {
        bottom_navigation.menu.getItem(0).isCheckable = false
        bottom_navigation.menu.getItem(1).isCheckable = false
        bottom_navigation.menu.getItem(2).isCheckable = false
        bottom_navigation.menu.getItem(3).isCheckable = false

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_details -> {

                    goToDetailsActivity()
                    true
                }
                R.id.nav_calendar -> {

                    true
                }
                R.id.nav_change -> {

                    true
                }
                R.id.nav_pray -> {

                    true
                }
                else -> false
            }
        }
    }

    private fun setUpAdapter() {
        displayDay = rightNow

        displayCurrDay = sdf.format(rightNow)
        displayPrevDay = sdf.format(getPreviousDay(rightNow))
        displayNextDay = sdf.format(getNextDay(rightNow))
        val mFragments = ArrayList<ContentFragment>()
        mFragments.add(ContentFragment(displayPrevDay))
        mFragments.add(ContentFragment(displayCurrDay))
        mFragments.add(ContentFragment(displayNextDay))
        val adapter = CalendarAdapter(mFragments, supportFragmentManager)
        pager.adapter = adapter
        pager.setCurrentItem(151, false)
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position > currentPos) {        // Next day
                    displayDay = getNextDay(displayDay)

                    displayPrevDay = displayCurrDay
                    displayCurrDay = displayNextDay
                    displayNextDay = sdf.format(getNextDay(displayDay))

                    mFragments.removeAt(0)
                    mFragments.add(2, ContentFragment(displayNextDay))
                    adapter.notifyDataSetChanged()

                    currentPos++
                } else if (position < currentPos) { // Previous day
                    displayDay = getPreviousDay(displayDay)

                    displayNextDay = displayCurrDay
                    displayCurrDay = displayPrevDay
                    displayPrevDay = sdf.format(getPreviousDay(displayDay))

                    mFragments.removeAt(2)
                    mFragments.add(0, ContentFragment(displayPrevDay))
                    adapter.notifyDataSetChanged()

                    currentPos--
                }
            }
        })
    }

    private fun goToDetailsActivity() {
        val intent = Intent(applicationContext, DetailsActivity::class.java)
        startActivity(intent)
    }

    private fun getPreviousDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        return calendar.time
    }

    private fun getNextDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        return calendar.time
    }
}
