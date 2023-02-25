package com.harshal.expensejournal.data.spending

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.harshal.expensejournal.domain.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface SpendingDao {

    @Insert(onConflict = REPLACE)
    fun addSpending(spendingEntity: SpendingEntity)

    @Query("UPDATE spending SET desc= :desc, category= :category WHERE sid = :sid")
    fun updateSpending(desc: String, category: Int, sid: Int)

    @Query("SELECT * FROM spending WHERE strftime('%d%m%Y',date(time/1000, 'unixepoch', 'localtime')) = strftime('%d%m%Y',date(:today/1000, 'unixepoch', 'localtime')) ORDER BY time DESC")
    fun spendingByDate(today: Long): Flow<List<SpendingEntity>>

    @Query("SELECT time AS time, SUM(amount) as amount, '0' as maxlimit  FROM spending WHERE strftime('%m%Y',date(time/1000, 'unixepoch', 'localtime')) = strftime('%m%Y',date(:month/1000, 'unixepoch', 'localtime'))  GROUP BY  strftime('%W',date(time/1000, 'unixepoch', 'localtime')) ORDER BY time DESC")
    fun weeklySpending(month: Long): Flow<List<WeeklySumEntity>>

    @Query("SELECT time AS time, SUM(amount) as amount, '0' as maxlimit FROM spending WHERE strftime('%Y',date(time/1000, 'unixepoch', 'localtime')) = (strftime('%Y',date(:year/1000, 'unixepoch', 'localtime'))) GROUP BY  strftime('%m',date(:year/1000, 'unixepoch', 'localtime')) ORDER BY time DESC")
    fun monthlySpending(year: Long): Flow<List<MonthlySumEntity>>

    @Query("SELECT DISTINCT time AS time, SUM(amount) as amount, '0' as maxlimit FROM spending  WHERE strftime('%Y',date(time/1000, 'unixepoch', 'localtime')) = strftime('%Y',date(:year/1000, 'unixepoch', 'localtime')) GROUP BY  strftime('%j',date(time/1000, 'unixepoch', 'localtime')) ORDER BY time DESC LIMIT 21")
    fun spendingByDays(year: Long): Flow<List<DailySumEntity>>

}