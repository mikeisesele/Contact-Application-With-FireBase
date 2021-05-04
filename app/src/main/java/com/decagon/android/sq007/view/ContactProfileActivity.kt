package com.decagon.android.sq007.view

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.decagon.android.sq007.databinding.ActivityContactProfileBinding

class ContactProfileActivity : AppCompatActivity() {
    private lateinit var editContact: View
    private lateinit var deleteContact: View
    private lateinit var shareContact: View
    private lateinit var caller: View
    private lateinit var messenger: View
    private lateinit var contactName: String
    private lateinit var contactNumber: String
    private lateinit var contactSurname: String

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityContactProfileBinding = ActivityContactProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // save views to variables
        editContact = binding.floatingEditButton
        deleteContact = binding.floatingDeleteButton
        shareContact = binding.floatingShareButton
        caller = binding.callButton
        messenger = binding.messageButton
        contactName = binding.profileName.toString()
        contactNumber = binding.phoneNumber.toString()

//        deleteContact.setOnClickListener {
//           // ContactViewModel.deleteContact()
//        }

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
            shareContact(contactNumber, contactName)
        }

}

    @RequiresApi(Build.VERSION_CODES.M)
    fun makeCall() {
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Call permission granted", Toast.LENGTH_SHORT)
                .show()
            // make call
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse(contactNumber)))
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
        if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent()
            val message = "$contactName $contactNumber"
            intent.action = ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share to: "))
        } else {
            // show warning
            if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                Toast.makeText(
                    this,
                    "Granting permission is required to send messages tia this application",
                    Toast.LENGTH_SHORT
                ).show()
            }
            requestPermissions(arrayOf(Manifest.permission.SEND_SMS), SHARE_CONTACT)
        }
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
                    shareContact(contactNumber, contactName)
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

