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
import com.harshal.expensejournal.domain.room.DailySumEntity
import com.harshal.expensejournal.usecases.FetchByDaysUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ByDays(
    private val _viewModelScope: CoroutineScope,
    private var _job: Job?,
    private val _spendingHistoryList: MutableLiveData<List<DailySumEntity>>,
    private val _fetchByDaysUseCase: FetchByDaysUseCase,
    private val _handleFailure: (FailureException) -> Unit
) : Clearable {

    companion object {
        @Volatile
        private var INSTANCE: ByDays? = null
        fun get(
            viewModelScope: CoroutineScope,
            job: Job?,
            spendingHistoryList: MutableLiveData<List<DailySumEntity>>,
            fetchByDaysUseCase: FetchByDaysUseCase,
            handle_failure: (FailureException) -> Unit
        ): ByDays {
            return INSTANCE ?: synchronized(this) {
                val instance = ByDays(
                    viewModelScope, job, spendingHistoryList, fetchByDaysUseCase, handle_failure
                )
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    fun fetch() {
        _fetchByDaysUseCase(BaseUseCase.None()) { either ->
            either.fold(_handleFailure, ::handleList)
        }
    }

    private fun handleList(list: Flow<List<DailySumEntity>>) {
        _job = _viewModelScope.launch {
            list.cancellable().collectLatest { dailySumEntity ->
                val newList = arrayListOf<DailySumEntity>()
                dailySumEntity.forEach {
                    it.maxlimit = MainActivity.curUser!!.dailyMax
                    newList.add(it)
                }
                _spendingHistoryList.postValue(newList.toList())
            }
        }
    }


    override fun clear() {
        _job?.cancel()
        _spendingHistoryList.postValue(listOf())
        INSTANCE = null
    }
}