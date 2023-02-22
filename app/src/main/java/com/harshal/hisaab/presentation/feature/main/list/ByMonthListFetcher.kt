package com.harshal.hisaab.presentation.feature.main.list

import androidx.lifecycle.MutableLiveData
import com.harshal.hisaab.domain.BaseUseCase
import com.harshal.hisaab.domain.FailureException
import com.harshal.hisaab.domain.room.MonthlySumEntity
import com.harshal.hisaab.domain.user.UserProfileEntity
import com.harshal.hisaab.usecases.spending.FetchByMonthsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch

class ByMonthListFetcher(
    private val _viewModelScope: CoroutineScope,
    private var _job: Job?,
    private val _monthList: MutableLiveData<List<MonthlySumEntity>>,
    private val _fetchByMonthsUseCase: FetchByMonthsUseCase,
    private val _handleFailure: (FailureException) -> Unit,
    private var _userProfileEntity: UserProfileEntity,
    private var _totalYearly: MutableLiveData<Float>
) : Clearable {

    companion object {
        @Volatile
        private var INSTANCE: ByMonthListFetcher? = null
        fun get(
            viewModelScope: CoroutineScope,
            job: Job?,
            _spendList: MutableLiveData<List<MonthlySumEntity>>,
            fetchByMonthsUseCase: FetchByMonthsUseCase,
            handle_failure: (FailureException) -> Unit,
            userProfileEntity: UserProfileEntity,
            totalYearly: MutableLiveData<Float>
        ): ByMonthListFetcher {
            return INSTANCE ?: synchronized(this) {
                val instance = ByMonthListFetcher(
                    viewModelScope,
                    job,
                    _spendList,
                    fetchByMonthsUseCase,
                    handle_failure,
                    userProfileEntity,
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
            list.cancellable().collect { monthlyDebitEntity ->
                val newList = arrayListOf<MonthlySumEntity>()
                var totalCount = 0f
                monthlyDebitEntity.forEach {
                    it.maxlimit = _userProfileEntity.monthlyMax
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