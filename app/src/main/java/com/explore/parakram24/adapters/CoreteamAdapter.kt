package com.explore.parakram24.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.explore.parakram24.CoreTeamData
import com.explore.parakram24.R
import kotlin.math.max
import kotlin.math.min

class CoreteamAdapter(
    private var teamList: List<CoreTeamData>) :RecyclerView.Adapter<CoreteamAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.iv_person)
        val name : TextView = view.findViewById(R.id.tv_name)
        val position : TextView = view.findViewById(R.id.tv_position_team)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_item_coreteam, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return teamList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = teamList[position]
        if (person.image.isEmpty()) {
            holder.image.setImageResource(R.drawable.ic_loading)
        } else {

            Glide.with(holder.image.context).load(person.image).apply(
                RequestOptions().placeholder(R.drawable.ic_loading).error(R.drawable.img)
                    .diskCacheStrategy(
                    DiskCacheStrategy.ALL
                )
            ).into(holder.image)
        }
        holder.name.text = person.name
        val text =  "${person.position} - ${person.team}"
        holder.position.text = text

    }

    fun setData(newData: List<CoreTeamData>) {
        val sizeBefore = teamList.size
        teamList = newData
        val sizeAfter = newData.size
        notifyItemRangeChanged(0, min(sizeBefore, sizeAfter))
        notifyItemRangeInserted(min(sizeBefore, sizeAfter), max(sizeBefore, sizeAfter) - min(sizeBefore, sizeAfter))
        notifyItemRangeRemoved(max(sizeBefore, sizeAfter), max(sizeBefore, sizeAfter) - min(sizeBefore, sizeAfter))
    }


}

