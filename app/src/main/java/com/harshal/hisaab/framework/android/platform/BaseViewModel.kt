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


package com.harshal.hisaab.framework.android.platform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harshal.hisaab.domain.FailureException

/**
 * Base ViewModel class with default Failure handling.
 */

abstract class BaseViewModel : ViewModel() {

    private val _failureException: MutableLiveData<FailureException> = MutableLiveData()
    val failureException: LiveData<FailureException> = _failureException

    protected val handleFailure = fun(failureException: FailureException) {
        _failureException.value = failureException
    }

}