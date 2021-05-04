package com.decagon.android.sq007.view

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.decagon.android.sq007.controller.ContactViewModel
import com.decagon.android.sq007.databinding.ActivityContactProfileBinding
import com.google.firebase.database.FirebaseDatabase

class ContactProfileActivity : AppCompatActivity() {
    private lateinit var editContact: View
    private lateinit var deleteContact: ImageView
    private lateinit var shareContact: View
    private lateinit var caller: View
    private lateinit var messenger: View
    private lateinit var contactName: TextView
    private lateinit var contactNumber: TextView
    private lateinit var viewModel: ContactViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // instantiate view model
        viewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        val binding: ActivityContactProfileBinding = ActivityContactProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // save views to variables
        editContact = binding.floatingEditButton
        deleteContact = binding.floatingDeleteButton
        shareContact = binding.floatingShareButton
        caller = binding.callButton
        contactName = binding.profileName
        contactNumber = binding.phoneNumber




        // get values for each recycler item (values coming from main activity intent listener click)
         contactName.text = intent.getStringExtra("personName").toString()
         contactNumber.text = intent.getStringExtra("personNumber").toString()
         var contactId = intent.getStringExtra("personId").toString()





        deleteContact.setOnClickListener {
            Log.d("Profile", "onCreate: deleting")
            var ref = FirebaseDatabase.getInstance().getReference("contact")

//            Log.i("name", contactName.toString())
//            Log.i("number", contactNumber.toString())
//            Log.i("id", "$contactId")

       //     ref.child(contactId).removeValue()

           viewModel.deleteContact(contactId)
            Toast.makeText(this, "$contactName deleted successfully", Toast.LENGTH_SHORT).show()
//            viewModel.getUpdatedData()
            finish()
        }

//        editContact.setOnClickListener {
//            startActivity(Intent(this, SaveContactActivity::class.java))
//            intent.putExtra("Name", contactName)
//            intent.putExtra("Number", contactNumber)
//            intent.putExtra("Surname", contactSurname)
//        }

        caller.setOnClickListener {
            makeCall()
        }

        shareContact.setOnClickListener {
            // share details
            shareContact(contactName.toString(), contactName.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun makeCall() {
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Call permission granted", Toast.LENGTH_SHORT)
                .show()
            // make call
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:" +  contactNumber.text.toString())))
        } else {
            // show warning
            if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                Toast.makeText(
                    this,
                    "Call permission is required to make phone calls with this app",
                    Toast.LENGTH_SHORT
                ).show()
            }
            requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun shareContact(contactNumber: String, contactName: String) {
        val intent = Intent()
        val message = "$contactName $contactNumber"
        intent.action = ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.type = "text/plain"
        startActivity(Intent.createChooser(intent, "Share to: "))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    // request permissions
    override fun onRequestPermissionsResult(

        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray

    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CALL) {
            if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    makeCall()
                }
            } else {
                Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == SHARE_CONTACT) {
            if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    shareContact(contactNumber.toString(), contactName.toString())
                }
            } else {
                Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private var SHARE_CONTACT = 202
        private var REQUEST_CALL = 101
    }
}

