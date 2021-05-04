package com.decagon.android.sq007.controller

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.decagon.android.sq007.model.Contact
import com.decagon.android.sq007.model.NODE_CONTACT
import com.google.firebase.database.*

class ContactViewModel : ViewModel() {

    val contactList: MutableLiveData<ArrayList<Contact>> by lazy {
        MutableLiveData<ArrayList<Contact>>()
    }




    private lateinit var firebaseSuccessListener: DataChangeSuccessListener

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

    fun deleteContact(id: String) {
        Log.d("Viewmodel", "deleteContact: $id")
        database.child(id).removeValue()
    }


    fun getUpdatedData() {

        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    updatedContactList.clear()
                    for (eachItem in snapshot.children) {

                        val contact: HashMap<String,String> = eachItem.value as HashMap<String, String>

                        // read JSON to convert hashmap to data class
                        val f = contact["firstName"]
                        val l = contact["lastName"]
                        val p = contact["phoneNumber"]
                        var i = contact["id"]

                        updatedContactList.add(Contact(id = i, firstName = f, lastName = l, phoneNumber = p))
                        updatedContactList.sortWith(compareBy<Contact>{it.firstName})

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