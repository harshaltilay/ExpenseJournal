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


package com.harshal.hisaab.framework.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.harshal.hisaab.data.spending.SpendingDao
import com.harshal.hisaab.domain.room.SpendingEntity

@Database(
    entities = [SpendingEntity::class], version = 1, exportSchema = false
)
@TypeConverters(RoomDateTypeConverter::class)
abstract class RoomDBFramework : RoomDatabase() {
    abstract fun spentDao(): SpendingDao

    companion object {
        private const val DATABASE_NAME = "spending.db"

        @Volatile
        private var INSTANCE: RoomDBFramework? = null
        fun getDatabase(context: Context): RoomDBFramework {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, RoomDBFramework::class.java, DATABASE_NAME
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}