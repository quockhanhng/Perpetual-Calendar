package com.quockhanhng.training.perpetualcalendar.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.quockhanhng.training.perpetualcalendar.fragment.ContentFragment


class CalendarAdapter(
    private var mFragments: ArrayList<ContentFragment>,
    fm: FragmentManager
) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var lastPos: Int = 151

    override fun getItem(position: Int): Fragment {
        return when {
            position > lastPos -> {
                lastPos = position
                ContentFragment(mFragments[2].date)
            }
            position < lastPos -> {
                lastPos = position
                ContentFragment(mFragments[0].date)
            }
            else -> {
                lastPos = position
                ContentFragment(mFragments[1].date)
            }
        }
    }

    override fun getCount() = 300
}