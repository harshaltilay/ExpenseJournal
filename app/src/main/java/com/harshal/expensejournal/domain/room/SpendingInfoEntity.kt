package com.harshal.expensejournal.domain.room

import android.content.Context
import android.text.Spanned
import com.harshal.expensejournal.R
import com.harshal.expensejournal.domain.html
import com.harshal.expensejournal.domain.inCurrency

data class SpendingInfoEntity(
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
                R.string.friendly_date_and_user, splitString[3] + " " + splitString[5], touser
            )
        )
    }

    fun getFriendlyAmount(context: Context): Spanned {
        return String.html(
            context.getString(
                R.string.string_amount_safe, String.inCurrency(amount.toFloat())
            )
        )
    }
}