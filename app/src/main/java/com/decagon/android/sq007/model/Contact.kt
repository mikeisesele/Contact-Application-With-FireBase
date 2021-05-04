package com.decagon.android.sq007.model

import com.google.firebase.database.Exclude

data class Contact(
    @get: Exclude
    var id: String? = null,
    var firstName: String?  = null,
    var lastName: String? = null,
    var phoneNumber: String? = null )