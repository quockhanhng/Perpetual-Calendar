package com.quockhanhng.training.perpetualcalendar.model

import java.util.*
import kotlin.math.floor
import kotlin.math.sin

class MyDate(private val day: Int, private val month: Int, private val year: Int) {

    companion object {
        val CAN =
            arrayOf("Giáp", "Ất", "Bính", "Đinh", "Mậu", "Kỷ", "Canh", "Tân", "Nhâm", "Quý")
        val CHI =
            arrayOf(
                "Tí",
                "Sửu",
                "Dần",
                "Mão",
                "Thìn",
                "Tỵ",
                "Ngọ",
                "Mùi",
                "Thân",
                "Dậu",
                "Tuất",
                "Hợi"
            )
        val THANG =
            arrayOf(
                "Giêng",
                "Hai",
                "Ba",
                "Tư",
                "Năm",
                "Sáu",
                "Bảy",
                "Tám",
                "Chín",
                "Mười",
                "Mười một",
                "Chạp"
            )

        val TIETKHI = arrayOf(
            "Xuân phân", "Thanh minh", "Cốc vũ", "Lập hạ", "Tiểu mãn", "Mang chủng", "Hạ chí",
            "Tiểu thử", "Đại thử", "Lập thu", "Xử thử", "Bạch lộ", "Thu phân", "Hàn lộ",
            "Sương giáng", "Lập đông", "Tiểu tuyết", "Đại tuyết", "Đông chí", "Tiểu hàn",
            "Đại hàn", "Lập xuân", "Vũ Thủy", "Kinh trập"
        )

        const val PI = Math.PI
        private const val TIMEZONE = 7.0 // Hanoi, Jakarta timezone..
    }

    // Check if a specific year is Leap or not.
    private fun isLeapYear(): Boolean {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0
    }

    // Get hour in CAN CHI.
    fun getHourCAN(h: Int): String? {
        return CAN[2 * (h - 1) % 10] + " " + CHI[0]
    }

    fun getLunarDayCanChiName(d: Int, m: Int, y: Int): String? {
        // Tim can
        var nStem = 0
        nStem += d % 10
        nStem += when (m) {
            8 -> 0
            9, 10 -> 1
            11, 12 -> 2
            3 -> 7
            1, 4, 5 -> 8
            2, 6, 7 -> 9
            else -> -1
        }
        if (isLeapYear()) {
            if (m == 1)
                nStem += 7 - 8      // Vi da cong voi 8 o tren
            if (m == 2)
                nStem += 8 - 9      // Vi da cong voi 9 o tren
        }
        val yy = y % 100
        nStem += (yy * 5 + yy / 4) % 10
        val c = y / 100
        nStem += (4 * c + c / 4 + 2) % 10
        nStem %= 10
        if (nStem == 0) nStem = 10

        //Tim chi
        var nBranch = 0
        nBranch += d % 12
        nBranch += when (m) {
            11 -> 0
            4 -> 2
            2, 6 -> 3
            8 -> 4
            10 -> 5
            12 -> 6
            3 -> 7
            1, 5 -> 8
            7 -> 9
            9 -> 11
            else -> -1
        }
        if (isLeapYear()) {
            if (m == 1)
                nBranch += 7 - 8      // Vi da cong voi 8 o tren
            if (m == 2)
                nBranch += 2 - 3      // Vi da cong voi 3 o tren
        }
        nBranch += (5 * yy + yy / 4) % 12
        nBranch += (8 * c + c / 4 + 2) % 12
        nBranch %= 12
        if (nBranch == 0) nBranch = 12

        return CAN[nStem - 1] + " " + CHI[nBranch - 1]
    }

    fun getLunarMonthCanChiName(y: Int, m: Int): String? {
        return CAN[(3 + (m + y * 12)) % 10] + " " + CHI[(m + 1) % 12]
    }

    fun getLunarMonthName(paramInt: Int): String? {
        return THANG[paramInt]
    }


