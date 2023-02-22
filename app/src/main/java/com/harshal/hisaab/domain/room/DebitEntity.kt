package com.harshal.hisaab.domain.room

import android.content.Context
import android.text.Spanned
import com.harshal.hisaab.R
import com.harshal.hisaab.domain.html
import com.harshal.hisaab.domain.inCurrency

data class DebitEntity(
    val type: Int,
    val sid: Int,
    val title: String,
    val time: String,
    val touser: String,
    val amount: String,
    var limit: String = "0",
    val category: Int = 3
) {
    fun getFriendlyTitle(): Spanned {
        return String.html(
            title
        )
    }
    fun getFriendlyDate(context: Context): Spanned {
        val splitString = time.split(' ')
        return String.html(
            context.getString(
                R.string.today_friendly_date,
                splitString[3] + " " + splitString[5],
                "<b>To:</b> $touser"
            )
        )
    }

    fun getFriendlyAmount(): Spanned {
        return String.html(
            if (amount.toFloat() > limit.toFloat()) {
                "<font color='#FF2E63'>${String.inCurrency(amount.toFloat())}</b>"
            } else {
                "<font color='#002c56'>${String.inCurrency(amount.toFloat())}</b>"
            }
        )
    }
}