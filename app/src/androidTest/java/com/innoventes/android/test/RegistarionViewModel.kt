import androidx.lifecycle.*
import com.innoventes.android.test.model.UserDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    private val _userDetails = MutableLiveData<UserDetails>()
    val userDetails: LiveData<UserDetails> get() = _userDetails

    private val _isFormValid = MutableLiveData<Boolean>()
    val isFormValid: LiveData<Boolean> get() = _isFormValid

    init {
        _userDetails.value = UserDetails()
        _isFormValid.value = false
    }

    fun setBirthdate(birthdate: String) {
        _userDetails.value?.birthdate = birthdate
        validateForm()
    }

    fun setPanNumber(panNumber: String) {
        _userDetails.value?.panNumber = panNumber
        validateForm()
    }

    private fun validateForm() {
        viewModelScope.launch(Dispatchers.IO) {
            val details = _userDetails.value
            val isValid = details?.isValidBirthdate() == true && details.isValidPAN()

            launch(Dispatchers.Main) {
                _isFormValid.value = isValid
            }
        }
    }

    fun submitDetails() {
        // Assuming submission logic here (e.g., saving to the server)
    }
}
