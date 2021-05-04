package com.decagon.android.sq007.Utils

object Validator {

    // validate phone number with regex
    fun validatePhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.isNotEmpty()
    }

    // validate name
    fun validateName(name: String): Boolean {
        return name.isNotEmpty()
    }

}
