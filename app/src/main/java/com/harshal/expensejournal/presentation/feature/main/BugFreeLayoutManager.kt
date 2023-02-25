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


package com.harshal.expensejournal.presentation.feature.main

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * There is a bug in RecyclerView which causes views that
 * are being reloaded to pull invalid ViewHolders from the internal recycler stack if the
 * adapter size has decreased since the ViewHolder was recycled.
 */
class BugFreeLayoutManager(context: Context?, orientation: Int, reverseLayout: Boolean) :
    LinearLayoutManager(context, orientation, reverseLayout) {
    //We set this value false purposefully
    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}