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

package com.harshal.expensejournal.domain.expenses

import androidx.lifecycle.MutableLiveData
import com.harshal.expensejournal.MainActivity
import com.harshal.expensejournal.domain.BaseUseCase
import com.harshal.expensejournal.domain.FailureException
import com.harshal.expensejournal.domain.room.WeeklySumEntity
import com.harshal.expensejournal.usecases.FetchByWeeksUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ByWeek(
    private val _viewModelScope: CoroutineScope,
    private var _job: Job?,
    private val _spendList: MutableLiveData<List<WeeklySumEntity>>,
    private val _fetchByWeeksUseCase: FetchByWeeksUseCase,
    private val _handleFailure: (FailureException) -> Unit,
    private var _totalMonthly: MutableLiveData<Float>
) : Clearable {

    companion object {
        @Volatile
        private var INSTANCE: ByWeek? = null
        fun get(
            viewModelScope: CoroutineScope,
            job: Job?,
            spendList: MutableLiveData<List<WeeklySumEntity>>,
            fetchByWeeksUseCase: FetchByWeeksUseCase,
            handle_failure: (FailureException) -> Unit,
            totalMonthly: MutableLiveData<Float>
        ): ByWeek {
            return INSTANCE ?: synchronized(this) {
                val instance = ByWeek(
                    viewModelScope,
                    job,
                    spendList,
                    fetchByWeeksUseCase,
                    handle_failure,
                    totalMonthly
                )
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }


    fun fetch() {
        _fetchByWeeksUseCase(BaseUseCase.None(), _viewModelScope) {
            it.fold(
                _handleFailure, ::handleList
            )
        }
    }

    private fun handleList(list: Flow<List<WeeklySumEntity>>) {
        _job = _viewModelScope.launch {
            list.cancellable().collectLatest { WeeklyDebitEntity ->
                val newList = arrayListOf<WeeklySumEntity>()
                var totalCount = 0f
                WeeklyDebitEntity.forEach {
                    it.maxlimit = MainActivity.curUser!!.weeklyMax
                    newList.add(it)
                    totalCount += it.amount
                }
                _spendList.postValue(newList.toList())
                _totalMonthly.postValue(totalCount)
            }
        }
    }

    override fun clear() {
        _job?.cancel()
        _spendList.postValue(listOf())
        _job = null
        INSTANCE = null
    }

}