    // Without parameters. Use specified attributes of this class.
    private fun getDayInMonth(): Int {
        return if (month == 2) 28 + if (isLeapYear()) 1 else 0 else 31 - (month - 1) % 7 % 2
    }

    fun getCANCHI(): String? {
        return CAN[(year + 6) % 10] + " " + CHI[(year + 8) % 12]
    }

    private fun getMOD(x: Int, y: Int): Int {
        var z = x - (y * floor(x.toDouble() / y)).toInt()
        if (z == 0) {
            z = y
        }
        return z
    }

    private fun toInt(d: Double): Int {
        return floor(d).toInt()
    }

    private fun getJulius(): Double {
        val res: Double
        val a = (14 - month) / 12
        val y = year + 4800 - a
        val m = month + 12 * a - 3
        res = day + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400 - 32045.toDouble()
        return res
    }

    fun getUniversalFromJulius(JD: Double): IntArray? {
        val a: Int
        val alpha: Int
        val b: Int
        val c: Int
        val d: Int
        val e: Int
        val dd: Int
        val mm: Int
        val yyyy: Int
        val f: Double
        val z: Int = toInt(JD + 0.5)
        f = JD + 0.5 - z
        if (z < 2299161) {
            a = z
        } else {
            alpha = toInt((z - 1867216.25) / 36524.25)
            a = z + 1 + alpha - toInt(alpha.toDouble() / 4)
        }
        b = a + 1524
        c = toInt((b - 122.1) / 365.25)
        d = toInt(365.25 * c)
        e = toInt((b - d) / 30.6001)
        dd = toInt(b - d - toInt(30.6001 * e) + f)
        mm = if (e < 14) {
            e - 1
        } else {
            e - 13
        }
        yyyy = if (mm < 3) {
            c - 4715
        } else {
            c - 4716
        }
        return intArrayOf(dd, mm, yyyy)
    }

    private fun getSunLongitude(jdn: Double): Double {
        val t = (jdn - 2451545.0) / 36525
        val t2 = t * t
        val dr = PI / 180
        val m = 357.52910 + 35999.05030 * t - 0.0001559 * t2 - 0.00000048 * t * t2
        val l0 = 280.46645 + 36000.76983 * t + 0.0003032 * t2
        var dl = (1.914600 - 0.004817 * t - 0.000014 * t2) * sin(dr * m)
        dl += (0.019993 - 0.000101 * t) * sin(dr * 2 * m) + 0.000290 * sin(dr * 3 * m)

        var l = l0 + dl
        l *= dr
        l -= PI * 2 * toInt(l / (PI * 2))

        return l
    }

    private fun getLunarMonth11(Y: Int): IntArray {
        val off = localToJD(31, 12, Y) - 2415021.076998695
        val k: Int = toInt(off / 29.530588853)
        var jd = getNewMoon(k)
        val ret = getLocalFromJD(jd)
        val sunLong = getSunLongitude(
            localToJD(ret[0], ret[1], ret[2])
        ) // sun longitude at local midnight
        if (sunLong > 3 * PI / 2) {
            jd = getNewMoon(k - 1)
        }
        return getLocalFromJD(jd)
    }

    private fun getUniversalFromJD(JD: Double): IntArray {
        val a: Int
        val alpha: Int
        val b: Int
        val c: Int
        val d: Int
        val e: Int
        val dd: Int
        val mm: Int
        val yyyy: Int
        val f: Double
        val z: Int = toInt(JD + 0.5)
        f = JD + 0.5 - z
        if (z < 2299161) {
            a = z
        } else {
            alpha = toInt((z - 1867216.25) / 36524.25)
            a = z + 1 + alpha - toInt(alpha.toDouble() / 4)
        }
        b = a + 1524
        c = toInt((b - 122.1) / 365.25)
        d = toInt(365.25 * c)
        e = toInt((b - d) / 30.6001)
        dd = toInt(b - d - toInt(30.6001 * e) + f)
        mm = if (e < 14) {
            e - 1
        } else {
            e - 13
        }
        yyyy = if (mm < 3) {
            c - 4715
        } else {
            c - 4716
        }
        return intArrayOf(dd, mm, yyyy)
    }

