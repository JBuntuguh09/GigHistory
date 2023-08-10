package com.lonewolf.pasco.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.lonewolf.pasco.R
import com.lonewolf.pasco.resources.ShortCut_To
import com.lonewolf.pasco.resources.Storage

class RecyclerViewDashboard(context : Context, arrayList: ArrayList<HashMap<String, String>>) : RecyclerView.Adapter<RecyclerViewDashboard.MyHolder>() {
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
        var num: TextView
        var name: TextView
        val card : CardView


        init {
            card = itemview.findViewById(R.id.cardMain)
            name = itemview.findViewById(R.id.txtName)
            score = itemview.findViewById(R.id.txtScore)
            num = itemview.findViewById(R.id.txtNum)

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewDashboard.MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_leader,parent, false)

        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewDashboard.MyHolder, position: Int) {

        val hash = arrayList[position]
        val pos = ShortCut_To.numberToPosition(position+4)
        holder.num.text = pos
        holder.name.text = hash["name"]
        holder.score.text = hash["score"]



    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}