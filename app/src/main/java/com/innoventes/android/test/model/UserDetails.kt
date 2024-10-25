package com.innoventes.android.test.model

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

data class UserDetails(
    var birthdate: String = "",
    var panNumber: String = ""
) {
    fun isValidBirthdate(): Boolean {
        if (birthdate.length != 10) return false

        val regex = Regex("^\\d{2}/\\d{2}/\\d{4}$")
        if (!regex.matches(birthdate)) return false

        val day = birthdate.substring(0, 2).toInt()
        val month = birthdate.substring(3, 5).toInt()
        val year = birthdate.substring(6, 10).toInt()


        return try {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Updated format
            dateFormat.isLenient = false
            dateFormat.parse(birthdate)
            true
        } catch (e: ParseException) {
            false
        }
    }


    fun isValidPAN(): Boolean {
        val panPattern = Regex("[A-Z]{5}[0-9]{4}[A-Z]{1}") //Valid PAN number format is ABCDE1234F
        return panPattern.matches(panNumber)
    }
}
