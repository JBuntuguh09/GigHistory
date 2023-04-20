package com.lonewolf.pasco.adaptors

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lonewolf.pasco.R
import com.lonewolf.pasco.dialog.show_me
import com.lonewolf.pasco.resources.Storage
import com.squareup.picasso.Picasso

class RecyclerViewDictionary(context: Activity, arrayList: ArrayList<HashMap<String, String>>)
    : RecyclerView.Adapter<RecyclerViewDictionary.MyHolder>() {

    private var arrayList = ArrayList<HashMap<String, String>>()
    private var context : Activity
    private var storage : Storage

    init {
        this.arrayList =arrayList
        this.context = context
        this.storage = Storage(context)
    }

    inner class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var pic : ImageView
        var word : TextView
        var meaning : TextView
        var lin : LinearLayout


        init {
            pic = itemview.findViewById(R.id.imgPic)
            word = itemview.findViewById(R.id.txtWord)
            meaning = itemview.findViewById(R.id.txtMeaning)
            lin = itemview.findViewById(R.id.linMain)

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewDictionary.MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_dictionary,parent, false)

        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewDictionary.MyHolder, position: Int) {
        val hash = arrayList[position]
        holder.word.text = hash["Word"]
        holder.meaning.text = hash["Meaning"]

        if(hash["Pic"]!=="no image" && !hash["Pic"].isNullOrEmpty()){
            Picasso.get().load(hash["Pic"]).into(holder.pic)
        }

        holder.lin.setOnClickListener {
            show_me.wordDic(context, holder.lin, hash)
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}