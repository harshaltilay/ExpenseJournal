/**
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


package com.harshal.expensejournal.usecases.spending

import com.harshal.expensejournal.data.spending.SpendingRepo
import com.harshal.expensejournal.domain.BaseUseCase
import com.harshal.expensejournal.domain.Either
import com.harshal.expensejournal.domain.FailureException
import com.harshal.expensejournal.domain.room.SpendingEntity
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class FetchDebitsUseCase
@Inject constructor(private val _spendingRepo: SpendingRepo) :
    BaseUseCase<Flow<List<SpendingEntity>>, Date>() {

    override suspend fun run(params: Date): Either<FailureException, Flow<List<SpendingEntity>>> =
        _spendingRepo.fetchDebits(params)

}
