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
package com.harshal.expensejournal.presentation.feature.main.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harshal.expensejournal.R
import com.harshal.expensejournal.databinding.RowDailyItemBinding
import com.harshal.expensejournal.domain.ClickListener
import com.harshal.expensejournal.domain.room.DailySumEntity
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.Delegates

@SuppressLint("NotifyDataSetChanged")
@Singleton
class ByDayAdapter
@Inject constructor() : RecyclerView.Adapter<ByDayAdapter.ViewHolder>() {

    private var selectedPosition = 0

    internal lateinit var clickListener: ClickListener<DailySumEntity>

    internal var collection: List<DailySumEntity> by Delegates.observable(emptyList()) { _, old, neu ->

        if (old.isEmpty() && neu.isNotEmpty()) {
            notifyDataSetChanged()
            selectItem()
        } else {
            old.forEachIndexed { index, dailySumEntity ->
                if (dailySumEntity != neu[index]) {
                    selectItem(index)
                    return@forEachIndexed
                }
            }
        }
    }

    private lateinit var _recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this._recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        RowDailyItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(position)

    override fun getItemCount() = collection.size

    private fun selectItem(position: Int = -1) {
        if (collection.isNotEmpty()) {
            notifyItemChanged(selectedPosition)
            selectedPosition = if (position == -1) selectedPosition else position
            clickListener.onItemClick(collection[selectedPosition])
            notifyItemChanged(selectedPosition)
        }
    }

    fun getCurEntity(): DailySumEntity? {
        return try {
            collection[selectedPosition]
        } catch (e: Exception) {
            null
        }
    }

    inner class ViewHolder(private val binding: RowDailyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.rowData = collection[position]
            binding.root.setOnClickListener {
                selectItem(position)
            }
            if (selectedPosition == position) binding.root.setBackgroundResource(R.drawable.rect_border_gray)
            else binding.root.setBackgroundResource(R.color.white)
        }
    }
}
