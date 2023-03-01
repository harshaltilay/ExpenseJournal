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


package com.harshal.expensejournal.framework.android.platform

import com.harshal.expensejournal.data.cookies.tab.TabSelectionAPI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TabSelectionFramework @Inject constructor(private var sharedPreferenceFramework: SharedPreferenceFramework) :
    TabSelectionAPI {

    override fun setTabIndex(value: Int) {
        sharedPreferenceFramework.setIntVal("KEY_TAB_INDEX", value)
    }

    override fun getTabIndex(): Int {
        return sharedPreferenceFramework.getIntVal("KEY_TAB_INDEX")
    }
}