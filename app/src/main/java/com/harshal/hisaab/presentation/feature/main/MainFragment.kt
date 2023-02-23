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

package com.harshal.hisaab.presentation.feature.main

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.harshal.hisaab.R
import com.harshal.hisaab.databinding.FragmentMainBinding
import com.harshal.hisaab.domain.*
import com.harshal.hisaab.domain.room.DailySumEntity
import com.harshal.hisaab.domain.room.DebitEntity
import com.harshal.hisaab.domain.room.SpendingEntity
import com.harshal.hisaab.domain.user.Quotes
import com.harshal.hisaab.framework.android.platform.ActivityDelegate
import com.harshal.hisaab.framework.android.platform.BaseFragment
import com.harshal.hisaab.presentation.feature.main.adapters.ByDayAdapter
import com.harshal.hisaab.presentation.feature.main.adapters.ByMonthAdapter
import com.harshal.hisaab.presentation.feature.main.adapters.ByWeekAdapter
import com.harshal.hisaab.presentation.feature.main.adapters.DebitListAdapter
import com.harshal.hisaab.presentation.feature.main.dialog.UpdateDetailsDialog
import com.harshal.hisaab.usecases.user.GetUserInfoUseCase
import dagger.hilt.android.AndroidEntryPoint
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : BaseFragment() {

    @Inject
    lateinit var getUserInfoUseCase: GetUserInfoUseCase

    @Inject
    lateinit var monthHistoryAdapter: ByMonthAdapter

    @Inject
    lateinit var weekHistoryAdapter: ByWeekAdapter

    @Inject
    lateinit var daysHistoryAdapter: ByDayAdapter

    @Inject
    lateinit var debitListAdapter: DebitListAdapter

    private var _binding: FragmentMainBinding? = null
    val binding get() = _binding!!

    private val _mainFragmentViewModel: MainFragmentViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mainActivityDelegate = context as ActivityDelegate
        } catch (e: ClassCastException) {
            throw ClassCastException("Host activity must implement ActivityDelegate")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = getUserInfoUseCase()
        if (user.name.trim().isBlank()) {
            gotTo(R.id.action_to_profile)
        } else {
            _mainFragmentViewModel.setUserProfile(user)
        }
        mainActivityDelegate.fragmentIsReady(this)
    }

    override fun onStart() {
        super.onStart()
        init()
    }

    override fun onStop() {
        super.onStop()
        _mainFragmentViewModel.endFlow()
    }

    private fun init() {
        val user = _mainFragmentViewModel.userProfileEntity
        user?.let {
            _mainFragmentViewModel.beginFlow()
            it.apply {
                binding.usernameTv.text = String.html(getString(R.string.string_welcome_user, name))
                binding.monthlyLimitTv.text = String.html(
                    getString(R.string.string_limit_monthly, String.inCurrency(monthlyMax))
                )
                binding.weeklyLimitTv.text = String.html(
                    getString(R.string.string_limit_weekly, String.inCurrency(weeklyMax))
                )
                binding.dailyLimitTv.text = String.html(
                    getString(R.string.string_limit_daily, String.inCurrency(dailyMax))
                )
                val currentDate =
                    Calendar.getInstance().time.toInstant().atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
                val month = currentDate.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                val year = currentDate.year.toString().uppercase()
                binding.presentMonthTv.text = month
                binding.presentYearTv.text = year
            }
            Quotes.getNormalQuote().apply {
                binding.quoteTv.text = this
            }
            setupViewAdapters()
            assignActions()
            setUpObservers()
        }
    }

    override fun assignActions() {

        binding.menuBtn.setOnClickListener {
            showPopUpMenu(it)
        }

        binding.showAddExpenseViewBtn.setOnClickListener {
            binding.addExpenseLayout.visible()
            if (binding.amountInput.requestFocus()) {
                requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            }
        }

        binding.closeExpenseBtn.setOnClickListener {
            hideKeyBoard()
            binding.addExpenseLayout.gone()
        }

        binding.submitExpenseBtn.setOnClickListener {
            val cat = when (binding.categoryRg.checkedRadioButtonId) {
                R.id.essential_rb -> 1
                R.id.casual_rb -> 2
                else -> 3
            }
            addNewSpending(
                binding.amountInput.text.toString().trim(),
                binding.sentToInput.text.toString().trim(),
                binding.descriptionInput.text.toString().trim(),
                cat
            )
            hideKeyBoard()
        }

        binding.sentToInput.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                hideKeyBoard()
            }
            false
        }
    }

    override fun setupViewAdapters() {

        binding.monthsRv.layoutManager =
            BugFreeLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.daysRv.setHasFixedSize(true)
        binding.daysRv.isNestedScrollingEnabled = false
        binding.monthsRv.adapter = monthHistoryAdapter

        binding.weeksRv.layoutManager =
            BugFreeLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.daysRv.setHasFixedSize(true)
        binding.daysRv.isNestedScrollingEnabled = false
        binding.weeksRv.adapter = weekHistoryAdapter

        binding.daysRv.layoutManager =
            BugFreeLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.daysRv.setHasFixedSize(true)
        binding.daysRv.isNestedScrollingEnabled = false
        daysHistoryAdapter.clickListener = object : ClickListener<DailySumEntity> {
            override fun onItemClick(entity: DailySumEntity) {
                binding.daySelectedTv.text = String.getFriendlyDayAndMonth(entity.time)
                _mainFragmentViewModel.fetchByDate(entity.time)
            }
        }
        binding.daysRv.adapter = daysHistoryAdapter


        binding.spendingListRecyclerView.layoutManager =
            BugFreeLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        debitListAdapter.clickListener = object : ClickListener<DebitEntity> {
            override fun onItemClick(entity: DebitEntity) {
                if (entity.type == 0) {
                    val updateDialog = UpdateDetailsDialog(
                        requireContext(), requireActivity(), entity
                    ) { desc, category, sid ->
                        _mainFragmentViewModel.updateSpendingDescription(desc, category, sid)
                        hideKeyBoard()
                    }
                    updateDialog.setOnCancelListener {
                        hideKeyBoard()
                    }
                    updateDialog.show()
                }
            }
        }
        binding.spendingListRecyclerView.adapter = debitListAdapter
    }

    override fun setUpObservers() {
        with(_mainFragmentViewModel) {

            observe(spendingByMonthList) {
                monthHistoryAdapter.collection = it ?: emptyList()

            }
            observe(spendingByWeekList) {
                weekHistoryAdapter.collection = it ?: emptyList()
            }

            observe(spendingByDayList) {
                daysHistoryAdapter.collection = it ?: emptyList()
            }

            observe(debitEntityList) {
                debitListAdapter.collection = it ?: emptyList()
            }

            observe(totalYearly) {
                binding.presentYearTotalTv.text = String.html(
                    getString(R.string.string_total_year, String.inCurrency(it ?: 0f))
                )
            }
            observe(totalMonthly) {
                if (it == null) return@observe

                binding.presentMonthTotalTv.text = String.html(
                    getString(R.string.string_total_month, String.inCurrency(it))
                )

                binding.presentMonthTotalTv.setTextColor(
                    if (it > _mainFragmentViewModel.userProfileEntity!!.monthlyMax) {
                        resources.getColor(
                            R.color.red, null
                        )
                    } else {
                        resources.getColor(
                            R.color.text_dark_blue_1, null
                        )
                    }
                )
            }
            failure(failureException) {
                notifyWithAction(R.string.failure_db_error, R.string.action_ok, null)
            }
        }
    }

    private fun addNewSpending(
        amount: String, touser: String, desc: String, category: Int
    ) {
        if (amount.isEmpty()) {
            binding.amountInput.error = "Invalid amount"
            return
        }
        if (desc.isEmpty() || desc.length < 3) {
            binding.descriptionInput.error = "Invalid description"
            return
        }
        if (touser.isEmpty() || touser.length < 2) {
            binding.sentToInput.error = "Invalid reference"
            return
        }
        val spendingEntity = SpendingEntity(
            amount = amount.toFloat(),
            touser = touser,
            desc = desc,
            category = category,
            time = Calendar.getInstance().time
        )
        _mainFragmentViewModel.addSpending(spendingEntity)
        hideKeyBoard()
        binding.addExpenseLayout.gone()
    }

    private fun hideKeyBoard() {
        val imm: InputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = requireActivity().currentFocus
        if (view == null) {
            view = View(requireActivity())
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showPopUpMenu(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.inflate(R.menu.application_menu)

        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.menu_item_profile -> {
                    gotTo(R.id.action_to_profile)
                }
                R.id.menu_item_search -> {
                    Toast.makeText(requireContext(), item.title, Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

        var item: MenuItem = popup.menu.getItem(0)
        var s = SpannableString(item.title)
        s.setSpan(ForegroundColorSpan(Color.BLACK), 0, s.length, 0)
        item.title = s

        item = popup.menu.getItem(1)
        s = SpannableString(item.title)
        s.setSpan(ForegroundColorSpan(Color.BLACK), 0, s.length, 0)
        item.title = s

        popup.show()
    }
}




