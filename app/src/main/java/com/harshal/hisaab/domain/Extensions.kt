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


package com.harshal.hisaab.domain

import android.text.Spanned
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
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
    val localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    val day = getPostFixedNumber(localDateTime.dayOfMonth)
    val month = localDateTime.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).uppercase()
    val year = localDateTime.year
    val amPm = if (localDateTime.hour >= 12) " PM" else " AM"
    val hours = localDateTime.hour.let { hour ->
        (if (hour > 12) hour - 12 else hour).run {
            if (this < 10) "0$this" else this
        }
    }
    val minute = localDateTime.minute.let {
        if (it < 10) "0${it}" else it
    }
    val seconds = localDateTime.second.let {
        if (it < 10) "0${it}" else it
    }
    //Example  12 AUG 2020 12:23 PM
    return "$day $month $year $hours:$minute:$seconds $amPm"
}

fun String.Companion.getFriendlyDayAndMonth(date: Date): String {
    val localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    val day = getPostFixedNumber(localDateTime.dayOfMonth)
    val month = localDateTime.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
    return "$day $month"
}

fun String.Companion.getMonth(
    date: Date, upperCase: Boolean = false, full: Boolean = false
): String {
    val localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    val month = localDateTime.month.getDisplayName(
        if (full) TextStyle.FULL else TextStyle.SHORT, Locale.getDefault()
    )
    return if (upperCase) month.uppercase()
    else month
}

fun String.Companion.getWeekOfMonth(date: Date): String {
    val now = Calendar.getInstance()
    now.time = date
    return getPostFixedNumber(now[Calendar.WEEK_OF_MONTH])
}

fun String.Companion.getPostFixedNumber(i: Int): String {
    val j = i.rem(10)
    val k = i.rem(100)
    if (j == 1 && k != 11) {
        return i.toString() + "st"
    }
    if (j == 2 && k != 12) {
        return i.toString() + "nd"
    }
    return if (j == 3 && k != 13) {
        i.toString() + "rd"
    } else i.toString() + "th"
}

fun String.Companion.inCurrency(amount: Float?, country: String = "IN"): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", country))
    format.maximumFractionDigits = 2
    val amnt = format.format(amount)
    return if (amnt.split('.')[1].indexOf("00") != -1) amnt.split('.')[0]
    else amnt
}

fun String.Companion.html(str: String): Spanned {
    return HtmlCompat.fromHtml(
        str, HtmlCompat.FROM_HTML_MODE_COMPACT
    )
}
