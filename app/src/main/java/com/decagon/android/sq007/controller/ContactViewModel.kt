package com.decagon.android.sq007.controller

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.decagon.android.sq007.adapter.RecyclerViewAdapter
import com.decagon.android.sq007.controller.ContactViewModel.Companion.updatedContactList
import com.decagon.android.sq007.model.Contact
import com.decagon.android.sq007.model.NODE_CONTACT
import com.decagon.android.sq007.view.MainActivity
import com.google.firebase.database.*

class ContactViewModel : ViewModel() {

    private lateinit var firebaseSuccessListener: DataChangeSuccessListener

    companion object {
        var updatedContactList: ArrayList<Contact> = ArrayList()
    }

    val updatedDatabase = FirebaseDatabase.getInstance().getReference("contact")

    // initialize the database
    private val database = FirebaseDatabase.getInstance().getReference(NODE_CONTACT)

    // function to add contact. function call from save contact
    fun addContact(contact: Contact, firebaseSuccessListener: DataChangeSuccessListener) {

        // get firebase generated id
        contact.id = database.push().key

        // use id to set data to firebase
        database.child(contact.id!!).setValue(contact)

        // get the updated contacts from the database to populate the recycler view
        getUpdatedData()
    }

//    fun deleteContact(contact: Contact) {
//
//    }


    fun getUpdatedData() {

        updatedDatabase.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    updatedContactList.clear()
                    for (eachItem in snapshot.children) {

                        val contact: HashMap<String,String> = eachItem.value as HashMap<String, String>

                        // read JSON to convert hashmap to data class
                        val f = contact["firstName"]
                        val l = contact["lastName"]
                        val p = contact["phoneNumber"]

                        updatedContactList.add(Contact(firstName = f, lastName = l, phoneNumber = p))

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}

interface DataChangeSuccessListener {

    fun onDataChangeSuccess(updatedContactList: ArrayList<Contact>)
}