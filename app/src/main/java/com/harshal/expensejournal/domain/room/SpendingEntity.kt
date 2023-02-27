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
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.harshal.expensejournal.R
import com.harshal.expensejournal.domain.getFriendlyDateTime
import com.harshal.expensejournal.domain.html
import com.harshal.expensejournal.domain.inCurrency
import java.util.*

@Entity(tableName = "spending")
data class SpendingEntity(
    @PrimaryKey(autoGenerate = true) val sid: Int = 0,
    @ColumnInfo(name = "amount") val amount: Float,
    @ColumnInfo(name = "touser") val touser: String,
    @ColumnInfo(name = "desc") val desc: String,
    @ColumnInfo(name = "category") val category: Int,
    @ColumnInfo(name = "time", defaultValue = "CURRENT_TIMESTAMP") val time: Date
) {
    fun getFriendlyTitle(): Spanned {
        return String.html(
            desc
        )
    }

    fun getFriendlyDate(context: Context): Spanned {
        return String.getFriendlyDateTime(time).split(' ').let {
            String.html(
                context.getString(
                    R.string.friendly_date_and_user, it[3] + " " + it[5], touser
                )
            )
        }

    }

    fun getFriendlyAmount(context: Context): Spanned {
        return String.html(
            context.getString(
                R.string.string_amount_safe, String.inCurrency(amount)
            )
        )
    }
}

//category
//R.id.essential -> 1
//R.id.casual -> 2
//R.id.anxiety -> 3