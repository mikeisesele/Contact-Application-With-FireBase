package com.decagon.android.sq007.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.decagon.android.sq007.databinding.ActivitySaveContactBinding
import com.decagon.android.sq007.model.Contact
import com.decagon.android.sq007.controller.ContactViewModel
import com.decagon.android.sq007.Utils.Validator
import com.decagon.android.sq007.controller.DataChangeSuccessListener
import kotlin.properties.Delegates

class SaveContactActivity : AppCompatActivity(), DataChangeSuccessListener {

    var handler = Handler()
    private lateinit var viewModel: ContactViewModel
    private var validatedName by Delegates.notNull<Boolean>()
    private var validatedNumber by Delegates.notNull<Boolean>()

    private lateinit var contactNameText: String
    private lateinit var contactNumberText: String
    private lateinit var contactSurnameText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set up binding
        val binding: ActivitySaveContactBinding = ActivitySaveContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ContactViewModel::class.java)


//        var receiver: Bundle? = intent.extras
//        // check if message received was empty. return if null
//        if(receiver == null){
//            return
//
//        } else {
//            // if not null, get the message received
//            contactNameText = receiver.getString("Name").toString()
//            contactNumberText = receiver.getString("Number").toString()
//            contactSurnameText = receiver.getString("Surname").toString()
//
//        }

        binding.btnSave.setOnClickListener {
            // convert the inputs to strings
            contactNameText = binding.tvEditTextFirstName.text.toString().trim()
            contactNumberText = binding.tvEditTextPhoneNumber.text.toString().trim()
            contactSurnameText = binding.tvEditTextSurnameName.text.toString().trim()

            validateInput()
        }
    }

    private fun validateInput() {
        // validate the inputs
        validatedName = Validator.validateName(contactNameText)
        validatedNumber = Validator.validatePhoneNumber(contactNumberText)

        if (validatedName && validatedNumber) {
            // create new contact
            val contact = Contact()

            // get the input details if the entries are correct
            contact.firstName = contactNameText
            contact.lastName = contactSurnameText
            contact.phoneNumber = contactNumberText

            // pass the contact to contact view model for database upload
            viewModel.addContact(contact, this)

            handler.postDelayed({startActivity(Intent(this, MainActivity::class.java))}, 500)


        } else {
            when {
                !validatedName -> Toast.makeText(this, "First Name is required", Toast.LENGTH_SHORT)
                    .show();
                !validatedNumber -> Toast.makeText(this, "Number is required", Toast.LENGTH_SHORT)
                    .show();
            }
        }
    }

    override fun onDataChangeSuccess(updatedContactList: ArrayList<Contact>) {

        Toast.makeText(this@SaveContactActivity, "Updated", Toast.LENGTH_SHORT).show()
    }
}



