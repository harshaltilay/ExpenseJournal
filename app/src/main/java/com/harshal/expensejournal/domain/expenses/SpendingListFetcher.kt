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
import com.harshal.expensejournal.domain.FailureException
import com.harshal.expensejournal.domain.room.SpendingEntity
import com.harshal.expensejournal.usecases.FetchSpendingOnDateUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

class SpendingListFetcher(
    private val _viewModelScope: CoroutineScope,
    private var _job: Job?,
    private val _spendList: MutableLiveData<List<SpendingEntity>>,
    private val _Fetch_spendingUseCase: FetchSpendingOnDateUseCase,
    private val _handleFailure: (FailureException) -> Unit
) : Clearable {

    companion object {
        @Volatile
        private var INSTANCE: SpendingListFetcher? = null
        fun get(
            viewModelScope: CoroutineScope,
            job: Job?,
            spendList: MutableLiveData<List<SpendingEntity>>,
            fetchSpendingOnDateUseCase: FetchSpendingOnDateUseCase,
            handle_failure: (FailureException) -> Unit
        ): SpendingListFetcher {
            return INSTANCE ?: synchronized(this) {
                val instance = SpendingListFetcher(
                    viewModelScope, job, spendList, fetchSpendingOnDateUseCase, handle_failure
                )
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }


    fun fetch(date: Date) {
        _Fetch_spendingUseCase(date, _viewModelScope) {
            it.fold(
                _handleFailure, ::handleList
            )
        }
    }

    private fun handleList(list: Flow<List<SpendingEntity>>) {
        _job = _viewModelScope.launch {
            list.cancellable().collectLatest {
                _spendList.postValue(it)
            }
        }
    }

    override fun clear() {
        _job?.cancel()
        _spendList.postValue(listOf())
        INSTANCE = null
    }
}