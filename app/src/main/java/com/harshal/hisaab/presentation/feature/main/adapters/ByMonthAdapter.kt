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


package com.harshal.hisaab.presentation.feature.main.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harshal.hisaab.databinding.RowMonthlyItemBinding
import com.harshal.hisaab.domain.room.MonthlySumEntity
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.Delegates

@SuppressLint("NotifyDataSetChanged")
@Singleton
class ByMonthAdapter
@Inject constructor() : RecyclerView.Adapter<ByMonthAdapter.ViewHolder>() {

    internal var collection: List<MonthlySumEntity> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    private lateinit var _recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this._recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        RowMonthlyItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(collection[position])

    override fun getItemCount() = collection.size

    inner class ViewHolder(private val binding: RowMonthlyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(monthlySumEntity: MonthlySumEntity) {
            binding.rowData = monthlySumEntity
        }
    }
}
