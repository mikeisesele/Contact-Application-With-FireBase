package com.decagon.android.sq007.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.R
import com.decagon.android.sq007.adapter.RecyclerViewAdapter
import com.decagon.android.sq007.databinding.ActivityMainBinding
import com.decagon.android.sq007.controller.ContactViewModel
import com.decagon.android.sq007.controller.DataChangeSuccessListener
import com.decagon.android.sq007.model.Contact

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingAddContactButton.setOnClickListener{
            startActivity(Intent(this, SaveContactActivity::class.java))
        }

        binding.recyclerView.setOnClickListener{
            startActivity(Intent(this, ContactProfileActivity::class.java))
        }

        // get data for recyclerView and save to recycler list
//        val recyclerList = ContactViewModel.updatedContactList

        val recyclerList = ContactViewModel.updatedContactList


        // get recycle list adapter and pass in data to be binded, save in a variable
        val recyclerViewListAdapter = RecyclerViewAdapter(recyclerList)

        // get the recycler view layout from xml
        val myRecyclerLayout: RecyclerView = binding.recyclerView

        // set the recycler view adapter to the recyclerview layout
        myRecyclerLayout.adapter = recyclerViewListAdapter

    }
}

