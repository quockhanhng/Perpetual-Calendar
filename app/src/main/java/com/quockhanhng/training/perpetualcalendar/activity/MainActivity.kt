package com.quockhanhng.training.perpetualcalendar.activity

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.quockhanhng.training.perpetualcalendar.*
import com.quockhanhng.training.perpetualcalendar.adapter.CalendarAdapter
import com.quockhanhng.training.perpetualcalendar.fragment.ContentFragment
import com.quockhanhng.training.perpetualcalendar.model.MyDate
import com.quockhanhng.training.perpetualcalendar.model.MyDate.Companion.CHI
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var currentPos: Int = 151
    private val rightNow = Date()
    private lateinit var displayDay: Date
    private val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())

    private lateinit var displayPrevDay: String
    private lateinit var displayCurrDay: String
    private lateinit var displayNextDay: String
    private lateinit var dateSplit: List<String>
    private var dd: Int = 0
    private var mm: Int = 0
    private var yyyy: Int = 0
    private lateinit var myDate: MyDate
    private lateinit var adapter: CalendarAdapter
    private lateinit var mFragments: ArrayList<ContentFragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpBottomNav()
        setUpAdapter()

        tvToday.text = sdf.format(rightNow).split("/")[0]
        tvMonth.setOnClickListener {
            goToCalendarActivity(displayCurrDay)
        }

        SharedPrefUtil.init(applicationContext)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        intent?.getStringExtra("Chosen day")?.let {
            goToThisDay(it)
        }
    }

    private fun goToThisDay(newDay: String) {
        currentPos = 151

        displayCurrDay = newDay
        val s = newDay.split("/")
        val cal = Calendar.getInstance()
        cal.set(s[2].toInt(), s[1].toInt() - 1, s[0].toInt())
        displayDay = cal.time
        displayPrevDay = sdf.format(getPreviousDay(displayDay))
        displayNextDay = sdf.format(getNextDay(displayDay))
        dateSplit = displayCurrDay.split("/")
        dd = dateSplit[0].toInt()
        mm = dateSplit[1].toInt()
        yyyy = dateSplit[2].toInt()

        myDate = MyDate(dd, mm, yyyy)
        updateUI(myDate)
        mFragments.clear()
        mFragments.add(ContentFragment(displayPrevDay))
        mFragments.add(ContentFragment(displayCurrDay))
        mFragments.add(ContentFragment(displayNextDay))
        adapter = CalendarAdapter(mFragments, supportFragmentManager)
        pager.adapter = adapter
        pager.setCurrentItem(151, false)
    }

    private fun setUpBottomNav() {
        bottom_navigation.menu.getItem(0).isCheckable = false
        bottom_navigation.menu.getItem(1).isCheckable = false
        bottom_navigation.menu.getItem(2).isCheckable = false
        bottom_navigation.menu.getItem(3).isCheckable = false

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_details -> {
                    goToDetailsActivity(displayCurrDay)
                    true
                }
                R.id.nav_calendar -> {
                    goToCalendarActivity(displayCurrDay)
                    true
                }
                R.id.nav_change -> {
                    goToChangeDayActivity(displayCurrDay)
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

        dateSplit = displayCurrDay.split("/")
        dd = dateSplit[0].toInt()
        mm = dateSplit[1].toInt()
        yyyy = dateSplit[2].toInt()

        myDate = MyDate(dd, mm, yyyy)
        updateUI(myDate)

        mFragments = ArrayList()
        mFragments.add(ContentFragment(displayPrevDay))
        mFragments.add(ContentFragment(displayCurrDay))
        mFragments.add(ContentFragment(displayNextDay))
        adapter = CalendarAdapter(mFragments, supportFragmentManager)
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
                // Update UI
                dateSplit = displayCurrDay.split("/")
                dd = dateSplit[0].toInt()
                mm = dateSplit[1].toInt()
                yyyy = dateSplit[2].toInt()
                myDate = MyDate(dd, mm, yyyy)
                updateUI(myDate)
            }
        })
    }

    private fun goToDetailsActivity(displayCurrentDay: String) {
        val intent = Intent(applicationContext, DetailsActivity::class.java)
        intent.putExtra("Current day", displayCurrentDay)
        intent.putExtra("Lunar day", myDate)
        startActivity(intent)
    }

    private fun goToCalendarActivity(displayCurrentDay: String) {
        val intent = Intent(applicationContext, CalendarActivity::class.java)
        intent.putExtra("Current day", displayCurrentDay)
        intent.putExtra("Lunar day", myDate)
        startActivity(intent)
    }

    private fun goToChangeDayActivity(displayCurrentDay: String) {
        val intent = Intent(applicationContext, ChangeDayActivity::class.java)
        intent.putExtra("Current day", displayCurrentDay)
        startActivity(intent)
    }

    fun updateUI(myDate: MyDate) {
        if (tvMonth.text != "$mm/$yyyy")
            tvMonth.text = "$mm/$yyyy"

        val todayLunar = myDate.getLunar()
        tvLunarDate.text = todayLunar[0].toString() + "/" + todayLunar[1]
        tvLunarDay.text = myDate.getLunarDayCanChiName(dd, mm, yyyy)
        val newLunarMonth = myDate.getLunarMonthCanChiName(todayLunar[2], todayLunar[1])
        val newLunarYear = myDate.getCANCHI()
        if (tvLunarMonth.text != newLunarMonth)
            tvLunarMonth.text = newLunarMonth
        if (tvLunarYear.text != newLunarYear)
            tvLunarYear.text = newLunarYear

        var goodHours = "Giờ hoàng đạo: "
        goodHours += getGoodHourInDay(CHI.indexOf(tvLunarDay.text.split(" ")[1]) + 1)
        tvLunarHour.text = goodHours
    }

    fun goBackToday(view: View) {
        goToThisDay(sdf.format(rightNow))
    }

    fun shareThisDay(view: View) {

    }

    fun openNote(view: View) {
        val intent = Intent(applicationContext, NoteManagerActivity::class.java)
        startActivity(intent)
    }
}