    private fun getLocalFromJD(JD: Double): IntArray {
        return getUniversalFromJD(JD + TIMEZONE / 24.0)
    }

    private fun getNewMoon(k: Int): Double {
        val t = k / 1236.85
        val t2 = t * t
        val t3 = t2 * t
        val dr = PI / 180
        var jd1 = 2415020.75933 + 29.53058868 * k + 0.0001178 * t2 - 0.000000155 * t3
        jd1 += 0.00033 * sin((166.56 + 132.87 * t - 0.009173 * t2) * dr)
        val m = 359.2242 + 29.10535608 * k - 0.0000333 * t2 - 0.00000347 * t3
        val mpr = 306.0253 + 385.81691806 * k + 0.0107306 * t2 + 0.00001236 * t3
        val f = 21.2964 + 390.67050646 * k - 0.0016528 * t2 - 0.00000239 * t3
        var c1 = (0.1734 - 0.000393 * t) * sin(m * dr) + 0.0021 * sin(2 * dr * m)
        c1 = c1 - 0.4068 * sin(mpr * dr) + 0.0161 * sin(dr * 2 * mpr)
        c1 -= 0.0004 * sin(dr * 3 * mpr)
        c1 = c1 + 0.0104 * sin(dr * 2 * f) - 0.0051 * sin(dr * (m + mpr))
        c1 = c1 - 0.0074 * sin(dr * (m - mpr)) + 0.0004 * sin(dr * (2 * f + m))
        c1 = c1 - 0.0004 * sin(dr * (2 * f - m)) - 0.0006 * sin(dr * (2 * f + mpr))
        c1 += 0.0010 * sin(dr * (2 * f - mpr)) + 0.0005 * sin(dr * (2 * mpr + m))

        val delta: Double
        delta = if (t < -11) {
            0.001 + 0.000839 * t + 0.0002261 * t2 - 0.00000845 * t3 - 0.000000081 * t * t3
        } else {
            -0.000278 + 0.000265 * t + 0.000262 * t2
        }

        return jd1 + c1 - delta
    }

    private fun getLunarYear(Y: Int): Array<IntArray> {
        val ret: Array<IntArray>
        val month11A = getLunarMonth11(Y - 1)
        val jdMonth11A = localToJD(month11A[0], month11A[1], month11A[2])
        val k = floor(0.5 + (jdMonth11A - 2415021.076998695) / 29.530588853).toInt()
        val month11B = getLunarMonth11(Y)
        val off = localToJD(month11B[0], month11B[1], month11B[2]) - jdMonth11A
        val leap = off > 365.0
        ret = if (!leap) {
            Array(13) { IntArray(5) }
        } else {
            Array(14) { IntArray(5) }
        }
        ret[0] = intArrayOf(month11A[0], month11A[1], month11A[2], 0, 0)
        ret[ret.size - 1] = intArrayOf(month11B[0], month11B[1], month11B[2], 0, 0)
        for (i in 1 until ret.size - 1) {
            val nm = getNewMoon(k + i)
            val a = getLocalFromJD(nm)
            ret[i] = intArrayOf(a[0], a[1], a[2], 0, 0)
        }
        for (i in ret.indices) {
            ret[i][3] = getMOD(i + 11, 12)
        }
        if (leap) {
            initLeapYear(ret)
        }
        return ret
    }

