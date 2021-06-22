package com.airbnb.common.utils

import com.fasterxml.jackson.databind.ObjectMapper
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import kotlin.math.floor

/**
 * @project IntelliJ IDEA
 * @author mir00r on 22/6/21
 */
class AppUtil {

    companion object {

        private const val usernameRegex = "^[a-zA-Z0-9_@\\.-]{4,}\$"
        private const val phoneNumberRegexBD = "\\+?(88)?0?1[56789][0-9]{8}\\b" // this is for bangladeshi phone number
        private const val phoneNumberRegex = "^\\+(?:[0-9]●?){6,14}[0-9]\$"
        private const val emailRegex = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})\$"

        @JvmStatic
        fun isEmailValid(email: String): Boolean {
            return Pattern.compile(emailRegex).matcher(email).matches()
        }

        @JvmStatic
        fun isUsernameValid(username: String): Boolean {
            return Pattern.compile(usernameRegex).matcher(username).matches()
        }

        @JvmStatic
        fun isPhoneNumberValid(phoneNumber: String): Boolean {
            return Pattern.compile(phoneNumberRegex).matcher(phoneNumber).matches()
        }

        @JvmStatic
        fun getRequestDate(date: Long?, anotherDate: Long?): Date {
            if (anotherDate != null)
                return Date(anotherDate)
            else if (date == null) return Date()
            else return Date(date)
        }

        @JvmStatic
        fun roundOffDecimal(number: Double): Double {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            return df.format(number).toDouble()
        }

        @JvmStatic
        fun roundOffDecimal(number: Double, pattern: String): Double {
            val df = DecimalFormat(pattern)
            df.roundingMode = RoundingMode.CEILING
            return df.format(number).toDouble()
        }

        @JvmStatic
        fun getPercentage(total: Long, totalServed: Long): Double {
            return if (total == 0L) 0.00
            else {
                val tot = total.toDouble()
                val tots = totalServed.toDouble()
                val value = (tots * 100) / tot
                this.roundOffDecimal(value)
            }
        }

        @JvmStatic
        fun centimeterToFtAndInches(cmValue: Double): String {
            val ftInchesValue = StringBuilder()
            val length = cmValue / 2.54
            val feet = floor(length / 12)
            val inch = length - 12 * feet
            ftInchesValue.append(feet.toInt()).append(" ft").append(" ").append(this.roundOffDecimal(inch, "#.#"))
                .append(" in")
            return ftInchesValue.toString()
        }

        @JvmStatic
        fun getFormattedPhoneNumber(phone: String): String {
            if (phone.startsWith("+88")) return phone.replace("+88", "")
            return phone
        }

        @JvmStatic
        fun getAgeDays(year: Long, isLeapYear: Boolean, days: Long): Long {
            if (isLeapYear)
                return (365 * year) + 1
            else if (days > 0) return (365 * year) + days
            return 365 * year
        }

        @JvmStatic
        fun getRemainingTime(createdTime: Date, hour: Int): Long {
            val totalTime = DateUtil.addHours(createdTime, hour)
            val diff = totalTime.time - Date().time

            // Calculate time difference in seconds,
            // minutes, hours, years, and days
            val differenceInSeconds = (TimeUnit.MILLISECONDS.toSeconds(diff) % 60)
            if (differenceInSeconds < 0) return 0
            return diff
        }

        @JvmStatic
        fun getRemainingFull(createdTime: Date, hour: Int): String {
            val totalTime = DateUtil.addHours(createdTime, hour)
            return DateUtil.findDifference(totalTime, Date())
        }
    }
}
