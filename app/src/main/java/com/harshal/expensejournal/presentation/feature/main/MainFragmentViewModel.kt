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


package com.harshal.expensejournal.presentation.feature.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.harshal.expensejournal.domain.room.DailySumEntity
import com.harshal.expensejournal.domain.room.MonthlySumEntity
import com.harshal.expensejournal.domain.room.SpendingEntity
import com.harshal.expensejournal.domain.room.WeeklySumEntity
import com.harshal.expensejournal.framework.android.platform.BaseViewModel
import com.harshal.expensejournal.presentation.feature.main.list.ByDaysListFetcher
import com.harshal.expensejournal.presentation.feature.main.list.ByMonthListFetcher
import com.harshal.expensejournal.presentation.feature.main.list.ByWeekListFetcher
import com.harshal.expensejournal.presentation.feature.main.list.SpendingListFetcher
import com.harshal.expensejournal.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import java.util.*
import javax.inject.Inject


@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val _addSpendingUseCase: AddSpendingUseCase,
    private val _spendingUseCase: SpendingUseCase,
    private val _fetchByMonthsUseCase: FetchByMonthsUseCase,
    private val _fetchByWeeksUseCase: FetchByWeeksUseCase,
    private val _spendingHistoryUseCase: SpendingHistoryUseCase,
    private val _updateSpendingUseCase: UpdateSpendingUseCase
) : BaseViewModel() {

    private val _spendingByMonthList: MutableLiveData<List<MonthlySumEntity>> = MutableLiveData()
    val spendingByMonthList: LiveData<List<MonthlySumEntity>> = _spendingByMonthList
    private var _monthListJob: Job? = null
    private var _byMonthListFetcher: ByMonthListFetcher? = null
    private val _totalYearly: MutableLiveData<Float> = MutableLiveData()
    val totalYearly: MutableLiveData<Float> = _totalYearly

    private val _spendingByWeekList: MutableLiveData<List<WeeklySumEntity>> = MutableLiveData()
    val spendingByWeekList: LiveData<List<WeeklySumEntity>> = _spendingByWeekList
    private var _weekListJob: Job? = null
    private var _byWeekListFetcher: ByWeekListFetcher? = null
    private val _totalMonthly: MutableLiveData<Float> = MutableLiveData()
    val totalMonthly: MutableLiveData<Float> = _totalMonthly

    private val _spendingByDayList: MutableLiveData<List<DailySumEntity>> = MutableLiveData()
    val spendingByDayList: LiveData<List<DailySumEntity>> = _spendingByDayList
    private var _daysListJob: Job? = null
    private var _byDaysListFetcher: ByDaysListFetcher? = null

    private val _spendingInfoEntityList: MutableLiveData<List<SpendingEntity>> = MutableLiveData()
    val spendingInfoEntityList: LiveData<List<SpendingEntity>> = _spendingInfoEntityList

    private var _dateListJob: Job? = null
    private var _spendingListFetcher: SpendingListFetcher? = null

    fun beginFlow() {
        _byMonthListFetcher?.clear()
        _byMonthListFetcher = ByMonthListFetcher.get(
            viewModelScope,
            _monthListJob,
            _spendingByMonthList,
            _fetchByMonthsUseCase,
            handleFailure,
            totalYearly
        )
        _byMonthListFetcher?.fetch()

        _byWeekListFetcher?.clear()
        _byWeekListFetcher = ByWeekListFetcher.get(
            viewModelScope,
            _weekListJob,
            _spendingByWeekList,
            _fetchByWeeksUseCase,
            handleFailure,
            totalMonthly
        )
        _byWeekListFetcher?.fetch()

        _byDaysListFetcher?.clear()
        _byDaysListFetcher = ByDaysListFetcher.get(
            viewModelScope, _daysListJob, _spendingByDayList, _spendingHistoryUseCase, handleFailure
        )
        _byDaysListFetcher?.fetch()


        _spendingListFetcher?.clear()
        _spendingListFetcher = SpendingListFetcher.get(
            viewModelScope,
            _dateListJob,
            _spendingInfoEntityList,
            _spendingUseCase,
            handleFailure
        )
    }

    fun fetchByDate(date: Date) {
        _spendingListFetcher?.fetch(date)
    }

    fun endFlow() {
        _byMonthListFetcher?.clear()
        _byWeekListFetcher?.clear()
        _byDaysListFetcher?.clear()
        _spendingListFetcher?.clear()
    }

    fun addSpending(spendingEntity: SpendingEntity) =
        _addSpendingUseCase(spendingEntity, viewModelScope) {
            it.fold(handleFailure) {}
        }

    fun updateSpendingDescription(desc: String, category: Int, sid: Int) {
        _updateSpendingUseCase(Triple(desc, category, sid))
    }
}

