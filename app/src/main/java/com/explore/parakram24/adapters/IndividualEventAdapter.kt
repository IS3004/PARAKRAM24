package com.explore.parakram24.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.explore.parakram24.MatchData
import com.explore.parakram24.R
import com.google.android.material.circularreveal.cardview.CircularRevealCardView
import kotlin.math.max
import kotlin.math.min


class IndividualEventAdapter(private var gamesList: List<MatchData>, private var current : String): RecyclerView.Adapter<IndividualEventAdapter.ViewHolder>()  {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val leagueTextView: TextView = view.findViewById(R.id.league)
        val team1Image: ImageView = view.findViewById(R.id.team1Image)
        val team1Name: TextView = view.findViewById(R.id.team1name)
        //val buttonFav1: CheckBox = view.findViewById(R.id.button_fav1)
        val team2Image: ImageView = view.findViewById(R.id.team2Image)
        val team2Name: TextView = view.findViewById(R.id.team2name)
        //val buttonFav2: CheckBox = view.findViewById(R.id.button_fav2)
        val dateTextView: TextView = view.findViewById(R.id.date)
        val timeTextView: TextView = view.findViewById(R.id.time)
        val venueTextView: TextView = view.findViewById(R.id.venue)
        val constraintLayout : ConstraintLayout = view.findViewById(R.id.cl_event)
        val llScore: LinearLayoutCompat = view.findViewById(R.id.ll_score)
        val leftField1 : TextView = view.findViewById(R.id.tv_leftField1)
        val leftField2 : TextView = view.findViewById(R.id.tv_leftField2)
        val leftField3 : TextView = view.findViewById(R.id.tv_leftField3)
        val field1 : TextView = view.findViewById(R.id.tv_field1)
        val field2 : TextView = view.findViewById(R.id.tv_field2)
        val field3 : TextView = view.findViewById(R.id.tv_field3)
        val rightField1 : TextView = view.findViewById(R.id.tv_rightField1)
        val rightField2 : TextView = view.findViewById(R.id.tv_rightField2)
        val rightField3 : TextView = view.findViewById(R.id.tv_rightField3)
        val llField1: LinearLayoutCompat = view.findViewById(R.id.ll_field1)
        val llField2: LinearLayoutCompat = view.findViewById(R.id.ll_field2)
        val llField3: LinearLayoutCompat = view.findViewById(R.id.ll_field3)
        val card : CircularRevealCardView = view.findViewById(R.id.rv_events_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndividualEventAdapter.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.events, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val match = gamesList[position]
        holder.apply {
            leagueTextView.text = match.league
            team1Name.text = match.teamAname
            team2Name.text = match.teamBname
            dateTextView.text = match.date
            timeTextView.text = match.time
            venueTextView.text = match.venue

            if(match.score.field1=="0"){
                llField1.visibility = View.GONE
            }
            else{
                llField1.visibility = View.VISIBLE
                leftField1.text = match.score.leftField1
                field1.text = match.score.field1
                rightField1.text = match.score.rightField1
            }
            if(match.score.field2=="0"){
                llField2.visibility = View.GONE
            }
            else{
                llField2.visibility = View.VISIBLE
                leftField2.text = match.score.leftField2
                field2.text = match.score.field2
                rightField2.text = match.score.rightField2
            }
            if(match.score.field3=="0"){
                llField3.visibility = View.GONE
            }
            else{
                llField3.visibility = View.VISIBLE
                leftField3.text = match.score.leftField3
                field3.text = match.score.field3
                rightField3.text = match.score.rightField3
            }

            if (match.teamAImage != "") {
                Glide.with(holder.team1Image.context).load(match.teamAImage).apply(
                    RequestOptions().placeholder(R.drawable.ic_loading).error(R.drawable.img)
                        .diskCacheStrategy(
                            DiskCacheStrategy.ALL
                        )
                ).into(holder.team1Image)
            }
            if (match.teamBImage != "") {
                Glide.with(holder.team2Image.context).load(match.teamBImage).apply(
                    RequestOptions().placeholder(R.drawable.ic_loading).error(R.drawable.img)
                        .diskCacheStrategy(
                            DiskCacheStrategy.ALL
                        )
                ).into(holder.team2Image)
            }
        }

        holder.constraintLayout.setOnClickListener{
            holder.llScore.visibility =
                if(holder.llScore.visibility == View.GONE){
                    View.VISIBLE
                }else {
                    View.GONE
                }
        }

//        holder.buttonFav1.setOnClickListener {
//            if(holder.buttonFav1.isChecked && holder.buttonFav2.isChecked){
//                holder.buttonFav1.isChecked = true
//                holder.buttonFav2.isChecked = false
//            }
//        }
//        holder.buttonFav2.setOnClickListener {
//            if(holder.buttonFav2.isChecked && holder.buttonFav1.isChecked){
//                holder.buttonFav2.isChecked = true
//                holder.buttonFav1.isChecked = false
//            }
//        }
//
//
//        holder.card.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context,R.anim.recycler_view_animations))
    }

    override fun getItemCount(): Int {
        return gamesList.size
    }

    fun setData(newData: List<MatchData>) {
//        val sizeBefore = gamesList.size
//        gamesList = newData
//        val sizeAfter = newData.size
//        notifyItemRangeChanged(0, min(sizeBefore, sizeAfter))
//        notifyItemRangeInserted(min(sizeBefore, sizeAfter), max(sizeBefore, sizeAfter) - min(sizeBefore, sizeAfter))
//        notifyItemRangeRemoved(max(sizeBefore, sizeAfter), max(sizeBefore, sizeAfter) - min(sizeBefore, sizeAfter))

        val oldData = ArrayList(gamesList) // Make a copy of the old data
        gamesList = newData // Update gamesList with the new data
        Log.i("old",oldData.toString())
        Log.i("new",newData.toString())

        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = oldData.size
            override fun getNewListSize(): Int = newData.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldData[oldItemPosition].key == newData[newItemPosition].key // Assuming each item has a unique identifier
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldData[oldItemPosition] == newData[newItemPosition]
            }
        })

        diffResult.dispatchUpdatesTo(this) // Dispatch updates to the adapter


    }

}

//class IndividualEventDiffCallback(
//    private val oldList: List<MatchData>,
//    private val newList: List<MatchData>
//) : DiffUtil.Callback() {
//
//    override fun getOldListSize(): Int {
//        return oldList.size
//    }
//
//    override fun getNewListSize(): Int {
//        return newList.size
//    }
//
//    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        return oldList[oldItemPosition].key == newList[newItemPosition].key
//    }
//
//    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        return oldList[oldItemPosition] == newList[newItemPosition]
//    }
//}
