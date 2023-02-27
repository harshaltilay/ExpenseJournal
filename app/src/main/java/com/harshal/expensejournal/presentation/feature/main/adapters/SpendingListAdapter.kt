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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.harshal.expensejournal.databinding.RowDebitEntityBinding
import com.harshal.expensejournal.domain.ClickListener
import com.harshal.expensejournal.domain.room.SpendingEntity
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.Delegates

@Singleton
class SpendingListAdapter
@Inject constructor() : RecyclerView.Adapter<SpendingListAdapter.ViewHolder>() {

    internal lateinit var clickListener: ClickListener<SpendingEntity>

    internal var collection: List<SpendingEntity> by Delegates.observable(emptyList()) { _, old, new ->
        val diffCallback = DiffCallback(old, new)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        scrollToTop()
    }

    private lateinit var _recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this._recyclerView = recyclerView
    }

    private fun scrollToTop() {
        _recyclerView.scrollToPosition(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        RowDebitEntityBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(position)

    override fun getItemCount() = collection.size

    inner class ViewHolder(private val binding: RowDebitEntityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val entity = collection[position]
            binding.rowData = entity
            binding.root.setOnClickListener {
                clickListener.onItemClick(entity)
            }
        }
    }

    inner class DiffCallback(
        private val oldList: List<SpendingEntity>, private val newList: List<SpendingEntity>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].sid == newList[newItemPosition].sid && oldList[oldItemPosition].touser == newList[newItemPosition].touser
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

}
