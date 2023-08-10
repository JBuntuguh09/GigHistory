package com.lonewolf.pasco.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.lonewolf.pasco.R
import com.lonewolf.pasco.resources.ShortCut_To
import com.lonewolf.pasco.resources.Storage

class RecyclerViewObj (context : Context, arrayList: ArrayList<HashMap<String, String>>) : RecyclerView.Adapter<RecyclerViewObj.MyHolder>() {
    private var arrayList = ArrayList<HashMap<String, String>>()
    private var context : Context
    private var storage : Storage

    init {
        this.arrayList =arrayList
        this.context = context
        this.storage = Storage(context)
    }

    inner class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        var score: TextView
        var date: TextView
        var name: TextView
        val card : CardView


        init {
            card = itemview.findViewById(R.id.cardMain)
            name = itemview.findViewById(R.id.txtName)
            score = itemview.findViewById(R.id.txtScore)
            date = itemview.findViewById(R.id.txtNum)

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewObj.MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_leader,parent, false)

        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewObj.MyHolder, position: Int) {

        val hash = arrayList[position]

        holder.date.text = hash["date"]
        holder.name.text = hash["name"]
        holder.score.text = hash["score"]
        holder.date.textSize = 13f



    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}