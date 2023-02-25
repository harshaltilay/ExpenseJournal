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


package com.harshal.expensejournal.domain.di

import android.content.Context
import com.harshal.expensejournal.data.cookies.UserProfileAPI
import com.harshal.expensejournal.data.cookies.UserProfileRepo
import com.harshal.expensejournal.data.spending.SpendingDao
import com.harshal.expensejournal.data.spending.SpendingRepo
import com.harshal.expensejournal.framework.android.platform.SharedPreferenceFramework
import com.harshal.expensejournal.framework.roomdb.RoomDBFramework
import com.harshal.expensejournal.framework.user.UserProfileFramework
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Lets declare dependency modules to be Injected (in our case all singleton)
 * */

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideSpendingDao(@ApplicationContext context: Context): SpendingDao =
        RoomDBFramework.getDatabase(context).spentDao()

    @Provides
    @Singleton
    fun provideSpendingRepository(dataSource: SpendingRepo.SpendingRepoImpl): SpendingRepo =
        dataSource

    @Provides
    @Singleton
    fun provideUserProfileAPI(framework: UserProfileFramework): UserProfileAPI = framework

    @Provides
    @Singleton
    fun provideUserRepository(dataSource: UserProfileRepo.UserProfileRepoImpl): UserProfileRepo =
        dataSource

    @Provides
    @Singleton
    fun provideSharedPreferenceFramework(@ApplicationContext context: Context): SharedPreferenceFramework =
        SharedPreferenceFramework.i(context)
}
