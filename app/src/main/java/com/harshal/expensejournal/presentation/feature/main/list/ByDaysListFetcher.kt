package com.harshal.expensejournal.presentation.feature.main.list

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

class ByDaysListFetcher(
    private val _viewModelScope: CoroutineScope,
    private var _job: Job?,
    private val _spendingHistoryList: MutableLiveData<List<DailySumEntity>>,
    private val _fetchByDaysUseCase: FetchByDaysUseCase,
    private val _handleFailure: (FailureException) -> Unit
) : Clearable {

    companion object {
        @Volatile
        private var INSTANCE: ByDaysListFetcher? = null
        fun get(
            viewModelScope: CoroutineScope,
            job: Job?,
            spendingHistoryList: MutableLiveData<List<DailySumEntity>>,
            fetchByDaysUseCase: FetchByDaysUseCase,
            handle_failure: (FailureException) -> Unit
        ): ByDaysListFetcher {
            return INSTANCE ?: synchronized(this) {
                val instance = ByDaysListFetcher(
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