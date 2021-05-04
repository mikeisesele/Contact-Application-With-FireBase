package com.decagon.android.sq007.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.Utils.ClickListener
import com.decagon.android.sq007.adapter.RecyclerViewAdapter
import com.decagon.android.sq007.databinding.ActivityMainBinding
import com.decagon.android.sq007.controller.ContactViewModel
import com.decagon.android.sq007.model.Contact

class MainActivity() : AppCompatActivity(), ClickListener {

    var handler = Handler()
    private lateinit var viewModel: ContactViewModel
    private lateinit var recyclerList: ArrayList<Contact>
    private lateinit var recyclerViewAdapter : RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        viewModel.getUpdatedData()

        viewModel.contactList.observe(this,{
            val myRecyclerLayout: RecyclerView = binding.recyclerView
            recyclerViewAdapter = RecyclerViewAdapter(this, it)
            myRecyclerLayout.adapter = recyclerViewAdapter
            recyclerList = it
        })

        binding.floatingAddContactButton.setOnClickListener{
            startActivity(Intent(this, SaveContactActivity::class.java))
        }

//        binding.loadPhoneContactButton.setOnClickListener{
//            startActivity(Intent(this, LoadPhoneContactActivity::class.java))
//        }

//        binding.recyclerView.setOnClickListener{
//            startActivity(Intent(this, ContactProfileActivity::class.java))
//        }

            // get data for recyclerView and save to recycler list
          //  recyclerList = ContactViewModel.updatedContactList

            // get recycle list adapter and pass in data to be binded, save in a variable


            // get the recycler view layout from xml


            // set the recycler view adapter to the recyclerview layout

    }
    override fun onItemClicked(position: Int) {
        var intent = Intent(this, ContactProfileActivity::class.java)
        intent.putExtra("personName", recyclerList[position].firstName)
        intent.putExtra("personId", recyclerList[position].id)
        intent.putExtra("personNumber", recyclerList[position].phoneNumber)
        Log.i("Id", "$recyclerList[].id")
        startActivity(intent)
     }
}