package com.decagon.android.sq007.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.R
import com.decagon.android.sq007.Utils.ClickListener
import com.decagon.android.sq007.databinding.ActivityMainBinding
import com.decagon.android.sq007.model.Contact
import java.util.*
import kotlin.collections.ArrayList

/**
 * recycler view adapter takes an argument (a data source)
 * recycler view adapter extends RecyclerView.Adapter class
 */

class RecyclerViewAdapter(private val clickListener: ClickListener, private val recyclerList: ArrayList<Contact>) : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewViewHolder>() {

//
    // inner class recycler view view holder takes the view type to be shown, and places it in the view holder
    inner class RecyclerViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {

        // creates the viewHolders that holds the data to be displayed
        val recyclerLayout = LayoutInflater.from(parent.context).inflate(R.layout.recycler_contact_model, parent, false)

        // returns a view holder with the view to be shown inside
        return RecyclerViewViewHolder(recyclerLayout)
    }

    override fun onBindViewHolder(holder: RecyclerViewViewHolder, position: Int) {

        holder.itemView.apply {
            findViewById<TextView>(R.id.tv_first_name).text = recyclerList[position].firstName
            findViewById<TextView>(R.id.tv_surname_name).text = recyclerList[position].lastName
            findViewById<TextView>(R.id.tv_name_initial).text =
                recyclerList[position].firstName?.take(1)?.toUpperCase(Locale.ROOT)
        }

        holder.itemView.setOnClickListener{
            clickListener.onItemClicked(position)
        }
    }

    // gets the count of data to be used for binding
    override fun getItemCount(): Int {
        return recyclerList.size
    }
}
