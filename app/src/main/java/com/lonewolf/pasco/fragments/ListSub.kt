package com.lonewolf.pasco.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.lonewolf.pasco.MainBase
import com.lonewolf.pasco.R
import com.lonewolf.pasco.adaptors.RecyclerViewListSub
import com.lonewolf.pasco.database.Notes
import com.lonewolf.pasco.database.NotesViewModel
import com.lonewolf.pasco.database.TopicsViewModel
import com.lonewolf.pasco.resources.ShortCut_To
import com.lonewolf.pasco.resources.Storage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListSub.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListSub : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage: Storage
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var progressBar : ProgressBar
    private lateinit var topicsViewModel: TopicsViewModel
    private lateinit var notesViewModel: NotesViewModel
    private var liveData: MutableLiveData<String> = MutableLiveData()
    private lateinit var search : EditText
    private  var arrayList = ArrayList<HashMap<String, String>>()
    private  var arrayListOff = ArrayList<HashMap<String, String>>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_sub, container, false)

        storage = Storage(requireContext())
        databaseReference = FirebaseDatabase.getInstance().reference
        recyclerView = view.findViewById(R.id.recyclerView)
        linearLayoutManager = LinearLayoutManager(requireContext())
        progressBar = (activity as MainBase).mainBaseBinding.progressBar
        search = view.findViewById(R.id.edtSearch)


        notesViewModel = ViewModelProvider(this, defaultViewModelProviderFactory).get(NotesViewModel::class.java)


