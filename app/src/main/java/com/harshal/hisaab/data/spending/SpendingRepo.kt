package com.harshal.hisaab.data.spending

import com.harshal.hisaab.domain.BaseUseCase.None
import com.harshal.hisaab.domain.Either
import com.harshal.hisaab.domain.FailureException
import com.harshal.hisaab.domain.room.DailySumEntity
import com.harshal.hisaab.domain.room.MonthlySumEntity
import com.harshal.hisaab.domain.room.SpendingEntity
import com.harshal.hisaab.domain.room.WeeklySumEntity
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

interface SpendingRepo {
    fun spendingByDate(params: Date): Either<FailureException, Flow<List<SpendingEntity>>>
    fun spendingByDays(params: None): Either<FailureException, Flow<List<DailySumEntity>>>
    fun spendingByWeeks(params: None): Either<FailureException, Flow<List<WeeklySumEntity>>>
    fun spendingByMonths(params: None): Either<FailureException, Flow<List<MonthlySumEntity>>>
    fun addSpending(spendingEntity: SpendingEntity): Either<FailureException, None>
    fun updateSpending(params: Triple<String, Int, Int>): Either<FailureException, None>

    class SpendingRepoImpl
    @Inject constructor(
        private val _spendingDao: SpendingDao
    ) : SpendingRepo {

        override fun spendingByDate(params: Date): Either<FailureException, Flow<List<SpendingEntity>>> {
            return try {
                Either.Right(
                    _spendingDao.spendingByDate(params.time)
                )
            } catch (e: Exception) {
                Either.Left(FailureException.DaoError)
            }
        }

        override fun spendingByDays(params: None): Either<FailureException, Flow<List<DailySumEntity>>> {
            return try {
                Either.Right(
                    _spendingDao.spendingByDays(Calendar.getInstance().time.time)
                )
            } catch (e: Exception) {
                Either.Left(FailureException.DaoError)
            }
        }

        override fun spendingByWeeks(params: None): Either<FailureException, Flow<List<WeeklySumEntity>>> {
            return try {
                Either.Right(
                    _spendingDao.weeklySpending(Calendar.getInstance().time.time)
                )
            } catch (e: Exception) {
                Either.Left(FailureException.DaoError)
            }
        }

        override fun spendingByMonths(params: None): Either<FailureException, Flow<List<MonthlySumEntity>>> {
            return try {
                Either.Right(
                    _spendingDao.monthlySpending(Calendar.getInstance().time.time)
                )
            } catch (e: Exception) {
                Either.Left(FailureException.DaoError)
            }
        }

        override fun addSpending(spendingEntity: SpendingEntity): Either<FailureException, None> {
            return try {
                _spendingDao.addSpending(spendingEntity)
                Either.Right(None())
            } catch (e: Exception) {
                Either.Left(FailureException.DaoError)
            }
        }

        override fun updateSpending(params: Triple<String, Int, Int>): Either<FailureException, None> {
            return try {
                _spendingDao.updateSpending(
                    params.first, params.second, params.third
                )
                Either.Right(
                    None()
                )
            } catch (e: Exception) {
                Either.Left(FailureException.DaoError)
            }
        }

    }

}
