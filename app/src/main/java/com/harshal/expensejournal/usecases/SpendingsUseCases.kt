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
package com.harshal.expensejournal.usecases

import com.harshal.expensejournal.data.spending.SpendingRepo
import com.harshal.expensejournal.domain.BaseUseCase
import com.harshal.expensejournal.domain.Either
import com.harshal.expensejournal.domain.FailureException
import com.harshal.expensejournal.domain.room.DailySumEntity
import com.harshal.expensejournal.domain.room.MonthlySumEntity
import com.harshal.expensejournal.domain.room.SpendingEntity
import com.harshal.expensejournal.domain.room.WeeklySumEntity
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class AddSpendingUseCase
@Inject constructor(private val _spendingRepo: SpendingRepo) :
    BaseUseCase<BaseUseCase.None, SpendingEntity>() {

    override suspend fun run(params: SpendingEntity): Either<FailureException, None> =
        _spendingRepo.addSpending(params)

}

class FetchByMonthsUseCase
@Inject constructor(private val _spendingRepo: SpendingRepo) :
    BaseUseCase<Flow<List<MonthlySumEntity>>, BaseUseCase.None>() {

    override suspend fun run(params: None): Either<FailureException, Flow<List<MonthlySumEntity>>> =
        _spendingRepo.spendingByMonths(params)

}

class FetchByWeeksUseCase
@Inject constructor(private val _spendingRepo: SpendingRepo) :
    BaseUseCase<Flow<List<WeeklySumEntity>>, BaseUseCase.None>() {

    override suspend fun run(params: None): Either<FailureException, Flow<List<WeeklySumEntity>>> =
        _spendingRepo.spendingByWeeks(params)

}


class SpendingUseCase
@Inject constructor(private val _spendingRepo: SpendingRepo) :
    BaseUseCase<Flow<List<SpendingEntity>>, Date>() {

    override suspend fun run(params: Date): Either<FailureException, Flow<List<SpendingEntity>>> =
        _spendingRepo.fetchSpending(params)

}

class SpendingHistoryUseCase
@Inject constructor(private val _spendingRepo: SpendingRepo) :
    BaseUseCase<Flow<List<DailySumEntity>>, BaseUseCase.None>() {

    override suspend fun run(params: None): Either<FailureException, Flow<List<DailySumEntity>>> =
        _spendingRepo.spendingByDays(params)

}


class UpdateSpendingUseCase
@Inject constructor(private val _spendingRepo: SpendingRepo) :
    BaseUseCase<BaseUseCase.None, Triple<String, Int, Int>>() {

    override suspend fun run(params: Triple<String, Int, Int>): Either<FailureException, None> =
        _spendingRepo.updateSpending(params)

}




