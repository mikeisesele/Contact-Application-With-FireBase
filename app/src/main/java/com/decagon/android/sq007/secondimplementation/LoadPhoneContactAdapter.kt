package com.decagon.android.sq007.secondimplementation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.R
import com.decagon.android.sq007.model.Contact
import kotlin.collections.ArrayList

class LoadPhoneContactAdapter(private val recyclerList: ArrayList<Contact>) : RecyclerView.Adapter<LoadPhoneContactAdapter.PhoneRecyclerViewViewHolder>() {

/**
     * recycler view adapter takes an argument (a data source)
     * recycler view adapter extends RecyclerView.Adapter class
     */

    //
    // inner class phone recycler view view holder takes the view type to be shown, and places it in the view holder
    inner class PhoneRecyclerViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneRecyclerViewViewHolder {

        // creates the viewHolders that holds the data to be displayed
        val recyclerLayout = LayoutInflater.from(parent.context).inflate(R.layout.load_phone_contact_model, parent, false)

        // returns a view holder with the view to be shown inside
        return PhoneRecyclerViewViewHolder(recyclerLayout)
    }

    override fun onBindViewHolder(holder: PhoneRecyclerViewViewHolder, position: Int) {

        // bind data to each recycler view
        holder.itemView.apply {
            findViewById<TextView>(R.id.tv_load_contact_first_name).text = recyclerList[position].firstName
            findViewById<TextView>(R.id.tv_load_contact_surname_name).text = recyclerList[position].phoneNumber
        }
    }

    // gets the count of data to be used for binding
    override fun getItemCount(): Int {
        return recyclerList.size
    }
}
