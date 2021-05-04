package com.decagon.android.sq007.view

//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import androidx.recyclerview.widget.RecyclerView
//import com.decagon.android.sq007.Utils.ClickListener
//import com.decagon.android.sq007.adapter.RecyclerViewAdapter
//import com.decagon.android.sq007.controller.ContactViewModel
//import com.decagon.android.sq007.databinding.ActivityLoadPhoneContactBinding
//import com.decagon.android.sq007.model.Contact
//
//class LoadPhoneContactActivity : AppCompatActivity() {
//
//    private lateinit var recyclerList: ArrayList<Contact>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//
//        val binding: ActivityLoadPhoneContactBinding = ActivityLoadPhoneContactBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//
//        // get data for recyclerView and save to recycler list
//        recyclerList
//
//        // get recycle list adapter and pass in data to be binded, save in a variable
//        val recyclerViewListAdapter = RecyclerViewAdapter(recyclerList)
//
//        // get the recycler view layout from xml
//        val myRecyclerLayout: RecyclerView = binding.phoneContactRecyclerView
//
//        // set the recycler view adapter to the recyclerview layout
//        myRecyclerLayout.adapter = recyclerViewListAdapter
//    }
//}