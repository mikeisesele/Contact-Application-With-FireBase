package com.decagon.android.sq007.model

import com.google.firebase.database.Exclude

data class Contact(
    @Exclude
    var id: String?,
    var firstName: String?,
    var lastName: String?,
    var phoneNumber: String?,
) {
    constructor() : this("", "", "", "")
}
