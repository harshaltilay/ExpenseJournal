package com.harshal.hisaab.presentation.feature.main.list

import androidx.lifecycle.MutableLiveData
import com.harshal.hisaab.MainActivity
import com.harshal.hisaab.domain.BaseUseCase
import com.harshal.hisaab.domain.FailureException
import com.harshal.hisaab.domain.room.DailySumEntity
import com.harshal.hisaab.usecases.spending.SpendingHistoryUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch

class ByDaysListFetcher(
    private val _viewModelScope: CoroutineScope,
    private var _job: Job?,
    private val _spendingHistoryList: MutableLiveData<List<DailySumEntity>>,
    private val _spendingHistoryUseCase: SpendingHistoryUseCase,
    private val _handleFailure: (FailureException) -> Unit
) : Clearable {

    companion object {
        @Volatile
        private var INSTANCE: ByDaysListFetcher? = null
        fun get(
            viewModelScope: CoroutineScope,
            job: Job?,
            spendingHistoryList: MutableLiveData<List<DailySumEntity>>,
            spendingHistoryUseCase: SpendingHistoryUseCase,
            handle_failure: (FailureException) -> Unit
        ): ByDaysListFetcher {
            return INSTANCE ?: synchronized(this) {
                val instance = ByDaysListFetcher(
                    viewModelScope, job, spendingHistoryList, spendingHistoryUseCase, handle_failure
                )
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    fun fetch() {
        _spendingHistoryUseCase(BaseUseCase.None()) { either ->
            either.fold(_handleFailure, ::handleList)
        }
    }

    private fun handleList(list: Flow<List<DailySumEntity>>) {
        _job = _viewModelScope.launch {
            list.cancellable().collect { dailySumEntity ->
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