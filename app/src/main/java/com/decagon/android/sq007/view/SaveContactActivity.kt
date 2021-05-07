package com.decagon.android.sq007.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.decagon.android.sq007.Utils.Validator
import com.decagon.android.sq007.controller.ContactViewModel
import com.decagon.android.sq007.controller.DataChangeSuccessListener
import com.decagon.android.sq007.databinding.ActivitySaveContactBinding
import com.decagon.android.sq007.model.Contact
import com.decagon.android.sq007.model.NODE_CONTACT
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class SaveContactActivity : AppCompatActivity(), DataChangeSuccessListener {

    /**
     * initialize variables
     * @param viewmodel ->  Variable to hold view model. the application's access route to the database
     */
    private lateinit var viewModel: ContactViewModel
    private lateinit var contactNameText: String
    private lateinit var contactNumberText: String
    private lateinit var contactSurnameText: String
    private var validatedName by Delegates.notNull<Boolean>()
    private var validatedNumber by Delegates.notNull<Boolean>()

    // initialize the database
    private val database = FirebaseDatabase.getInstance().getReference(NODE_CONTACT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set up binding
        val binding: ActivitySaveContactBinding = ActivitySaveContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialise view model
        viewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        // collect data from input fields
        binding.btnSave.setOnClickListener {
            // convert the inputs to strings
            contactNameText = binding.tvEditTextFirstName.text.toString().trim()
            contactNumberText = binding.tvEditTextPhoneNumber.text.toString().trim()
            contactSurnameText = binding.tvEditTextSurnameName.text.toString().trim()

            // validate input data
            validateInput()
        }
    }

    private fun validateInput() {
        // validate the inputs
        validatedName = Validator.validateName(contactNameText)
        validatedNumber = Validator.validatePhoneNumber(contactNumberText)

        // check if entries meets expectations
        if (validatedName && validatedNumber) {
            // create new contact
            val contact = Contact()

            // get the input details if the entries are correct
            contact.firstName = contactNameText
            contact.lastName = contactSurnameText
            contact.phoneNumber = contactNumberText
            contact.id = database.push().key.toString()

            // pass the contact to contact view model for database upload
            viewModel.addContact(contact, this)
            finish()

            // lunch main activity after save complete
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // show  notifications if data not entered correctly
            when {
                !validatedName -> Toast.makeText(this, "First Name is required", Toast.LENGTH_SHORT)
                    .show()
                !validatedNumber -> Toast.makeText(this, "Number is required", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // notify when save is successful
    override fun onDataChangeSuccess(updatedContactList: ArrayList<Contact>) {
        Toast.makeText(this@SaveContactActivity, "Updated", Toast.LENGTH_SHORT).show()
    }
}
