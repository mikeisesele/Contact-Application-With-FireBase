package com.decagon.android.sq007.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.Utils.ClickListener
import com.decagon.android.sq007.adapter.RecyclerViewAdapter
import com.decagon.android.sq007.controller.ContactViewModel
import com.decagon.android.sq007.databinding.ActivityMainBinding
import com.decagon.android.sq007.model.Contact
import com.decagon.android.sq007.secondimplementation.LoadPhoneContactActivity

class MainActivity() : AppCompatActivity(), ClickListener {

    /**
     * @param viewmodel ->  Variable to hold view model. the application's access route to the database
     * @param recyclerlist -> This variable holds a list of contact objects which will be used by the recycler view adapter
     * @param recyclerviewadapter -> This variable holds a reference to the recycler view adapter
     */
    private lateinit var viewModel: ContactViewModel
    private lateinit var recyclerList: ArrayList<Contact>
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialise view binding
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialize view model
        viewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        // call for updated data to populate recycler view on app lunch
        viewModel.getUpdatedData()

        // viewModel observer to observe recycler the list of objects coming from database for any changes
        viewModel.contactList.observe(
            this,
            {
                // get recyclerView xml layout
                val myRecyclerLayout: RecyclerView = binding.recyclerView

                // set the list to be observed to the list coming from database
                recyclerList = it

                // set the recycler view adapter to a variable
                recyclerViewAdapter = RecyclerViewAdapter(this, it)

                // set the xml recycler layout to the recycler view adapter
                myRecyclerLayout.adapter = recyclerViewAdapter
            }
        )

        // lunch save contact activity
        binding.floatingAddContactButton.setOnClickListener {
            startActivity(Intent(this, SaveContactActivity::class.java))
        }

        // lunch activity ot load phone contact
        binding.loadPhoneContactButton.setOnClickListener {
            startActivity(Intent(this, LoadPhoneContactActivity::class.java))
        }
    }

    // send contact object details to contact profile activity when any recycler view is clicked
    override fun onItemClicked(position: Int) {
        val intent = Intent(this, ContactProfileActivity::class.java)
        intent.putExtra("personName", recyclerList[position].firstName)
        intent.putExtra("personId", recyclerList[position].id)
        intent.putExtra("personNumber", recyclerList[position].phoneNumber)
        intent.putExtra("personSurname", recyclerList[position].lastName)
        startActivity(intent)
    }
}
