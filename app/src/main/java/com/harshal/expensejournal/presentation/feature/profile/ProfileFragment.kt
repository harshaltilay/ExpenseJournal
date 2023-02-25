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


package com.harshal.expensejournal.presentation.feature.profile


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.harshal.expensejournal.databinding.FragmentProfileBinding
import com.harshal.expensejournal.domain.invisible
import com.harshal.expensejournal.domain.user.UserProfileEntity
import com.harshal.expensejournal.framework.android.platform.ActivityDelegate
import com.harshal.expensejournal.framework.android.platform.BaseFragment
import com.harshal.expensejournal.usecases.user.GetUserInfoUseCase
import com.harshal.expensejournal.usecases.user.SetUserInfoUseCase
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    @Inject
    lateinit var setUserInfoUseCase: SetUserInfoUseCase

    @Inject
    lateinit var getUserInfoUseCase: GetUserInfoUseCase

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


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
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = getUserInfoUseCase()
        if (user.name.trim().isNotBlank()) {
            binding.usernameInput.setText(user.name)
            binding.amountDailyInput.setText(user.dailyMax.toInt().toString())
            binding.amountWeeklyInput.setText(user.weeklyMax.toInt().toString())
            binding.amountMonthlyInput.setText(user.monthlyMax.toInt().toString())
            binding.countryCodeInput.setText(user.isoCountry)
        }
        setUpObservers()
        setupViewAdapters()
        assignActions()
        mainActivityDelegate.fragmentIsReady(this)
    }


    override fun assignActions() {
        binding.amountMonthlyInput.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                validateAndSubmit()
            }
            false
        }

        binding.cancelBtn.setOnClickListener {
            findNavController().popBackStack()
            binding.cancelBtn.invisible()
        }

        binding.updateProfileBtn.setOnClickListener {
            validateAndSubmit()
        }
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
            binding.backBtn.invisible()
        }
    }

    override fun setupViewAdapters() {
        if (binding.usernameInput.requestFocus()) {
            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    override fun setUpObservers() {

    }

    private fun hideKeyBoard() {
        val imm: InputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = requireActivity().currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun validateAndSubmit() {
        binding.amountDailyInput.error = null
        binding.amountWeeklyInput.error = null
        binding.amountMonthlyInput.error = null
        binding.usernameInput.error = null
        binding.countryCodeInput.error = null

        val username = binding.usernameInput.text.toString().trim()
        val daily = binding.amountDailyInput.text.toString().trim()
        val weekly = binding.amountWeeklyInput.text.toString().trim()
        val monthly = binding.amountMonthlyInput.text.toString().trim()
        val countryCode = binding.countryCodeInput.text.toString().trim()

        if (username.isEmpty() || username.length < 2) {
            binding.usernameInput.error = "Invalid reference"
            return
        }
        if (daily.isEmpty() || daily.toFloat() < 1f) {
            binding.amountDailyInput.error = "Invalid amount"
            return
        }
        if (weekly.isEmpty() || weekly.toFloat() < 1f) {
            binding.amountWeeklyInput.error = "Invalid amount"
            return
        }
        if (monthly.isEmpty() || monthly.toFloat() < 1f) {
            binding.amountMonthlyInput.error = "Invalid amount"
            return
        }

        if (!isValidLocale(countryCode)) {
            binding.countryCodeInput.error = "Invalid country code"
            return
        }

        hideKeyBoard()
        binding.updateProfileBtn.invisible()
        setUserInfoUseCase(
            UserProfileEntity(
                daily.toFloat(), weekly.toFloat(), monthly.toFloat(), username, countryCode
            )
        )
        findNavController().popBackStack()
    }

    private fun isValidLocale(countryCode: String): Boolean {
        val list = Locale.getISOCountries()
        return list.contains(countryCode.uppercase())
    }
/*
 Locale.getAvailableLocales().map { it.displayName }.filter { it.trim().isNotBlank() }
.distinct().sorted()
 */

}

