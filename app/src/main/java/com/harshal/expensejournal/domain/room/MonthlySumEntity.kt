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


package com.harshal.expensejournal.domain.room

import android.content.Context
import android.text.Spanned
import com.harshal.expensejournal.R
import com.harshal.expensejournal.domain.getMonth
import com.harshal.expensejournal.domain.html
import com.harshal.expensejournal.domain.inCurrency
import java.util.*

data class MonthlySumEntity(
    val amount: Float, var maxlimit: Float = 0f, val time: Date
) {
    fun getFriendlyAmount(context: Context): Spanned {
        return String.html(
            if (amount > maxlimit) {
                context.getString(R.string.string_amount_danger, String.inCurrency(amount))
            } else {
                context.getString(R.string.string_amount_safe, String.inCurrency(amount))
            }
        )
    }

    fun getFriendlyDate(): Spanned {
        return String.html(
            String.getMonth(time, true)
        )
    }

}
