package com.innoventes.android.test.viewModel

import androidx.lifecycle.*
import com.innoventes.android.test.R
import com.innoventes.android.test.model.UserDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class RegistrationViewModel : ViewModel() {

    private val _userDetails = MutableLiveData<UserDetails>()
    val userDetails: LiveData<UserDetails> get() = _userDetails

    private val _isFormValid = MutableLiveData<Boolean>()
    val isFormValid: LiveData<Boolean> get() = _isFormValid

    private val _buttonBackgroundColor = MutableLiveData<Int>()
    val buttonBackgroundColor: LiveData<Int> get() = _buttonBackgroundColor


    private val _panInputBackgroundColor = MutableLiveData<Int>()
    val panInputBackgroundColor: LiveData<Int> get() = _panInputBackgroundColor


    init {
        _userDetails.value = UserDetails()
        _isFormValid.value = false
        _buttonBackgroundColor.value = R.drawable.bg_button_disabled

    }

    fun setBirthdate(day: String, month: String, year: String) {
        if (day.isNotEmpty() && month.isNotEmpty() && year.isNotEmpty()) {
            try {
                val birthdate = "$day/$month/$year"
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                dateFormat.isLenient = false
                _userDetails.value?.birthdate = birthdate
            } catch (e: ParseException) {
                _userDetails.value?.birthdate = ""
            }
        } else {
            _userDetails.value?.birthdate = ""
        }
        validateForm()
    }


    fun setPanNumber(panNumber: String) {
        _userDetails.value?.panNumber = panNumber
        validateForm()
    }

    private fun validateForm() {
        viewModelScope.launch(Dispatchers.IO) {
            val details = _userDetails.value
            val isValidBirthdate = details?.isValidBirthdate() == true
            val isValidPAN = details?.isValidPAN() == true

            val isValidPanAndBirthDate = isValidBirthdate && isValidPAN

            launch(Dispatchers.Main) {
                _isFormValid.value = isValidPanAndBirthDate

                _buttonBackgroundColor.value = if (isValidPanAndBirthDate) {
                    R.drawable.bg_button_enabled
                } else {
                    R.drawable.bg_button_disabled
                }

                _panInputBackgroundColor.value = if (isValidPAN) {
                    R.drawable.bg_blue_input
                } else {
                    R.drawable.bg_grey_input
                }
            }
        }
    }

    fun getPanNumber(): String {
        return _userDetails.value?.panNumber ?: ""
    }

    fun getBirthdate(): String {
        return _userDetails.value?.birthdate ?: ""
    }


    fun submitDetails() {
        // ToDo submission logic here (e.g., saving to the server and move to next screen)
    }
}
