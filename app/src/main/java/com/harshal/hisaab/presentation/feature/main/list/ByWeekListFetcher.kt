package com.harshal.hisaab.presentation.feature.main.list

import androidx.lifecycle.MutableLiveData
import com.harshal.hisaab.MainActivity
import com.harshal.hisaab.domain.BaseUseCase
import com.harshal.hisaab.domain.FailureException
import com.harshal.hisaab.domain.room.WeeklySumEntity
import com.harshal.hisaab.usecases.spending.FetchByWeeksUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch

class ByWeekListFetcher(
    private val _viewModelScope: CoroutineScope,
    private var _job: Job?,
    private val _spendList: MutableLiveData<List<WeeklySumEntity>>,
    private val _fetchByWeeksUseCase: FetchByWeeksUseCase,
    private val _handleFailure: (FailureException) -> Unit,
    private var _totalMonthly: MutableLiveData<Float>
) : Clearable {

    companion object {
        @Volatile
        private var INSTANCE: ByWeekListFetcher? = null
        fun get(
            viewModelScope: CoroutineScope,
            job: Job?,
            spendList: MutableLiveData<List<WeeklySumEntity>>,
            fetchByWeeksUseCase: FetchByWeeksUseCase,
            handle_failure: (FailureException) -> Unit,
            totalMonthly: MutableLiveData<Float>
        ): ByWeekListFetcher {
            return INSTANCE ?: synchronized(this) {
                val instance = ByWeekListFetcher(
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
            list.cancellable().collect { WeeklyDebitEntity ->
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