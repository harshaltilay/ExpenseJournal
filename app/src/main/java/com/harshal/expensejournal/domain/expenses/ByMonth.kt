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
import com.harshal.expensejournal.domain.room.MonthlySumEntity
import com.harshal.expensejournal.usecases.FetchByMonthsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ByMonth(
    private val _viewModelScope: CoroutineScope,
    private var _job: Job?,
    private val _monthList: MutableLiveData<List<MonthlySumEntity>>,
    private val _fetchByMonthsUseCase: FetchByMonthsUseCase,
    private val _handleFailure: (FailureException) -> Unit,
    private var _totalYearly: MutableLiveData<Float>
) : Clearable {

    companion object {
        @Volatile
        private var INSTANCE: ByMonth? = null
        fun get(
            viewModelScope: CoroutineScope,
            job: Job?,
            _spendList: MutableLiveData<List<MonthlySumEntity>>,
            fetchByMonthsUseCase: FetchByMonthsUseCase,
            handle_failure: (FailureException) -> Unit,
            totalYearly: MutableLiveData<Float>
        ): ByMonth {
            return INSTANCE ?: synchronized(this) {
                val instance = ByMonth(
                    viewModelScope,
                    job,
                    _spendList,
                    fetchByMonthsUseCase,
                    handle_failure,
                    totalYearly
                )
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }


    fun fetch() {
        _fetchByMonthsUseCase(BaseUseCase.None(), _viewModelScope) {
            it.fold(
                _handleFailure, ::handleList
            )
        }
    }

    private fun handleList(list: Flow<List<MonthlySumEntity>>) {
        _job = _viewModelScope.launch {
            list.cancellable().collectLatest { monthlyDebitEntity ->
                val newList = arrayListOf<MonthlySumEntity>()
                var totalCount = 0f
                monthlyDebitEntity.forEach {
                    it.maxlimit = MainActivity.curUser!!.monthlyMax
                    newList.add(it)
                    totalCount += it.amount
                }
                _monthList.postValue(newList.toList())
                _totalYearly.postValue(totalCount)
            }
        }
    }

    override fun clear() {
        _job?.cancel()
        _monthList.postValue(listOf())
        _job = null
        INSTANCE = null
    }
}