/*
 *
 *  Copyright (C) 2023 Harshal Tilay
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */


package com.harshal.expensejournal.domain

import android.text.Spanned
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.harshal.expensejournal.MainActivity
import java.text.NumberFormat
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, Observer(body))

fun <L : LiveData<FailureException>> LifecycleOwner.failure(
    liveData: L, body: (FailureException?) -> Unit
) = liveData.observe(this, Observer(body))

//fun View.isVisible(): Boolean {
//    return this.visibility == View.VISIBLE
//}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun String.Companion.empty() = ""

fun String.Companion.getFriendlyDateTime(date: Date): String {
    return with(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()) {
        val day = getPostFixedNumber(dayOfMonth)
        val month = month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).uppercase()
        val year = year
        val amPm = if (hour >= 12) " PM" else " AM"

        val hours = hour.let { hour ->
            (if (hour > 12) hour - 12 else hour).let {
                if (it < 10) "0$it" else it
            }
        }
        val minute = minute.let {
            if (it < 10) "0${it}" else it
        }
//    val seconds = localDateTime.second.let {
//        if (it < 10) "0${it}" else it
//    }
        //Example  12 AUG 2020 12:23 PM
        "$day $month $year $hours:$minute $amPm"
    }
}

fun String.Companion.getFriendlyDayAndMonth(date: Date): String {
    return with(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()) {
        "${getPostFixedNumber(dayOfMonth)} ${month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)}"
    }
}

fun String.Companion.getMonth(
    date: Date, upperCase: Boolean = false, full: Boolean = false
): String {
    return with(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()) {
        month.getDisplayName(
            if (full) TextStyle.FULL else TextStyle.SHORT, Locale.getDefault()
        ).let {
            if (upperCase) it.uppercase()
            else it
        }
    }
}

fun String.Companion.getWeekOfMonth(date: Date): String {
    return with(Calendar.getInstance()) {
        time = date
        getPostFixedNumber(this[Calendar.WEEK_OF_MONTH])
    }
}

fun String.Companion.getPostFixedNumber(i: Int): String {
    return with(i) {
        val j = rem(10)
        val k = rem(100)
        when {
            (j == 1 && k != 11) -> {
                return toString() + "st"
            }

            (j == 2 && k != 12) -> {
                return toString() + "nd"
            }

            (j == 3 && k != 13) -> {
                toString() + "rd"
            }
            else -> toString() + "th"
        }
    }
}

fun String.Companion.inCurrency(amount: Float): String {
    return with(amount) {
        val format =
            NumberFormat.getCurrencyInstance(Locale("en", MainActivity.curUser!!.isoCountry))
        format.maximumFractionDigits = 2
        val amnt = format.format(this)
        try {
            if (amnt.split('.')[1].indexOf("00") != -1) amnt.split('.')[0] else amnt
        } catch (e: Exception) {
            amnt
        }
    }
}

fun String.Companion.html(str: String): Spanned {
    return HtmlCompat.fromHtml(
        str, HtmlCompat.FROM_HTML_MODE_COMPACT
    )
}
