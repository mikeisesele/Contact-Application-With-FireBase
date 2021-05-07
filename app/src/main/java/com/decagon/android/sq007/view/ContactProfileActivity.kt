package com.decagon.android.sq007.view

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.lifecycle.ViewModelProvider
import com.decagon.android.sq007.controller.ContactViewModel
import com.decagon.android.sq007.databinding.ActivityContactProfileBinding

class ContactProfileActivity : AppCompatActivity() {
    private lateinit var editContact: View
    private lateinit var deleteContact: ImageView
    private lateinit var shareContact: View
    private lateinit var caller: View
    private lateinit var contactName: TextView
    private lateinit var contactSurname: TextView
    private lateinit var contactNumber: TextView
    private lateinit var viewModel: ContactViewModel
    private lateinit var contactId: String

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // instantiate view model
        viewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        // initialize binding
        val binding: ActivityContactProfileBinding = ActivityContactProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // save views to variables
        editContact = binding.floatingEditButton
        deleteContact = binding.floatingDeleteButton
        shareContact = binding.floatingShareButton
        caller = binding.callButton
        contactName = binding.profileName
        contactNumber = binding.phoneNumber
        contactSurname = binding.profileSurname

        // get values for each recycler item (values coming from main activity intent listener click)
        contactName.text = intent.getStringExtra("personName").toString()
        contactNumber.text = intent.getStringExtra("personNumber").toString()
        contactSurname.text = intent.getStringExtra("personSurname").toString()
        contactId = intent.getStringExtra("personId").toString()

        // delete contact
        deleteContact.setOnClickListener {
            AlertDialog.Builder(this).also {
                it.setTitle("Are You Sure You Want To Delete This Contact?")
                it.setPositiveButton("YES") { dialog, which ->
                    initiateDelete()
//                        viewModel.deleteContact(swipedContact)
//                        binding.recyclerView.adapter?.notifyItemRemoved(position)
                    Toast.makeText(this, "Contact Deleted Successfully", Toast.LENGTH_SHORT).show()
                }
                it.setNegativeButton("NO") { dialog, which ->
                    dialog.cancel()
                }
            }.create().show()
        }

        // edit contact
        editContact.setOnClickListener {
            val intent = Intent(this, EditContactActivity::class.java)
            intent.putExtra("Edit", "Edit contact")
            intent.putExtra("Name", contactName.text)
            intent.putExtra("Number", contactNumber.text)
            intent.putExtra("Surname", contactSurname.text)
            intent.putExtra("Id", contactId)
            startActivity(intent)
        }

        // make call
        caller.setOnClickListener {
            makeCall()
        }

        // share contact details
        shareContact.setOnClickListener {

            shareContact(contactName.text.toString(), contactName.text.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun makeCall() {
        // check if install time permission is granted
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            // make call if true
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contactNumber.text.toString())))
        } else {
            // show warning when call request fails
            if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                Toast.makeText(this, "Call permission is required to make phone calls with this app", Toast.LENGTH_SHORT)
                    .show()
                // request permission at run time
                requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    // function to share contact
    fun shareContact(contactNumber: String, contactName: String) {
        val intent = Intent()
        // set data to be sent
        val message = "$contactName $contactNumber"
        // set intent action
        intent.action = ACTION_SEND
        // put data to be sent into intent
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.type = "text/plain"
        // begin share interface
        startActivity(Intent.createChooser(intent, "Share to: "))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    // get runtime request permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray

    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL) {
            // if request was granted, make call
            if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    makeCall()
                }
            } else {
                // notify that permission was denied
                Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initiateDelete() {
        // send contact to viewmodel to delete from database
        viewModel.deleteContact(contactId)
        Toast.makeText(this, "$contactName deleted successfully", Toast.LENGTH_SHORT).show()
        finish()
    }

    companion object {
        private var REQUEST_CALL = 101
    }
}
