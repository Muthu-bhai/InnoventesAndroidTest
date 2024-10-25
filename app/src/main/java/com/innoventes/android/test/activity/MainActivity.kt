package com.innoventes.android.test.activity

import android.content.Intent
import com.innoventes.android.test.viewModel.RegistrationViewModel
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.innoventes.android.test.R
import com.innoventes.android.test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBirthdateListeners()
        setupPanKYCInfoText()

        viewModel.isFormValid.observe(this) { isValid ->
            binding.nextButton.isEnabled = isValid
        }

        viewModel.buttonBackgroundColor.observe(this) { backgroundResId ->
            binding.nextButton.setBackgroundResource(backgroundResId)
        }
        viewModel.panInputBackgroundColor.observe(this) { backgroundResId ->
            binding.panInput.setBackgroundResource(backgroundResId)
        }

        binding.panInput.addTextChangedListener { text ->
            viewModel.setPanNumber(text.toString())
        }


        binding.nextButton.setOnClickListener {
            viewModel.submitDetails()
            Toast.makeText(this, "Details submitted successfully", Toast.LENGTH_SHORT).show()
            val panNumber = viewModel.getPanNumber()
            val dateOfBirth = viewModel.getBirthdate()
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, UserDetailsActivity::class.java).apply {
                    putExtra("PAN_NUMBER", panNumber)
                    putExtra("DATE_OF_BIRTH", dateOfBirth)
                }
                startActivity(intent)
                finish()
            }, Toast.LENGTH_LONG.toLong())
        }

        binding.noPanButton.setOnClickListener {
            finish() // Close activity
        }
    }

    private fun setupPanKYCInfoText() {
        val spannableString = SpannableString(getString(R.string.pan_details_text))

        val startIndex = getString(R.string.pan_details_text).indexOf("Learn more")
        val endIndex = startIndex + "Learn more".length

        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.purple_700)),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )


        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(this@MainActivity, "Learn more clicked!", Toast.LENGTH_SHORT).show()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.infoText.text = spannableString
       // binding.infoText.movementMethod = LinkMovementMethod.getInstance()
    }
    private fun setupBirthdateListeners() {
        val inputs = listOf(binding.dayInput, binding.monthInput, binding.yearInput)

        for (input in inputs) {
            input.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    handleTextChange(input)
                    updateBackgroundColor(input)

                    updateBirthdateInViewModel()
                }

                override fun afterTextChanged(s: Editable?) {
                    updateBackgroundColor(input)
                }
            })

            input.setOnFocusChangeListener { _, hasFocus ->
                updateBackgroundColor(input)
            }
        }
    }


    private fun handleTextChange(input: EditText) {
        when (input) {
            binding.dayInput -> if (input.text.length == 2) binding.monthInput.requestFocus()
            binding.monthInput -> if (input.text.length == 2) binding.yearInput.requestFocus()
            binding.yearInput -> { /* No action needed */ }
        }
    }

    private fun updateBackgroundColor(editText: EditText) {
        if (editText.hasFocus()) {
            editText.setBackgroundResource(R.drawable.bg_purple_input)
        } else {
            editText.setBackgroundResource(if (editText.text.isNullOrEmpty()) R.drawable.bg_grey_input else R.drawable.bg_grey_input)
        }
    }

    private fun updateBirthdateInViewModel() {
        val day = binding.dayInput.text.toString()
        val month = binding.monthInput.text.toString()
        val year = binding.yearInput.text.toString()
        viewModel.setBirthdate(day, month, year)
    }

}
