package com.harshal.expensejournal.presentation.feature.main.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.view.*
import com.harshal.expensejournal.R
import com.harshal.expensejournal.databinding.DialogUpdateDetailsBinding
import com.harshal.expensejournal.domain.inCurrency
import com.harshal.expensejournal.domain.room.SpendingInfoEntity

class UpdateDetailsDialog(
    private val contxt: Context,
    private val _activity: Activity,
    private val _entity: SpendingInfoEntity,
    val onSubmit: (desc: String, category: Int, sid: Int) -> Unit
) : Dialog(contxt) {

    private val binding: DialogUpdateDetailsBinding = DialogUpdateDetailsBinding.inflate(
        LayoutInflater.from(context)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(binding.root)
        binding.updateButtonBtn.setOnClickListener {
            validateAndSubmit()
        }
        when (_entity.category) {
            1 -> binding.essentialRb.isChecked = true
            2 -> binding.casualRb.isChecked = true
            3 -> binding.anxietyRb.isChecked = true
        }
        val userName = if (_entity.touser.length < 13) _entity.touser
        else _entity.touser.substring(0, _entity.touser.length - 1) + "..."

        "$userName : ${String.inCurrency(_entity.amount.toFloat())}".also {
            binding.sentToTv.text = it
        }
        _entity.title.also { binding.descriptionInput.setText(it) }
    }

    override fun onStart() {
        val width = getScreenWidth(_activity)
        window!!.setLayout((width * .9).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        if (binding.descriptionInput.requestFocus()) {
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    private fun validateAndSubmit() {
        val desc = binding.descriptionInput.text.toString().trim()
        val category = when (binding.categoryRg.checkedRadioButtonId) {
            R.id.essential_rb -> 1
            R.id.casual_rb -> 2
            else -> 3
        }
        if (desc.isBlank() || desc.length <= 1) {
            binding.descriptionInput.error = contxt.getString(R.string.error_invalid_desc)
            return
        }
        dismiss()
        onSubmit(desc, category, _entity.sid)
    }

    private fun getScreenWidth(activity: Activity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets: Insets =
                windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = context.resources.displayMetrics
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            @Suppress("DEPRECATION") windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }
    }
}