//        if(data!!.isNotEmpty()){
//            println("gh ${data}")
//            getTextbookOffline(data)
//        }else{
//            println("mmmmmmm")
//            getTextBookOnline()
//        }
        notesViewModel.liveData.observe(viewLifecycleOwner){data->
                if(data.isNotEmpty()){
                    println("gh ${data}")
                    getTextbookOffline(data)
                }else{
                    println("mmmmmmm")
                    getTextBookOnline()
                }
            notesViewModel.liveData.removeObservers(viewLifecycleOwner)
        }


        // showUI()
        getButton()
        //syncOnline()
        return view
    }

    private fun getButton() {
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                liveData.postValue(p0.toString())
            }

        })

        liveData.observe(viewLifecycleOwner){

        }
    }

    private fun getTextbookOffline(data: List<Notes>) {
        val listUniq = ArrayList<HashMap<String, String>>()
        val list = ArrayList<String>()

        for (a in data){
            val hash = HashMap<String, String>()
            hash["Note_Id"] = a.Note_Id.toString()
            hash["Topic"] = a.Topic
            hash["Sub_Topic"] = a.Sub_Topic
            hash["Content"] = a.Content
            hash["Date_Created"] = a.CreatedDatetime
            hash["val"] = a.Sub_Topic
            hash["Priority"] = "5"

            val hashOff = HashMap<String, String>()
            hashOff["Topic"] = a.Topic
            hashOff["Sub_Topic"] = a.Sub_Topic
            hashOff["Content"] = a.Content
            hashOff["Date_Created"] = a.CreatedDatetime
            hashOff["val"] = a.Sub_Topic
            hashOff["Priority"] = a.Priority.toString()
            arrayList.add(hash)
            arrayListOff.add(hashOff)

            //if(!list.contains(a.Sub_Topic)){
            list.add(a.Sub_Topic)
            listUniq.add(hash)
            //  }
        }

        if (arrayList.size>0){
            ShortCut_To.sortData(arrayListOff, "Priority")
            ShortCut_To.sortData(arrayList, "Priority")

            progressBar.visibility = View.GONE
           // notesViewModel.deleteAll()
            syncOnline()

            try {
                val recyclerViewList = RecyclerViewListSub(requireContext(), listUniq, 1)
                recyclerView.layoutManager = linearLayoutManager
                recyclerView.itemAnimator = DefaultItemAnimator()
                recyclerView.adapter = recyclerViewList
            }catch (e:Exception){
                e.printStackTrace()
            }

        }
    }

    private fun getTextBookOnline() {
        val listUniq = ArrayList<HashMap<String, String>>()
        val list = ArrayList<String>()
        progressBar.visibility = View.VISIBLE
        databaseReference.child("Notes").child("History").child(storage.project!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    println("mooo 2: $p0")
                    for (grand in p0.children){
                        for (father in grand.children){
                            val hash = HashMap<String, String>()
                            hash["Note_Id"] = father.child("Note_Id").value.toString()
                            hash["Topic"] = father.child("Topic").value.toString()
                            hash["Sub_Topic"] = father.child("Sub_Topic").value.toString()
                            hash["Content"] = father.child("Content").value.toString()
                            hash["Date_Created"] = father.child("Date_Created").value.toString()
                            hash["val"] = father.child("Sub_Topic").value.toString()
                            hash["Priority"] =  father.child("Priority").value.toString()

                            val hashOff = HashMap<String, String>()
                            hashOff["Topic"] = father.child("Topic").value.toString()
                            hashOff["Sub_Topic"] = father.child("Sub_Topic").value.toString()
                            hashOff["Content"] = father.child("Content").value.toString()
                            hashOff["Date_Created"] = father.child("Date_Created").value.toString()
                            hashOff["val"] = father.child("Sub_Topic").value.toString()
                            hashOff["Priority"] =  father.child("Priority").value.toString()

                            arrayList.add(hash)
                            arrayListOff.add(hashOff)


                            if(!list.contains(father.child("Sub_Topic").value.toString())){
                                list.add(father.child("Sub_Topic").value.toString())
                                listUniq.add(hash)
                            }

                        }
                    }
                    if (arrayList.size>0){
                        ShortCut_To.sortData(arrayListOff, "Priority")
                        ShortCut_To.sortData(arrayList, "Priority")

                        insertOffline(arrayList)
                        println("hhhhh : $arrayList")
                        progressBar.visibility = View.GONE

                        try {
                            val recyclerViewList = RecyclerViewListSub(requireContext(), listUniq, 1)
                            recyclerView.layoutManager = linearLayoutManager
                            recyclerView.itemAnimator = DefaultItemAnimator()
                            recyclerView.adapter = recyclerViewList
                        }catch (e:Exception){
                            e.printStackTrace()
                        }

                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
    }

    private fun insertOffline(arrayList:ArrayList<HashMap<String, String>>){
        for(a in 0 until arrayList.size){
            val hash = arrayList.get(a)
            val notex = Notes(0, hash.get("Topic")!!, hash.get("Sub_Topic")!!,
                hash.get("Content")!!, hash.get("Priority")!!.toInt(),hash.get("Date_Created")!!)
            notesViewModel.insert(notex)

        }
    }

    override fun onResume() {
        (activity as MainBase).mainBaseBinding.txtTopic.text = storage.project
        super.onResume()
    }

    fun syncOnline(){
          val arrayListNew = ArrayList<HashMap<String, String>>()
        databaseReference.child("Notes").child("History").child(storage.project!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    arrayListNew.clear()
                    println("mooo : $p0")
                    for (grand in p0.children){
                        for (father in grand.children){
                            val hash = HashMap<String, String>()

                            hash["Topic"] = father.child("Topic").value.toString()
                            hash["Sub_Topic"] = father.child("Sub_Topic").value.toString()
                            hash["Content"] = father.child("Content").value.toString()
                            hash["Date_Created"] = father.child("Date_Created").value.toString()
                            hash["val"] = father.child("Sub_Topic").value.toString()
                            hash["Priority"] =  father.child("Priority").value.toString()


                            arrayListNew.add(hash)


                        }
                    }

                    ShortCut_To.sortData(arrayListOff, "Priority")
                    ShortCut_To.sortData(arrayListNew, "Priority")

                    if(arrayListNew!=arrayListOff){
                        println("bbbbb : ${arrayListNew.sortBy { "Priority" }}")
                        println("bbbbb22 : ${arrayListOff.sortBy { "Priority" }}")

                        notesViewModel.deleteAll(storage.project!!)

                        insertOffline(arrayListNew)
                    }

                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListSub.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListSub().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}