package com.lonewolf.pasco.adaptors

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lonewolf.pasco.MainBase
import com.lonewolf.pasco.R
import com.lonewolf.pasco.dialog.show_me
import com.lonewolf.pasco.fragments.*
import com.lonewolf.pasco.resources.Storage

class RecyclerViewListSub(context: Context, arrayList : ArrayList<HashMap<String, String>>, type : Int)
    : RecyclerView.Adapter<RecyclerViewListSub.MyHolder>()  {

    private var arrayList = ArrayList<HashMap<String, String>>()
    private var context : Context
    private var storage : Storage
    private var type : Int

    init {
        this.arrayList =arrayList
        this.context = context
        this.storage = Storage(context)
        this.type = type
    }

    inner class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var button : TextView

        init {
            button = itemview.findViewById(R.id.btnList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewListSub.MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_text,parent, false)

        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewListSub.MyHolder, position: Int) {
        val hash = arrayList[position]
        holder.button.setText(hash["val"])
        holder.button.setOnClickListener {
            if(type==1){
//                storage.fragVal = hash["val"]
//                storage.randVal = hash["Note_Id"]
                val notes = (context as MainBase).notes
                notes.clear()
                notes["Sub_Topic"] = hash["val"]!!
                notes["Content"] = hash["Content"]!!

                (context as MainBase).navTo(WassceTextbook(), hash["val"]!!, "List Sub",1)

            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}