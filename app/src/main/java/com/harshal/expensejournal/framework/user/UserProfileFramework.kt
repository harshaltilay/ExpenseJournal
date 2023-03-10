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


package com.harshal.expensejournal.framework.user

import com.google.gson.Gson
import com.harshal.expensejournal.data.cookies.user.UserProfileAPI
import com.harshal.expensejournal.domain.user.UserProfileEntity
import com.harshal.expensejournal.framework.android.platform.SharedPreferenceFramework
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileFramework @Inject constructor(private var sharedPreferenceFramework: SharedPreferenceFramework) :
    UserProfileAPI {

    override fun setUserInfo(value: UserProfileEntity) {
        val gson = Gson()
        val json = gson.toJson(value)
        sharedPreferenceFramework.setStringVal("KEY_USER_PROFILE", json)
    }

    override fun getUserInfo(): UserProfileEntity {
        val gson = Gson()
        val json: String? = sharedPreferenceFramework.getStringVal("KEY_USER_PROFILE")
        return gson.fromJson(json, UserProfileEntity::class.java) ?: UserProfileEntity.blankEntity()
    }
}