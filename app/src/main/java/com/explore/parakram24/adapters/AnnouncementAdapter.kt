package com.explore.parakram24.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.explore.parakram24.NotificationData
import com.explore.parakram24.R
import com.google.android.material.textview.MaterialTextView
import kotlin.math.max
import kotlin.math.min

class AnnouncementAdapter(private var notificationList : List<NotificationData>) : RecyclerView.Adapter<AnnouncementAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title : MaterialTextView = view.findViewById(R.id.notification_title)
        val body : MaterialTextView = view.findViewById(R.id.notification_body)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = notificationList[position]
        Log.i("data in adapter",data.toString())
        holder.title.text = data.title
        holder.body.text = data.body
    }

    fun setData(newData: List<NotificationData>) {
        val sizeBefore = notificationList.size
        notificationList = newData
        val sizeAfter = newData.size
        notifyItemRangeChanged(0, min(sizeBefore, sizeAfter))
        notifyItemRangeInserted(min(sizeBefore, sizeAfter), max(sizeBefore, sizeAfter) - min(sizeBefore, sizeAfter))
        notifyItemRangeRemoved(max(sizeBefore, sizeAfter), max(sizeBefore, sizeAfter) - min(sizeBefore, sizeAfter))
    }
}
