package com.decagon.android.sq007.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.decagon.android.sq007.controller.ContactViewModel
import com.decagon.android.sq007.controller.DataChangeSuccessListener
import com.decagon.android.sq007.databinding.ActivityEditContactBinding
import com.decagon.android.sq007.model.Contact
import com.decagon.android.sq007.model.NODE_CONTACT
import com.google.firebase.database.FirebaseDatabase

class EditContactActivity : AppCompatActivity(), DataChangeSuccessListener {

    /**
     * initialize variables
     * @param viewmodel ->  Variable to hold view model. the application's access route to the database
     */

    private lateinit var contactNameText: String
    private lateinit var contactNumberText: String
    private lateinit var contactSurnameText: String
    private lateinit var contactId: String
    private lateinit var viewModel: ContactViewModel

    // initialize the database
    private val database = FirebaseDatabase.getInstance().getReference(NODE_CONTACT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialize variables
        val binding: ActivityEditContactBinding = ActivityEditContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialise view model
        viewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        // initialize variable to get data coming from contact profile activity
        val receiver: Bundle? = intent.extras
        // check if message received was empty. return if null
        if (receiver == null) {
            return
        } else {
            // if not null, get the message received
            contactNameText = receiver.getString("Name").toString()
            contactNumberText = receiver.getString("Number").toString()
            contactSurnameText = receiver.getString("Surname").toString()
            var editText = "Edit Contact"
            contactId = receiver.getString("Id").toString()

            // set profile data to edit text fields
            binding.tvEditContactFirstName.setText(contactNameText)
            binding.tvEditContactSurnameName.setText(contactSurnameText)
            binding.tvEditContactHeading.setText(editText)
            binding.tvEditContactPhoneNumber.setText(contactNumberText)

            // create new contact
            var editedContact = Contact()

            // get values from text fields and set to contact object fields
            binding.btnEditSave.setOnClickListener {
                // get the input details if the entries are correct
                editedContact.firstName = binding.tvEditContactFirstName.text.toString()
                editedContact.lastName = binding.tvEditContactSurnameName.text.toString()
                editedContact.phoneNumber = binding.tvEditContactPhoneNumber.text.toString()
                editedContact.id = contactId

                // pass the contact to contact view model for database upload
                viewModel.addContact(editedContact, this)

                // kill activity
                finish()
            }
        }
    }

    // notify when save is successful
    override fun onDataChangeSuccess(updatedContactList: ArrayList<Contact>) {
        Toast.makeText(this@EditContactActivity, "Updated", Toast.LENGTH_SHORT).show()
    }
}
