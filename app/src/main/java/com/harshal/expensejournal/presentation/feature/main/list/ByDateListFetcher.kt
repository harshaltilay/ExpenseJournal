

package com.harshal.expensejournal.presentation.feature.main.list

import androidx.lifecycle.MutableLiveData
import com.harshal.expensejournal.domain.FailureException
import com.harshal.expensejournal.domain.getFriendlyDateTime
import com.harshal.expensejournal.domain.room.DebitEntity
import com.harshal.expensejournal.domain.room.SpendingEntity
import com.harshal.expensejournal.usecases.spending.FetchByDateUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import java.util.*

class ByDateListFetcher(
    private val _viewModelScope: CoroutineScope,
    private var _job: Job?,
    private val _spendList: MutableLiveData<List<DebitEntity>>,
    private val _fetchByDateUseCase: FetchByDateUseCase,
    private val _handleFailure: (FailureException) -> Unit
) : Clearable {

    companion object {
        @Volatile
        private var INSTANCE: ByDateListFetcher? = null
        fun get(
            viewModelScope: CoroutineScope,
            job: Job?,
            spendList: MutableLiveData<List<DebitEntity>>,
            fetchByDateUseCase: FetchByDateUseCase,
            handle_failure: (FailureException) -> Unit
        ): ByDateListFetcher {
            return INSTANCE ?: synchronized(this) {
                val instance = ByDateListFetcher(
                    viewModelScope, job, spendList, fetchByDateUseCase, handle_failure
                )
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }


    fun fetch(date: Date) {
        _fetchByDateUseCase(date, _viewModelScope) {
            it.fold(
                _handleFailure, ::handleList
            )
        }
    }

    private fun handleList(list: Flow<List<SpendingEntity>>) {

        _job = _viewModelScope.launch {
            list.cancellable().collect { it ->
                val newList = arrayListOf<DebitEntity>()
                it.forEach {
                    val entity = DebitEntity(
                        type = 0,
                        sid = it.sid,
                        title = it.desc,
                        time = String.getFriendlyDateTime(it.time),
                        touser = it.touser,
                        amount = it.amount.toString(),
                        category = it.category,
                        limit = it.amount.toString()
                    )
                    newList.add(entity)
                }
                _spendList.postValue(newList.toList())
            }

        }
    }

    override fun clear() {
        _job?.cancel()
        _spendList.postValue(listOf())
        INSTANCE = null
    }
}