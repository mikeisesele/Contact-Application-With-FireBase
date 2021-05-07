package com.decagon.android.sq007.controller

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.decagon.android.sq007.model.Contact
import com.decagon.android.sq007.model.NODE_CONTACT
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ContactViewModel : ViewModel() {

    // set variable to watch list coming from firebase for changes
    val contactList: MutableLiveData<ArrayList<Contact>> by lazy {
        // initialize mutable live data on on the list to be watched
        MutableLiveData<ArrayList<Contact>>()
    }

    // set variable to listen to changes from firebase
    private lateinit var firebaseSuccessListener: DataChangeSuccessListener

    // set variable to hold list of contacts from firebase
    companion object {
        var updatedContactList: ArrayList<Contact> = ArrayList()
    }

    // initialize the database
    private val database = FirebaseDatabase.getInstance().getReference(NODE_CONTACT)

    // function to add contact. function call from save contact
    fun addContact(contact: Contact, firebaseSuccessListener: DataChangeSuccessListener) {

        // use id to set data to firebase
        database.child(contact.id!!).setValue(contact)

        // get the updated contacts from the database to populate the recycler view
        getUpdatedData()
    }

    // delete contact using id as reference
    fun deleteContact(id: String) {
        Log.d("Viewmodel", "deleteContact: $id")
        database.child(id).removeValue()
    }

    // gets updated data from database
    fun getUpdatedData() {
        // listen to the database for any change
        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // get the current state of data in data base
                if (snapshot.exists()) {

                    // clear current list
                    updatedContactList.clear()

                    for (eachItem in snapshot.children) {

                        // get keys and values for each item in data node children
                        val contact: HashMap<String, String> = eachItem.value as HashMap<String, String>

                        // set the value from the node keys to variables
                        val firstname = contact["firstName"]?.capitalize(Locale.ROOT)
                        val lastname = contact["lastName"]
                        val phonenumber = contact["phoneNumber"]
                        val id = contact["id"]

                        // populate list to be used by recycler view with new contact objects.
                        updatedContactList.add(Contact(id = id, firstName = firstname, lastName = lastname, phoneNumber = phonenumber))

                        // sort list alphabetically based on first name
                        updatedContactList.sortWith(compareBy<Contact> { it.firstName })
                    }
                    // set list to mutable live data watcher
                    contactList.value = updatedContactList
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}

// update list for recycler view on data change
interface DataChangeSuccessListener {
    fun onDataChangeSuccess(updatedContactList: ArrayList<Contact>)
}