    private fun initLeapYear(ret: Array<IntArray>) {
        val sunLongitudes = DoubleArray(ret.size)
        for (i in ret.indices) {
            val a = ret[i]
            val jdAtMonthBegin = localToJD(a[0], a[1], a[2])
            sunLongitudes[i] = getSunLongitude(jdAtMonthBegin)
        }
        var found = false
        for (i in ret.indices) {
            if (found) {
                ret[i][3] = getMOD(i + 10, 12)
                continue
            }
            val sl1 = sunLongitudes[i]
            val sl2 = sunLongitudes[i + 1]
            val hasMajorTerm =
                floor(sl1 / PI * 6) != floor(sl2 / PI * 6)
            if (!hasMajorTerm) {
                found = true
                ret[i][4] = 1
                ret[i][3] = getMOD(i + 10, 12)
            }
        }
    }

    private fun universalToJD(D: Int, M: Int, Y: Int): Double {
        val juliusDay: Double
        if (Y > 1582 || (Y == 1582 && M > 10) || (Y == 1582 && M == 10 && D > 14)) {
            juliusDay = (367 * Y - toInt(7 * (Y + toInt((M + 9.toDouble()) / 12)).toDouble() / 4)
                    - toInt(3 * (toInt((Y + (M - 9).toDouble() / 7) / 100) + 1).toDouble() / 4)
                    + toInt(275 * M.toDouble() / 9) + D + 1721028.5)
        } else {
            juliusDay =
                367 * Y - toInt(7 * (Y + 5001 + toInt((M - 9).toDouble() / 7)).toDouble() / 4).toDouble()
            +toInt(275 * M.toDouble() / 9) + D + 1729776.5
        }
        return juliusDay
    }

    private fun localToJD(D: Int, M: Int, Y: Int): Double {
        return universalToJD(D, M, Y) - TIMEZONE / 24.0
    }

    private fun getSunLongitudeAstronomical(d: Double): Double {
        val t = (d - 2451545.0) / 36525.0
        val t2 = t * t
        val l0 = 280.46645 + 36000.76983 * t + 0.0003032 * t2
        val m = 357.52910 + 35999.05030 * t - 0.0001559 * t2 - t2 * (0.00000048 * t)
        val c = (1.914600 - 0.004817 * t - 0.000014 * t2) * sin(m)
        +(0.01993 - 0.000101 * t) * sin(2 * m) + 0.000290 * sin(3 * m)
        val theta = l0 + c
        var lambda = theta - 0.00569 - 0.00478 * sin(125.04 - 1934.136 * t)
        lambda -= 360 * toInt(lambda / 360)
        return lambda
    }

    private fun getSolarTerm(timeZone: Double): Int {
        val jd = getJulius() + (0 - 12) / 24 + 0 / 1440 + 0 / 86400
        return toInt(getSunLongitudeAstronomical(jd - timeZone / 24.0 * 24 * 60 * 60))
    }

    // Chuyen doi duong lich sang am lich.
    fun getLunar(): IntArray {
        var yy = year
        var ly: Array<IntArray> = getLunarYear(year)
        val month11 = ly[ly.size - 1]
        val jdToday: Double = localToJD(day, month, year)
        val jdMonth11: Double = localToJD(month11[0], month11[1], month11[2])
        if (jdToday >= jdMonth11) {
            ly = getLunarYear(year + 1)
            yy = year + 1
        }
        var i = ly.size - 1
        while (jdToday < localToJD(ly[i][0], ly[i][1], ly[i][2])) {
            i--
        }
        val dd = (jdToday - localToJD(ly[i][0], ly[i][1], ly[i][2])).toInt() + 1
        val mm = ly[i][3]
        if (mm >= 11) {
            yy--
        }
        return intArrayOf(dd, mm, yy, ly[i][4])
    }

    fun getTIETKHI(): String {
        val k = getSolarTerm(TIMEZONE) / 15
        var result = "Tiết khí: " + TIETKHI[k] + " - "
        result += if (k == 23)
            TIETKHI[0]
        else
            TIETKHI[k + 1]
        return result
    }

    fun isToday(day: LunaCalendar): Boolean {
        val c1: Calendar = Calendar.getInstance()
        val c2: Calendar = Calendar.getInstance()
        c1.set(day.year, day.month - 1, day.day)

        return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH))
    }
}