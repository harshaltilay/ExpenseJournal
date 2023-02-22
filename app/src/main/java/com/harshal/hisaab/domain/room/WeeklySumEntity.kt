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


package com.harshal.hisaab.domain.room

import android.content.Context
import android.text.Spanned
import com.harshal.hisaab.R
import com.harshal.hisaab.domain.getWeekOfMonth
import com.harshal.hisaab.domain.html
import com.harshal.hisaab.domain.inCurrency
import java.util.*

data class WeeklySumEntity(
    val amount: Float, var maxlimit: Float = 0f, val time: Date
) {
    fun getFriendlyAmount(): Spanned {
        return String.html(
            if (amount > maxlimit) {
                "<font color='#FF2E63'>${String.inCurrency(amount)}</b>"
            } else {
                "<font color='#002c56'>${String.inCurrency(amount)}</b>"
            }
        )
    }

    fun getFriendlyDate(context: Context): Spanned {
        return String.html(
            context.getString(
                R.string.string_friendly_week, String.getWeekOfMonth(time)
            )
        )
    }

}
