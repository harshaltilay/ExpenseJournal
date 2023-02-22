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


package com.harshal.hisaab.usecases.spending

import com.harshal.hisaab.data.spending.SpendingRepo
import com.harshal.hisaab.domain.BaseUseCase
import com.harshal.hisaab.domain.BaseUseCase.None
import com.harshal.hisaab.domain.Either
import com.harshal.hisaab.domain.FailureException
import com.harshal.hisaab.domain.room.DailySumEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SpendingHistoryUseCase
@Inject constructor(private val _spendingRepo: SpendingRepo) :
    BaseUseCase<Flow<List<DailySumEntity>>, None>() {

    override suspend fun run(params: None): Either<FailureException, Flow<List<DailySumEntity>>> =
        _spendingRepo.spendingByDays(params)

}
