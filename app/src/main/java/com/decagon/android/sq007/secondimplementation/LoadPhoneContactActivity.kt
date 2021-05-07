package com.decagon.android.sq007.secondimplementation

import android.Manifest.permission.READ_CONTACTS
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.R
import com.decagon.android.sq007.model.Contact

class LoadPhoneContactActivity : AppCompatActivity() {

    // Columns to hold data from the phone contacts list
    private val columns = listOf<String>(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    ).toTypedArray()

    // initialize recycler view
    lateinit var loadPhoneRecyclerView: RecyclerView

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_phone_contact)

        // get xml for recycler view
        loadPhoneRecyclerView = findViewById(R.id.load_phone_contact_recycler_view)

        // request permission to read contacts
        requestPermission()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission() {
        if (
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // check if permissions are granted on install time
                checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        ) {

            // read contacts if permission is granted
            readContact()
            Toast.makeText(this, "Read Contact permission granted", Toast.LENGTH_SHORT).show()
        } else {
            if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // ask for permission at run time
                shouldShowRequestPermissionRationale(READ_CONTACTS)
            } else {
                    Toast.makeText(
                            this,
                            "Call permission is required to make phone calls with this app",
                            Toast.LENGTH_SHORT
                        ).show()
                    TODO("VERSION.SDK_INT < M")
                }
            ) {
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(READ_CONTACTS), 111)
            }
        }
    }

    // This function reads the contacts list of the phone
    private fun readContact() {
        var phoneContactList = ArrayList<Contact>()

        // Saving the result set in a variable
        val contactsResult = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            columns,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        )

        // if phone contact exists, move to the first contact
        if (contactsResult?.moveToFirst()!!) {
            // add the contact to our array list
            phoneContactList.add(
                Contact(
                    "",
                    contactsResult.getString(0).toString(),
                    "",
                    contactsResult.getString(1).toString(),
                )
            )

            // Adding the result set to the contact lists
            while (contactsResult.moveToNext()) {
                phoneContactList.add(
                    Contact(
                        "",
                        contactsResult.getString(0).toString(),
                        "",
                        contactsResult.getString(1),
                    )
                )
            }

            // set our recycler view adapter to the list of phone contacts
            loadPhoneRecyclerView.adapter = LoadPhoneContactAdapter(phoneContactList)

            // close phone query
            contactsResult.close()
        } else Toast.makeText(this, "No contact to display", Toast.LENGTH_LONG).show()
    }
}
